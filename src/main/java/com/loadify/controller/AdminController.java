package com.loadify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.loadify.repository.FeedbackRepository;
import com.loadify.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Secured endpoint for stats
    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam String filter) {
        return ResponseEntity.ok(adminService.getDashboardStats(filter));
    }

    // Endpoint to get all feedback
    @GetMapping("/feedbacks")
    public ResponseEntity<?> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackRepository.findAllByOrderByCreatedAtDesc());
    }
}


