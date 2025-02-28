package com.example.taskmanagementsystem.repository;

import com.example.taskmanagementsystem.model.Task;
import com.example.taskmanagementsystem.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthor_Email(String authorName);
    List<Task> findByPerformers_Email(String performerName);

    Page<Task> findAll(Specification<Task> specification, Pageable pageable);
}
