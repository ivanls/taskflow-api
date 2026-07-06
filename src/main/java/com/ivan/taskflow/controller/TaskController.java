package com.ivan.taskflow.controller;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.TaskResponse;
import com.ivan.taskflow.dto.UpdateTaskRequest;
import com.ivan.taskflow.entity.Task;
import com.ivan.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }


    @GetMapping
    public Page<TaskResponse> getAllTasks(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) String title,
            @ParameterObject Pageable pageable
    ){
        return taskService.getAllTasks(completed, title, pageable);
    }


    @GetMapping("/{id}")
    public TaskResponse getTaskById (@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask (@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return  taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
