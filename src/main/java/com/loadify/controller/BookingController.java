package com.loadify.controller;

import com.loadify.dto.BookingRequest;
import com.loadify.dto.BookingResponse;
import com.loadify.service.BookingService;
import com.loadify.util.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin("*")
public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<BookingResponse>> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.success(201, "Booking created", bookingService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<BookingResponse>>> all() {
        return ResponseEntity.ok(ResponseStructure.success(200, "Bookings fetched", bookingService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<BookingResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Booking fetched", bookingService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<BookingResponse>> update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Booking updated", bookingService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.ok(ResponseStructure.success(200, "Booking deleted", "Deleted"));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ResponseStructure<List<BookingResponse>>> myBookings(@PathVariable Long customerId) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Customer bookings fetched", bookingService.myBookings(customerId)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ResponseStructure<List<BookingResponse>>> providerBookings(@PathVariable Long providerId) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Provider booking requests fetched", bookingService.providerBookings(providerId)));
    }
}
