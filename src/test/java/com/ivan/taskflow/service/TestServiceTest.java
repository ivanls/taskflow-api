package com.ivan.taskflow.service;

import com.ivan.taskflow.dto.CreateTaskRequest;
import com.ivan.taskflow.dto.TaskResponse;
import com.ivan.taskflow.dto.UpdateTaskRequest;
import com.ivan.taskflow.entity.Task;
import com.ivan.taskflow.exception.TaskNotFoundException;
import com.ivan.taskflow.repository.TaskRepository;
import com.ivan.taskflow.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    // ============================================================
    // CREATE TASK
    // ============================================================

    @Test
    void shouldCreateTaskWithCorrectDataAndMarkItAsNotCompleted() {

        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Preparar presentación del proyecto");
        request.setDescription(
                "Revisar las diapositivas y añadir los últimos cambios antes de la presentación."
        );

        // Act
        TaskResponse response = taskService.createTask(request);

        // Assert
        assertEquals("Preparar presentación del proyecto", response.getTitle());
        assertEquals(
                "Revisar las diapositivas y añadir los últimos cambios antes de la presentación.",
                response.getDescription()
        );
        assertEquals(false, response.isCompleted());
    }


    @Test
    void shouldSaveTaskWhenCreatingANewTask() {

        // Arrange
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Enviar documentación del proyecto");
        request.setDescription(
                "Enviar por correo la documentación necesaria para finalizar el proyecto."
        );

        // Act
        taskService.createTask(request);

        // Assert
        verify(taskRepository).save(
                org.mockito.ArgumentMatchers.any(Task.class)
        );
    }


    // ============================================================
    // GET TASK BY ID
    // ============================================================

    @Test
    void shouldReturnTaskWhenTaskExists() {

        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Preparar presentación del proyecto");
        task.setDescription(
                "Revisar las diapositivas y añadir los últimos cambios."
        );
        task.setCompleted(false);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Act
        TaskResponse response = taskService.getTaskById(1L);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Preparar presentación del proyecto", response.getTitle());
        assertEquals(
                "Revisar las diapositivas y añadir los últimos cambios.",
                response.getDescription()
        );
        assertEquals(false, response.isCompleted());
    }


    @Test
    void shouldThrowExceptionWhenTaskDoesNotExist() {

        // Arrange
        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTaskById(999L)
        );
    }


    // ============================================================
    // UPDATE TASK
    // ============================================================

    @Test
    void shouldUpdateTaskWithNewData() {

        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Preparar documentación");
        existingTask.setDescription(
                "Revisar la documentación inicial del proyecto."
        );
        existingTask.setCompleted(false);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Preparar documentación final");
        request.setDescription(
                "Revisar y completar toda la documentación antes de entregar el proyecto."
        );
        request.setCompleted(true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        // Act
        TaskResponse response = taskService.updateTask(1L, request);

        // Assert
        assertEquals("Preparar documentación final", response.getTitle());
        assertEquals(
                "Revisar y completar toda la documentación antes de entregar el proyecto.",
                response.getDescription()
        );
        assertEquals(true, response.isCompleted());
    }


    @Test
    void shouldThrowExceptionWhenUpdatingTaskThatDoesNotExist() {

        // Arrange
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Actualizar tarea");
        request.setDescription("Actualizar información de la tarea.");
        request.setCompleted(true);

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.updateTask(999L, request)
        );
    }


    @Test
    void shouldSaveTaskAfterUpdatingIt() {

        // Arrange
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Preparar documentación");
        existingTask.setDescription(
                "Revisar la documentación del proyecto."
        );
        existingTask.setCompleted(false);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Preparar documentación final");
        request.setDescription(
                "Completar la documentación final del proyecto."
        );
        request.setCompleted(true);

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(existingTask));

        // Act
        taskService.updateTask(1L, request);

        // Assert
        verify(taskRepository).save(existingTask);
    }


    // ============================================================
    // DELETE TASK
    // ============================================================

    @Test
    void shouldDeleteTaskWhenTaskExists() {

        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Eliminar tarea antigua");
        task.setDescription(
                "Eliminar una tarea que ya no es necesaria."
        );

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository).delete(task);
    }


    @Test
    void shouldThrowExceptionWhenDeletingTaskThatDoesNotExist() {

        // Arrange
        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.deleteTask(999L)
        );
    }
}