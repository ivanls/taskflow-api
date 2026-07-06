package com.ivan.taskflow.service;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.TaskResponse;
import com.ivan.taskflow.dto.UpdateTaskRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(CreateTaskRequest request);
    Page<TaskResponse> getAllTasks(Boolean completed, String title, Pageable pageable);
    TaskResponse getTaskById(Long id);
    TaskResponse updateTask(Long id, UpdateTaskRequest request);
    void deleteTask(Long id);
}
