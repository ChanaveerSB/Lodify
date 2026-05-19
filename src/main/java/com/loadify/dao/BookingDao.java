package com.loadify.dao;

import com.loadify.entity.Booking;
import com.loadify.exception.BookingNotFoundException;
import com.loadify.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingDao {
    private final BookingRepository bookingRepository;

    public BookingDao(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Booking save(Booking booking) { return bookingRepository.save(booking); }
    public List<Booking> findAll() { return bookingRepository.findAll(); }
    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + id));
    }
    public void delete(Booking booking) { bookingRepository.delete(booking); }
    public List<Booking> findByCustomer(Long customerId) { return bookingRepository.findByCustomerUserId(customerId); }
    public List<Booking> findByProvider(Long providerId) { return bookingRepository.findByTruckUploadedByUserId(providerId); }
}
