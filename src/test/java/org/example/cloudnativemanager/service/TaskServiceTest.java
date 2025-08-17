package org.example.cloudnativemanager.service;

import org.example.cloudnativemanager.manager.model.Task;
import org.example.cloudnativemanager.manager.model.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Test Task");
        sampleTask.setDescription("Test Description");
        sampleTask.setDueDate(LocalDate.now().plusDays(2));
        sampleTask.setStatus(Task.Status.PENDING);
    }

    @Test
    void testCreateTask() {
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);
        Task result = taskService.createTask(sampleTask);
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void testGetTasksByStatus() {
        Page<Task> mockPage = new PageImpl<>(List.of(sampleTask));
        when(taskRepository.findByStatus(Task.Status.PENDING, PageRequest.of(0, 5)))
                .thenReturn(mockPage);

        Page<Task> result = taskService.getTasks(Task.Status.PENDING, PageRequest.of(0, 5));
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        Optional<Task> result = taskService.getTask(1L);
        assertTrue(result.isPresent());
        assertEquals("Test Task", result.get().getTitle());
    }

    @Test
    void testUpdateTask() {
        Task updated = new Task();
        updated.setTitle("Updated Title");
        updated.setDescription("Updated Desc");
        updated.setDueDate(LocalDate.now().plusDays(3));
        updated.setStatus(Task.Status.COMPLETED);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updated);

        Task result = taskService.updateTask(1L, updated);

        assertEquals("Updated Title", result.getTitle());
        assertEquals(Task.Status.COMPLETED, result.getStatus());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskRepository).deleteById(1L);
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }
}
