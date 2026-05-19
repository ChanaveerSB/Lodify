package com.loadify.repository;

import com.loadify.entity.Booking;
import com.loadify.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomerUserId(Long userId);
    List<Booking> findByTruckUploadedByUserId(Long providerId);
    long countByBookingStatus(BookingStatus status);

    // long countByCreatedAtAfter(LocalDateTime startDate);

    // For the Demand Table: Get recent bookings
    List<Booking> findAllByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime startDate);

    // For the Tally Card: Calculate Revenue
    @Query("SELECT SUM(b.totalPrice) FROM Booking b WHERE b.createdAt >= :startDate")
    Double calculateTotalRevenueSince(@Param("startDate") LocalDateTime startDate);
}
