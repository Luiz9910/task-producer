package com.task.repository;

import com.task.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM DBATK.TB_TASKS WHERE CD_USER = ?")
    Task findByCdUser(@Param("id") Integer id);
}
