package com.ivan.taskflow.controller;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.TaskResponse;
import com.ivan.taskflow.dto.UpdateTaskRequest;
import com.ivan.taskflow.entity.Task;
import com.ivan.taskflow.service.TaskService;
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
    public TaskResponse createTask(@RequestBody CreateTaskRequest request) {
        return taskService.createTask(request);
    }


    @GetMapping
    public List<TaskResponse> getAllTasks(){
        return taskService.getAllTasks();
    }


    @GetMapping("/{id}")
    public TaskResponse getTaskById (@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask (@PathVariable Long id, @RequestBody UpdateTaskRequest request) {
        return  taskService.updateTask(id, request);
    }
}
