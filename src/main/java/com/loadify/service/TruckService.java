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

    public TruckResponse create(TruckRequest request) {
        User provider = userDao.findById(request.getUploadedBy());
        Truck truck = new Truck();
        truck.setUploadedBy(provider);
        truck.setTruckNumber(request.getTruckNumber());
        truck.setTruckType(request.getTruckType());
        truck.setDriverName(request.getDriverName());
        truck.setDriverPhone(request.getDriverPhone());
        truck.setCapacity(request.getCapacity());
        truck.setAvailableCapacity(request.getAvailableCapacity() == null ? request.getCapacity() : request.getAvailableCapacity());
        truck.setSource(request.getDestination());
        truck.setDestination(request.getSource());
        truck.setDepartureDate(request.getReturnDate());
        truck.setReturnDate(request.getDepartureDate());
        truck.setEstimatedArrivalTime(request.getEstimatedArrivalTime());
        truck.setCurrentLocation(request.getDestination());
        truck.setPricePerTon(request.getPricePerTon());
        truck.setDescription(request.getDescription());
        truck.setRouteType(RouteType.RETURN);
        truck.setStatus(TripStatus.AVAILABLE);
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
        truck.setTruckNumber(request.getTruckNumber());
        truck.setTruckType(request.getTruckType());
        truck.setDriverName(request.getDriverName());
        truck.setDriverPhone(request.getDriverPhone());
        truck.setCapacity(request.getCapacity());
        truck.setAvailableCapacity(request.getAvailableCapacity());
        truck.setSource(request.getSource());
        truck.setDestination(request.getDestination());
        truck.setDepartureDate(request.getDepartureDate());
        truck.setReturnDate(request.getReturnDate());
        truck.setEstimatedArrivalTime(request.getEstimatedArrivalTime());
        truck.setPricePerTon(request.getPricePerTon());
        truck.setDescription(request.getDescription());
        return toResponse(truckDao.save(truck));
    }

    public void delete(Long id) {
        truckDao.delete(truckDao.findById(id));
    }

    public Page<TruckResponse> search(String source, String destination, LocalDate date, Double capacity,
                                      String truckType, Double minRating, Double maxPrice, TripStatus status,
                                      int page, int size, String sortBy, String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Truck> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (source != null && !source.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("source")), "%" + source.toLowerCase() + "%"));
            }
            if (destination != null && !destination.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("destination")), "%" + destination.toLowerCase() + "%"));
            }
            if (date != null) {
                predicates.add(cb.equal(root.get("departureDate"), date));
            }
            if (capacity != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("availableCapacity"), capacity));
            }
            if (truckType != null && !truckType.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("truckType")), "%" + truckType.toLowerCase() + "%"));
            }
            if (minRating != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("rating"), minRating));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("pricePerTon"), maxPrice));
            }
            predicates.add(cb.notEqual(root.get("status"), TripStatus.COMPLETED));
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return truckDao.search(specification, pageable).map(this::toResponse);
    }

    public List<TruckResponse> myTrips(Long providerId) {
        return truckDao.findByProvider(providerId).stream().map(this::toResponse).toList();
    }

    private TruckResponse toResponse(Truck truck) {
        TruckResponse response = new TruckResponse();
        response.setTruckId(truck.getTruckId());
        response.setProviderName(truck.getUploadedBy().getFullName());
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
        response.setStatus(truck.getStatus());
        return response;
    }
}
