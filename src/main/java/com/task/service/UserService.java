package com.task.service;

import com.task.dto.request.TaskUpdateDTO;
import com.task.dto.request.UserDTO;
import com.task.dto.request.UserUpdateDTO;
import com.task.dto.response.UserResponseDTO;
import com.task.exception.BadRequestException;
import com.task.exception.ConflictException;
import com.task.exception.NotFoundException;
import com.task.model.Task;
import com.task.model.User;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final KafkaTemplate<String, Serializable> kafkaTemplate;
    private final ModelMapper mapper = new ModelMapper();
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> mapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());
    }

    public UserDTO createUser(UserDTO userDTO) {
        ensureEmailIsUnique(userDTO.getEmail());

        User user = mapper.map(userDTO, User.class);
        user.setCreatedDate(new Date());

        kafkaTemplate.send("userCreate-topic", user);
        return userDTO;
    }

    public void deleteUser(Integer id) {
        ensureUserExists(id);
        ensureUserHasNoAssignedTasks(id);

        kafkaTemplate.send("userDelete-topic", id);
    }

    public UserDTO updateUser(UserUpdateDTO userUpdateDTO) {
        User userExisting = ensureUserExists(userUpdateDTO.getId());
        ensureEmailIsUnique(userUpdateDTO.getEmail());

        User user = mapper.map(userUpdateDTO, User.class);
        user.setCreatedDate(userExisting.getCreatedDate());
        copyMissingFields(user, userUpdateDTO, userExisting);

        kafkaTemplate.send("userUpdate-topic", user);
        return mapper.map(user, UserDTO.class);
    }

    public User ensureUserExists(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
    }

    public void ensureEmailIsUnique(String email) {
        Optional<User> userWithEmail = Optional.ofNullable(userRepository.findByEmail(email));
        if (userWithEmail.isPresent()) {
            throw new ConflictException("Já existe um usuário com esse email");
        }
    }

    public void ensureUserHasNoAssignedTasks(Integer userId) {
        if (!taskRepository.findByCdUser(userId).isEmpty()) {
            throw new BadRequestException("O usuário não pode ser deletado pois ainda tem tarefas associadas a ele");
        }
    }

    private void copyMissingFields(User user, UserUpdateDTO userUpdateDTO, User userExisting) {
        if (userUpdateDTO.getName() == null) {
            user.setName(userExisting.getName());
        }

        if (userUpdateDTO.getEmail() == null) {
            user.setEmail(userExisting.getEmail());
        }
    }
}
