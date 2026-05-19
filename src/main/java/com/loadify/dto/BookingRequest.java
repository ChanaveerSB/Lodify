package com.loadify.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class BookingRequest {
    @NotNull
    private Long truckId;
    @NotNull
    private Long customerId;
    @NotBlank
    private String customerName;
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must contain 10 digits")
    private String customerPhone;
    @NotBlank
    private String goodsType;
    @Positive
    private Double weight;
    @NotBlank
    private String pickupLocation;
    @NotBlank
    private String dropLocation;
    @NotNull
    private LocalDate bookingDate;
    @Positive
    private Integer requiredTrucks;
    private String bookingNotes;

    public Long getTruckId() { return truckId; }
    public void setTruckId(Long truckId) { this.truckId = truckId; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getGoodsType() { return goodsType; }
    public void setGoodsType(String goodsType) { this.goodsType = goodsType; }
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getDropLocation() { return dropLocation; }
    public void setDropLocation(String dropLocation) { this.dropLocation = dropLocation; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public Integer getRequiredTrucks() { return requiredTrucks; }
    public void setRequiredTrucks(Integer requiredTrucks) { this.requiredTrucks = requiredTrucks; }
    public String getBookingNotes() { return bookingNotes; }
    public void setBookingNotes(String bookingNotes) { this.bookingNotes = bookingNotes; }
}
