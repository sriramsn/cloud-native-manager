package org.example.cloudnativemanager.controller;

import jakarta.validation.Valid;
import org.example.cloudnativemanager.manager.model.Task;
import org.example.cloudnativemanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public Task create(@Valid @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @GetMapping
    public Page<Task> list(
            @RequestParam(required = false) Task.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return taskService.getTasks(status, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return taskService.getTask(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
