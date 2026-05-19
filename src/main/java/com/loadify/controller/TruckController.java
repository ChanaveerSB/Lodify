package com.loadify.controller;

import com.loadify.dto.TruckRequest;
import com.loadify.dto.TruckResponse;
import com.loadify.enums.TripStatus;
import com.loadify.service.TruckService;
import com.loadify.util.ResponseStructure;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/trucks")
@CrossOrigin("*")
public class TruckController {
    private final TruckService truckService;

    public TruckController(TruckService truckService) {
        this.truckService = truckService;
    }

    @PostMapping
    public ResponseEntity<ResponseStructure<TruckResponse>> create(@Valid @RequestBody TruckRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.success(201, "Return trip uploaded successfully", truckService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<TruckResponse>>> all() {
        return ResponseEntity.ok(ResponseStructure.success(200, "Trucks fetched", truckService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<TruckResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Truck fetched", truckService.findById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseStructure<TruckResponse>> update(@PathVariable Long id, @Valid @RequestBody TruckRequest request) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Truck updated", truckService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseStructure<String>> delete(@PathVariable Long id) {
        truckService.delete(id);
        return ResponseEntity.ok(ResponseStructure.success(200, "Truck deleted", "Deleted"));
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseStructure<Page<TruckResponse>>> search(
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Double capacity,
            @RequestParam(required = false) String truckType,
            @RequestParam(required = false) Double minRating,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) TripStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(defaultValue = "departureDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Search completed",
                truckService.search(source, destination, date, capacity, truckType, minRating, maxPrice, status, page, size, sortBy, direction)));
    }

    @GetMapping("/provider/{providerId}")
    public ResponseEntity<ResponseStructure<List<TruckResponse>>> myTrips(@PathVariable Long providerId) {
        return ResponseEntity.ok(ResponseStructure.success(200, "Provider trips fetched", truckService.myTrips(providerId)));
    }
}
