package com.loadify.dao;

import com.loadify.entity.Truck;
import com.loadify.exception.TruckNotFoundException;
import com.loadify.repository.TruckRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TruckDao {
    private final TruckRepository truckRepository;

    public TruckDao(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    public Truck save(Truck truck) { return truckRepository.save(truck); }
    public List<Truck> findAll() { return truckRepository.findAll(); }
    public Truck findById(Long id) {
        return truckRepository.findById(id)
                .orElseThrow(() -> new TruckNotFoundException("Truck not found with id: " + id));
    }
    public void delete(Truck truck) { truckRepository.delete(truck); }
    public Page<Truck> search(Specification<Truck> specification, Pageable pageable) {
        return truckRepository.findAll(specification, pageable);
    }
    public List<Truck> findByProvider(Long providerId) {
        return truckRepository.findByUploadedByUserId(providerId);
    }
}
