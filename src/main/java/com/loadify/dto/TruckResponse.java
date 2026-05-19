package com.loadify.dto;

import com.loadify.enums.TripStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TruckResponse {
    private Long truckId;
    private String providerName;
    private String truckNumber;
    private String truckType;
    private String driverName;
    private String driverPhone;
    private Double capacity;
    private Double availableCapacity;
    private String source;
    private String destination;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private LocalDateTime estimatedArrivalTime;
    private Double pricePerTon;
    private String description;
    private Double rating;
    private Integer totalReviews;
    private TripStatus status;

    public Long getTruckId() { return truckId; }
    public void setTruckId(Long truckId) { this.truckId = truckId; }
    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getTruckNumber() { return truckNumber; }
    public void setTruckNumber(String truckNumber) { this.truckNumber = truckNumber; }
    public String getTruckType() { return truckType; }
    public void setTruckType(String truckType) { this.truckType = truckType; }
    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }
    public String getDriverPhone() { return driverPhone; }
    public void setDriverPhone(String driverPhone) { this.driverPhone = driverPhone; }
    public Double getCapacity() { return capacity; }
    public void setCapacity(Double capacity) { this.capacity = capacity; }
    public Double getAvailableCapacity() { return availableCapacity; }
    public void setAvailableCapacity(Double availableCapacity) { this.availableCapacity = availableCapacity; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public LocalDate getDepartureDate() { return departureDate; }
    public void setDepartureDate(LocalDate departureDate) { this.departureDate = departureDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public LocalDateTime getEstimatedArrivalTime() { return estimatedArrivalTime; }
    public void setEstimatedArrivalTime(LocalDateTime estimatedArrivalTime) { this.estimatedArrivalTime = estimatedArrivalTime; }
    public Double getPricePerTon() { return pricePerTon; }
    public void setPricePerTon(Double pricePerTon) { this.pricePerTon = pricePerTon; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }
}
