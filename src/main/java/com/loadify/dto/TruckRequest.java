package com.loadify.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TruckRequest {
    @NotNull
    private Long uploadedBy;
    @NotBlank
    private String truckNumber;
    @NotBlank
    private String truckType;
    @NotBlank
    private String driverName;
    @Pattern(regexp = "^[0-9]{10}$", message = "Driver phone must contain 10 digits")
    private String driverPhone;
    @Positive
    private Double capacity;
    @PositiveOrZero
    private Double availableCapacity;
    @NotBlank
    private String source;
    @NotBlank
    private String destination;
    @NotNull
    private LocalDate departureDate;
    @NotNull
    private LocalDate returnDate;
    private LocalDateTime estimatedArrivalTime;
    @Positive
    private Double pricePerTon;
    private String description;
    private String expectedDeliveryTime;

    public Long getUploadedBy() { return uploadedBy; }
    public void setUploadedBy(Long uploadedBy) { this.uploadedBy = uploadedBy; }
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
    public String getExpectedDeliveryTime() { return expectedDeliveryTime; }
    public void setExpectedDeliveryTime(String expectedDeliveryTime) { this.expectedDeliveryTime = expectedDeliveryTime; }
}
