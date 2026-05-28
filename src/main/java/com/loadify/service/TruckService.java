package com.loadify.service;

import com.loadify.dao.TruckDao;
import com.loadify.dao.UserDao;
import com.loadify.dto.TruckRequest;
import com.loadify.dto.TruckResponse;
import com.loadify.entity.Truck;
import com.loadify.entity.User;
import com.loadify.enums.RouteType;
import com.loadify.enums.TripStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TruckService {
    private final TruckDao truckDao;
    private final UserDao userDao;

    public TruckService(TruckDao truckDao, UserDao userDao) {
        this.truckDao = truckDao;
        this.userDao = userDao;
    }

    private TripStatus calculateStatus(Double capacity, Double available) {

        if (capacity == null || available == null) {
            return TripStatus.AVAILABLE;
        }

        if (available <= 0) {
            return TripStatus.FULLY_BOOKED;
        }

        if (available.equals(capacity)) {
            return TripStatus.AVAILABLE;
        }

        return TripStatus.PARTIALLY_BOOKED;
    }

    public TruckResponse create(TruckRequest request) {

        User provider = userDao.findById(request.getUploadedBy());

        Truck truck = new Truck();

        truck.setUploadedBy(provider);
        truck.setTruckNumber(request.getTruckNumber());
        truck.setTruckType(request.getTruckType());
        truck.setDriverName(request.getDriverName());
        truck.setDriverPhone(request.getDriverPhone());

        truck.setCapacity(request.getCapacity());

        // fallback logic
        truck.setAvailableCapacity(
                request.getAvailableCapacity() == null
                        ? request.getCapacity()
                        : request.getAvailableCapacity());

        /*
         * ================================
         * RETURN LOAD OPTIMIZATION SYSTEM
         * ================================
         * Frontend sends forward trip (A → B)
         * Backend stores reverse trip (B → A)
         * This enables return-load marketplace.
         */

        truck.setSource(request.getDestination());
        truck.setDestination(request.getSource());

        truck.setDepartureDate(request.getReturnDate());
        truck.setReturnDate(request.getDepartureDate());

        truck.setEstimatedArrivalTime(request.getEstimatedArrivalTime());

        truck.setCurrentLocation(request.getDestination());

        truck.setPricePerTon(request.getPricePerTon());
        truck.setDescription(request.getDescription());

        // DEFAULT MODE (for now)
        truck.setRouteType(RouteType.RETURN);

        truck.setStatus(
                calculateStatus(
                        truck.getCapacity(),
                        truck.getAvailableCapacity()));

        /*
         * ===========================================
         * FUTURE SCOPE (ONE-WAY SUPPORT - NOT ACTIVE)
         * ===========================================
         * 
         * // if (request.getRouteType() != null) {
         * // truck.setRouteType(request.getRouteType());
         * //
         * // if (request.getRouteType() == RouteType.ONE_WAY) {
         * // truck.setSource(request.getSource());
         * // truck.setDestination(request.getDestination());
         * // truck.setDepartureDate(request.getDepartureDate());
         * // truck.setReturnDate(request.getReturnDate());
         * // }
         * // }
         */

        return toResponse(truckDao.save(truck));
    }

    public List<TruckResponse> findAll() {
        return truckDao.findAll().stream().map(this::toResponse).toList();
    }

    public TruckResponse findById(Long id) {
        return toResponse(truckDao.findById(id));
    }

    public TruckResponse update(Long id, TruckRequest request) {

        Truck truck = truckDao.findById(id);

        // -------------------------------
        // BASIC DETAILS UPDATE (SAFE NULL CHECKS)
        // -------------------------------
        if (request.getTruckNumber() != null)
            truck.setTruckNumber(request.getTruckNumber());

        if (request.getTruckType() != null)
            truck.setTruckType(request.getTruckType());

        if (request.getDriverName() != null)
            truck.setDriverName(request.getDriverName());

        if (request.getDriverPhone() != null)
            truck.setDriverPhone(request.getDriverPhone());

        // -------------------------------
        // CAPACITY LOGIC (SAFE UPDATE)
        // -------------------------------
        if (request.getCapacity() != null)
            truck.setCapacity(request.getCapacity());

        if (request.getAvailableCapacity() != null)
            truck.setAvailableCapacity(request.getAvailableCapacity());

        // -------------------------------
        // ROUTE DETAILS (KEEP YOUR CURRENT FLOW)
        // -------------------------------
        if (request.getSource() != null)
            truck.setSource(request.getSource());

        if (request.getDestination() != null)
            truck.setDestination(request.getDestination());

        if (request.getDepartureDate() != null)
            truck.setDepartureDate(request.getDepartureDate());

        if (request.getReturnDate() != null)
            truck.setReturnDate(request.getReturnDate());

        if (request.getEstimatedArrivalTime() != null)
            truck.setEstimatedArrivalTime(request.getEstimatedArrivalTime());

        if (request.getPricePerTon() != null)
            truck.setPricePerTon(request.getPricePerTon());

        if (request.getDescription() != null)
            truck.setDescription(request.getDescription());

        // -------------------------------
        // STATUS AUTO-RECALCULATION (IMPORTANT FIX)
        // -------------------------------
        truck.setStatus(
                calculateStatus(
                        truck.getCapacity(),
                        truck.getAvailableCapacity()));

        // -------------------------------
        // SAVE + RETURN
        // -------------------------------
        return toResponse(truckDao.save(truck));
    }

    // public TruckResponse update(Long id, TruckRequest request) {
    // Truck truck = truckDao.findById(id);
    // truck.setTruckNumber(request.getTruckNumber());
    // truck.setTruckType(request.getTruckType());
    // truck.setDriverName(request.getDriverName());
    // truck.setDriverPhone(request.getDriverPhone());
    // truck.setCapacity(request.getCapacity());
    // truck.setAvailableCapacity(request.getAvailableCapacity());
    // truck.setSource(request.getSource());
    // truck.setDestination(request.getDestination());
    // truck.setDepartureDate(request.getDepartureDate());
    // truck.setReturnDate(request.getReturnDate());
    // truck.setEstimatedArrivalTime(request.getEstimatedArrivalTime());
    // truck.setPricePerTon(request.getPricePerTon());
    // truck.setDescription(request.getDescription());
    // return toResponse(truckDao.save(truck));
    // }

    public void delete(Long id) {
        truckDao.delete(truckDao.findById(id));
    }

    public Page<TruckResponse> search(
            String source,
            String destination,
            LocalDate date,
            Double capacity,
            String truckType,
            Double minRating,
            Double maxPrice,
            TripStatus status,
            int page,
            int size,
            String sortBy,
            String direction) {

        // -------------------------------
        // SORTING LOGIC (ASC / DESC)
        // -------------------------------
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        // -------------------------------
        // PAGINATION SETUP
        // page = current page number (0-based)
        // size = number of records per page
        // -------------------------------
        Pageable pageable = PageRequest.of(page, size, sort);

        // -------------------------------
        // DYNAMIC SEARCH BUILDER (SPECIFICATION)
        // Used to build SQL WHERE conditions dynamically
        // based on available filters from frontend
        // -------------------------------
        Specification<Truck> specification = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // -------------------------------
            // FILTER: SOURCE (LIKE search, case-insensitive)
            // -------------------------------
            if (source != null && !source.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("source")),
                        "%" + source.toLowerCase() + "%"));
            }

            // -------------------------------
            // FILTER: DESTINATION (LIKE search)
            // -------------------------------
            if (destination != null && !destination.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("destination")),
                        "%" + destination.toLowerCase() + "%"));
            }

            // -------------------------------
            // FILTER: DEPARTURE DATE (exact match)
            // -------------------------------
            if (date != null) {
                predicates.add(cb.equal(root.get("departureDate"), date));
            }

            // -------------------------------
            // FILTER: CAPACITY CHECK
            // Only show trucks that can handle required load
            // -------------------------------
            if (capacity != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("availableCapacity"),
                        capacity));
            }

            // -------------------------------
            // FILTER: TRUCK TYPE (LIKE search)
            // -------------------------------
            if (truckType != null && !truckType.isBlank()) {
                predicates.add(cb.like(
                        cb.lower(root.get("truckType")),
                        "%" + truckType.toLowerCase() + "%"));
            }

            // -------------------------------
            // FILTER: MINIMUM RATING
            // -------------------------------
            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(
                        root.get("rating"),
                        minRating));
            }

            // -------------------------------
            // FILTER: MAX PRICE PER TON
            // -------------------------------
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(
                        root.get("pricePerTon"),
                        maxPrice));
            }

            // -------------------------------
            // BUSINESS RULE:
            // Never show completed trips in search results
            // -------------------------------
            predicates.add(cb.notEqual(
                    root.get("status"),
                    TripStatus.COMPLETED));

            // -------------------------------
            // OPTIONAL FILTER: STATUS
            // If user explicitly wants a status filter
            // -------------------------------
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Combine all filters using AND condition
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // -------------------------------
        // EXECUTE QUERY + MAP ENTITY → DTO
        // -------------------------------
        return truckDao.search(specification, pageable)
                .map(this::toResponse);
    }

    public List<TruckResponse> myTrips(Long providerId) {
        return truckDao.findByProvider(providerId).stream().map(this::toResponse).toList();
    }

    private TruckResponse toResponse(Truck truck) {

    TruckResponse response = new TruckResponse();

    response.setTruckId(truck.getTruckId());

    // SAFE NULL CHECK (prevents NullPointerException)
    response.setProviderName(
            truck.getUploadedBy() != null
                    ? truck.getUploadedBy().getFullName()
                    : null
    );

    response.setTruckNumber(truck.getTruckNumber());
    response.setTruckType(truck.getTruckType());
    response.setDriverName(truck.getDriverName());
    response.setDriverPhone(truck.getDriverPhone());

    response.setCapacity(truck.getCapacity());
    response.setAvailableCapacity(truck.getAvailableCapacity());

    response.setSource(truck.getSource());
    response.setDestination(truck.getDestination());

    response.setDepartureDate(truck.getDepartureDate());
    response.setReturnDate(truck.getReturnDate());
    response.setEstimatedArrivalTime(truck.getEstimatedArrivalTime());

    response.setPricePerTon(truck.getPricePerTon());
    response.setDescription(truck.getDescription());

    response.setRating(truck.getRating());
    response.setTotalReviews(truck.getTotalReviews());

    // STATUS SAFE CONVERSION
    response.setStatus(
            truck.getStatus() != null
                    ? truck.getStatus()
                    : TripStatus.AVAILABLE
    );

    return response;
}
    // private TruckResponse toResponse(Truck truck) {
    //     TruckResponse response = new TruckResponse();
    //     response.setTruckId(truck.getTruckId());
    //     response.setProviderName(truck.getUploadedBy().getFullName());
    //     response.setTruckNumber(truck.getTruckNumber());
    //     response.setTruckType(truck.getTruckType());
    //     response.setDriverName(truck.getDriverName());
    //     response.setDriverPhone(truck.getDriverPhone());
    //     response.setCapacity(truck.getCapacity());
    //     response.setAvailableCapacity(truck.getAvailableCapacity());
    //     response.setSource(truck.getSource());
    //     response.setDestination(truck.getDestination());
    //     response.setDepartureDate(truck.getDepartureDate());
    //     response.setReturnDate(truck.getReturnDate());
    //     response.setEstimatedArrivalTime(truck.getEstimatedArrivalTime());
    //     response.setPricePerTon(truck.getPricePerTon());
    //     response.setDescription(truck.getDescription());
    //     response.setRating(truck.getRating());
    //     response.setTotalReviews(truck.getTotalReviews());
    //     response.setStatus(truck.getStatus());
    //     return response;
    // }
}
