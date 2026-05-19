package com.loadify.repository;

import com.loadify.entity.Truck;
import com.loadify.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface TruckRepository extends JpaRepository<Truck, Long>, JpaSpecificationExecutor<Truck> {
    List<Truck> findByUploadedByUserId(Long userId);
    long countByStatus(TripStatus status);
    // For the Supply Table: Get recent truck posts
    List<Truck> findAllByCreatedAtAfterOrderByCreatedAtDesc(LocalDateTime startDate);
}
