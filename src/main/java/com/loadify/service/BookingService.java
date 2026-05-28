package com.loadify.service;

import com.loadify.dao.BookingDao;
import com.loadify.dao.TruckDao;
import com.loadify.dao.UserDao;
import com.loadify.dto.BookingRequest;
import com.loadify.dto.BookingResponse;
import com.loadify.entity.Booking;
import com.loadify.entity.Truck;
import com.loadify.entity.User;
import com.loadify.enums.BookingStatus;
import com.loadify.enums.TripStatus;
import com.loadify.exception.InvalidCapacityException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingService {

    private final BookingDao bookingDao;
    private final TruckDao truckDao;
    private final UserDao userDao;

    public BookingService(BookingDao bookingDao, TruckDao truckDao, UserDao userDao) {
        this.bookingDao = bookingDao;
        this.truckDao = truckDao;
        this.userDao = userDao;
    }

    // =========================
    // CREATE BOOKING
    // =========================
    @Transactional
    public BookingResponse create(BookingRequest request) {

        Truck truck = truckDao.findById(request.getTruckId());
        User customer = userDao.findById(request.getCustomerId());

        // Prevent overbooking
        if (request.getWeight() > truck.getAvailableCapacity()) {
            throw new InvalidCapacityException("Requested weight exceeds available capacity");
        }

        Booking booking = new Booking();

        booking.setTruck(truck);
        booking.setCustomer(customer);
        booking.setCustomerName(request.getCustomerName());
        booking.setCustomerPhone(request.getCustomerPhone());
        booking.setGoodsType(request.getGoodsType());
        booking.setWeight(request.getWeight());
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDropLocation(request.getDropLocation());
        booking.setBookingDate(request.getBookingDate());
        booking.setRequiredTrucks(request.getRequiredTrucks());
        booking.setBookingNotes(request.getBookingNotes());

        // Default booking state
        booking.setBookingStatus(BookingStatus.PENDING);

        booking.setTotalPrice(request.getWeight() * truck.getPricePerTon());

        // =========================
        // CAPACITY UPDATE (SOURCE OF TRUTH)
        // =========================
        double newCapacity = truck.getAvailableCapacity() - request.getWeight();
        truck.setAvailableCapacity(newCapacity);

        // =========================
        // TRUCK STATUS UPDATE (ONLY HERE)
        // =========================
        updateTruckStatus(truck);

        truckDao.save(truck);

        return toResponse(bookingDao.save(booking));
    }

    // =========================
    // CONFIRM BOOKING
    // =========================
    @Transactional
    public BookingResponse confirm(Long bookingId) {

        Booking booking = bookingDao.findById(bookingId);

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new RuntimeException("Booking already confirmed");
        }

        booking.setBookingStatus(BookingStatus.CONFIRMED);

        return toResponse(bookingDao.save(booking));
    }

    // =========================
    // CANCEL BOOKING
    // =========================
    @Transactional
    public BookingResponse cancel(Long bookingId) {

        Booking booking = bookingDao.findById(bookingId);

        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking already cancelled");
        }

        Truck truck = booking.getTruck();

        // Restore capacity safely
        truck.setAvailableCapacity(
                truck.getAvailableCapacity() + booking.getWeight());

        // Recalculate status
        updateTruckStatus(truck);

        truckDao.save(truck);

        booking.setBookingStatus(BookingStatus.CANCELLED);

        return toResponse(bookingDao.save(booking));
    }

    // =========================
    // UPDATE TRUCK STATUS (SINGLE SOURCE OF TRUTH)
    // =========================
    private void updateTruckStatus(Truck truck) {

        double cap = truck.getCapacity();
        double avail = truck.getAvailableCapacity();

        if (avail <= 0) {
            truck.setStatus(TripStatus.FULLY_BOOKED);
        } else if (avail == cap) {
            truck.setStatus(TripStatus.AVAILABLE);
        } else {
            truck.setStatus(TripStatus.PARTIALLY_BOOKED);
        }
    }

    public BookingResponse update(Long id, BookingRequest request) {

        Booking booking = bookingDao.findById(id);

        booking.setCustomerName(request.getCustomerName());
        booking.setCustomerPhone(request.getCustomerPhone());
        booking.setGoodsType(request.getGoodsType());
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDropLocation(request.getDropLocation());
        booking.setBookingDate(request.getBookingDate());
        booking.setRequiredTrucks(request.getRequiredTrucks());
        booking.setBookingNotes(request.getBookingNotes());

        return toResponse(bookingDao.save(booking));
    }

    @Transactional
    public void delete(Long id) {

        Booking booking = bookingDao.findById(id);
        Truck truck = booking.getTruck();

        // Restore capacity ONLY if booking was active
        if (booking.getBookingStatus() != BookingStatus.CANCELLED) {

            truck.setAvailableCapacity(
                    truck.getAvailableCapacity() + booking.getWeight());

            updateTruckStatus(truck);
            truckDao.save(truck);
        }

        bookingDao.delete(booking);
    }

    // =========================
    // READ OPERATIONS
    // =========================
    public List<BookingResponse> findAll() {
        return bookingDao.findAll().stream().map(this::toResponse).toList();
    }

    public BookingResponse findById(Long id) {
        return toResponse(bookingDao.findById(id));
    }

    public List<BookingResponse> myBookings(Long customerId) {
        return bookingDao.findByCustomer(customerId).stream().map(this::toResponse).toList();
    }

    public List<BookingResponse> providerBookings(Long providerId) {
        return bookingDao.findByProvider(providerId).stream().map(this::toResponse).toList();
    }

    // =========================
    // DTO MAPPER
    // =========================
    private BookingResponse toResponse(Booking booking) {

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getBookingId());

        response.setTruckId(
                booking.getTruck() != null ? booking.getTruck().getTruckId() : null);

        response.setCustomerName(booking.getCustomerName());
        response.setCustomerPhone(booking.getCustomerPhone());
        response.setGoodsType(booking.getGoodsType());
        response.setWeight(booking.getWeight());
        response.setPickupLocation(booking.getPickupLocation());
        response.setDropLocation(booking.getDropLocation());
        response.setBookingDate(booking.getBookingDate());
        response.setRequiredTrucks(booking.getRequiredTrucks());
        response.setBookingStatus(booking.getBookingStatus());
        response.setTotalPrice(booking.getTotalPrice());

        return response;
    }
}