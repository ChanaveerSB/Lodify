package com.loadify.service;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loadify.entity.Feedback;
import com.loadify.repository.BookingRepository;
import com.loadify.repository.FeedbackRepository;
import com.loadify.repository.TruckRepository;

@Service
public class AdminService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private TruckRepository truckRepository;

    public Map<String, Object> getDashboardStats(String filter) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = switch (filter.toLowerCase()) {
            case "day" -> now.with(LocalTime.MIN);
            case "month" -> now.withDayOfMonth(1).with(LocalTime.MIN);
            case "year" -> now.withDayOfYear(1).with(LocalTime.MIN);
            default -> now.minusYears(1);
        };

        Map<String, Object> stats = new HashMap<>();

        // 1. Financial Tally
        Double totalRev = bookingRepository.calculateTotalRevenueSince(start);
        stats.put("totalRevenue", totalRev != null ? totalRev : 0.0);

        // 2. Supply Data (Trucks)
        stats.put("recentTrucks", truckRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(start));

        // 3. Demand Data (Bookings)
        stats.put("recentBookings", bookingRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(start));
        
        // 4. Feedback Data (Complaints) - IMPORTANT: Don't leave this out!
        stats.put("recentFeedbacks", feedbackRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(start));

        return stats;
    }

    // This handles the incoming feedback from the user's "Complaints" page
    public void saveFeedback(Feedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now()); // Ensure timestamp is set before saving
        feedbackRepository.save(feedback);
    }
}