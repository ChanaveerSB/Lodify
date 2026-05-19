package com.loadify.entity;

import com.loadify.enums.RouteType;
import com.loadify.enums.TripStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trucks")
public class Truck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "truck_id")
    private Long truckId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by", nullable = false)
    private User uploadedBy;

    @Column(name = "truck_number", nullable = false, length = 20)
    private String truckNumber;
    @Column(name = "truck_type", nullable = false, length = 30)
    private String truckType;
    @Column(name = "driver_name", nullable = false, length = 50)
    private String driverName;
    @Column(name = "driver_phone", nullable = false, length = 15)
    private String driverPhone;
    private Double capacity;
    @Column(name = "available_capacity")
    private Double availableCapacity;
    private String source;
    private String destination;
    @Column(name = "departure_date")
    private LocalDate departureDate;
    @Column(name = "return_date")
    private LocalDate returnDate;
    @Column(name = "estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;
    @Column(name = "current_location")
    private String currentLocation;
    @Column(name = "price_per_ton")
    private Double pricePerTon;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(name = "image_url")
    private String imageUrl;
    private Double rating = 4.5;
    @Column(name = "total_reviews")
    private Integer totalReviews = 0;
    @Column(name = "insurance_available")
    private Boolean insuranceAvailable = true;
    @Enumerated(EnumType.STRING)
    @Column(name = "route_type")
    private RouteType routeType = RouteType.RETURN;
    @Enumerated(EnumType.STRING)
    private TripStatus status = TripStatus.AVAILABLE;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "truck")
    private List<Booking> bookings = new ArrayList<>();

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getTruckId() { return truckId; }
    public void setTruckId(Long truckId) { this.truckId = truckId; }
    public User getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(User uploadedBy) { this.uploadedBy = uploadedBy; }
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
    public String getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(String currentLocation) { this.currentLocation = currentLocation; }
    public Double getPricePerTon() { return pricePerTon; }
    public void setPricePerTon(Double pricePerTon) { this.pricePerTon = pricePerTon; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
    public Integer getTotalReviews() { return totalReviews; }
    public void setTotalReviews(Integer totalReviews) { this.totalReviews = totalReviews; }
    public Boolean getInsuranceAvailable() { return insuranceAvailable; }
    public void setInsuranceAvailable(Boolean insuranceAvailable) { this.insuranceAvailable = insuranceAvailable; }
    public RouteType getRouteType() { return routeType; }
    public void setRouteType(RouteType routeType) { this.routeType = routeType; }
    public TripStatus getStatus() { return status; }
    public void setStatus(TripStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
