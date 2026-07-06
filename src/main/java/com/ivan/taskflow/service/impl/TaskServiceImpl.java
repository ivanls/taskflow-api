package com.ivan.taskflow.service.impl;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.TaskResponse;
import com.ivan.taskflow.dto.UpdateTaskRequest;
import com.ivan.taskflow.entity.Task;
import com.ivan.taskflow.exception.TaskNotFoundException;
import com.ivan.taskflow.repository.TaskRepository;
import com.ivan.taskflow.service.TaskService;

import com.ivan.taskflow.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());

        taskRepository.save(task);

        return toResponse(task);
    }

    @Override
    public Page<TaskResponse> getAllTasks (Boolean completed, String title, Pageable pageable) {

        Specification<Task> spec =
                Specification.<Task>unrestricted()
                        .and(TaskSpecification.hasCompleted(completed))
                        .and(TaskSpecification.hasTitle(title));

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        return taskPage.map(this::toResponse);
    }

    @Override
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        return toResponse(task);
    }

    @Override
    public TaskResponse updateTask (Long id, UpdateTaskRequest request){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.getCompleted());

        taskRepository.save(task);

        return toResponse(task);
    }

    @Override
    public void deleteTask(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));

        taskRepository.delete(task);
    }

    private TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setCompleted(task.isCompleted());

        return response;
    }
}