package org.example.cloudnativemanager.manager.model.repository;

import org.example.cloudnativemanager.manager.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByStatus(Task.Status status, Pageable pageable);
}

