package com.loadify.service;

import com.loadify.dto.DashboardResponse;
import com.loadify.entity.Truck;
import com.loadify.enums.BookingStatus;
import com.loadify.repository.BookingRepository;
import com.loadify.repository.TruckRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final TruckRepository truckRepository;
    private final BookingRepository bookingRepository;

    public DashboardService(TruckRepository truckRepository, BookingRepository bookingRepository) {
        this.truckRepository = truckRepository;
        this.bookingRepository = bookingRepository;
    }

    public DashboardResponse summary() {
        double capacity = truckRepository.findAll().stream()
                .mapToDouble(Truck::getAvailableCapacity)
                .sum();
        return new DashboardResponse(
                truckRepository.count(),
                bookingRepository.count(),
                capacity,
                bookingRepository.countByBookingStatus(BookingStatus.DELIVERED)
        );
    }
}
