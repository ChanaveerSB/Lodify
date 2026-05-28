package com.loadify.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.loadify.entity.Feedback;
import com.loadify.service.AdminService;

@RestController
@RequestMapping("/api/user")
public class FeedbackController {
    @Autowired
    private AdminService adminService;

    @PostMapping("/feedback")
    public ResponseEntity<?> postFeedback(@RequestBody Feedback feedback) {
        adminService.saveFeedback(feedback);
        return ResponseEntity.ok(Map.of(
        "message", "Feedback saved"
        ));
    }
}
