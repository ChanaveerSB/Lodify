package com.loadify.controller;

import com.loadify.dto.DashboardResponse;
import com.loadify.service.DashboardService;
import com.loadify.util.ResponseStructure;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin("*")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    public ResponseEntity<ResponseStructure<DashboardResponse>> admin() {
        return ResponseEntity.ok(ResponseStructure.success(200, "Admin dashboard", dashboardService.summary()));
    }

    @GetMapping("/provider")
    public ResponseEntity<ResponseStructure<DashboardResponse>> provider() {
        return ResponseEntity.ok(ResponseStructure.success(200, "Provider dashboard", dashboardService.summary()));
    }

    @GetMapping("/customer")
    public ResponseEntity<ResponseStructure<DashboardResponse>> customer() {
        return ResponseEntity.ok(ResponseStructure.success(200, "Customer dashboard", dashboardService.summary()));
    }
}
