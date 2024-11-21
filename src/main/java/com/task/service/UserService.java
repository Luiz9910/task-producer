package com.task.service;

import com.task.dto.request.UserDTO;
import com.task.exception.BadRequestException;
import com.task.exception.ConflictException;
import com.task.exception.NotFoundException;
import com.task.model.User;
import com.task.repository.TaskRepository;
import com.task.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {
    @Autowired
    private KafkaTemplate<String, Serializable> kafkaTemplate;

    private final ModelMapper mapper = new ModelMapper();

    private final UserRepository userRepository;

    private final TaskRepository taskRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserDTO createUser(UserDTO userDTO) {
        User userExisting = userRepository.findByEmail(userDTO.getEmail());
        if (userExisting != null) {
            throw new ConflictException("Já existe um usuário com esse email");
        }

        User user = mapper.map(userDTO, User.class);
        user.setCreatedDate(new Date());

        kafkaTemplate.send("userCreate-topic", user);
        return userDTO;
    }

    // TESTAR E CORRIGIR ALGUMAS COISAS NESSA ROTA POR COMPLETO
    public void deleteUser(Integer id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (taskRepository.findByCdUser(id) != null) {
            throw new BadRequestException("O usuário não pode ser deletado pois ainda tem tarefas associadas a ele");
        }

        kafkaTemplate.send("userDelete-topic", id);
    }
}