package com.loadify.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.loadify.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    
    // 1. Gets all feedbacks ever, newest first
    List<Feedback> findAllByOrderByCreatedAtDesc();

    // 2. Gets feedbacks filtered by date (Matches your Admin Dashboard filter)
    // FIX: Change 'Truck' to 'Feedback'
    List<Feedback> findAllByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime startDate);
}