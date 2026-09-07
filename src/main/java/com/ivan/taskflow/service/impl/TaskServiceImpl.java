package com.ivan.taskflow.service.impl;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.TaskResponse;
import com.ivan.taskflow.dto.UpdateTaskRequest;
import com.ivan.taskflow.entity.Task;
import com.ivan.taskflow.entity.User;
import com.ivan.taskflow.exception.TaskNotFoundException;
import com.ivan.taskflow.repository.TaskRepository;
import com.ivan.taskflow.repository.UserRepository;
import com.ivan.taskflow.service.TaskService;
import com.ivan.taskflow.specification.TaskSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {

        User currentUser = getCurrentUser();

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(false);
        task.setCreatedAt(LocalDateTime.now());
        task.setUser(currentUser);

        taskRepository.save(task);

        return toResponse(task);
    }

    @Override
    public Page<TaskResponse> getAllTasks (Boolean completed, String title, Pageable pageable) {

        User currentUser = getCurrentUser();

        Specification<Task> spec =
                Specification.<Task>unrestricted()
                        .and(TaskSpecification.hasUser(currentUser.getId()))
                        .and(TaskSpecification.hasCompleted(completed))
                        .and(TaskSpecification.hasTitle(title));

        Page<Task> taskPage = taskRepository.findAll(spec, pageable);

        return taskPage.map(this::toResponse);
    }

    @Override
    public TaskResponse getTaskById(Long id) {

        Task task = getTaskForCurrentUser(id);

        return toResponse(task);
    }

    @Override
    public TaskResponse updateTask (Long id, UpdateTaskRequest request){

        Task task = getTaskForCurrentUser(id);

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.getCompleted());
        taskRepository.save(task);

        return toResponse(task);
    }

    @Override
    public void deleteTask(Long id){

        Task task = getTaskForCurrentUser(id);

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

    private User getCurrentUser() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Task getTaskForCurrentUser(Long id) {

        User user = getCurrentUser();

        return taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new TaskNotFoundException("Task not found"));
    }
}