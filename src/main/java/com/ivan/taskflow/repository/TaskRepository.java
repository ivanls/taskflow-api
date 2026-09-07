package com.ivan.taskflow.repository;

import com.ivan.taskflow.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.lang.ScopedValue;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>,
        JpaSpecificationExecutor<Task> {
    Optional<Task> findByIdAndUserId(Long taskId, Long userId);
}
