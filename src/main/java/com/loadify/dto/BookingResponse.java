package com.loadify.dto;

import com.loadify.enums.BookingStatus;
import java.time.LocalDate;

public class BookingResponse {
    private Long bookingId;
    private Long truckId;
    private String customerName;
    private String customerPhone;
    private String goodsType;
    private Double weight;
    private String pickupLocation;
    private String dropLocation;
    private LocalDate bookingDate;
    private Integer requiredTrucks;
    private BookingStatus bookingStatus;
    private Double totalPrice;

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }
    public Long getTruckId() { return truckId; }
    public void setTruckId(Long truckId) { this.truckId = truckId; }
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
    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }
    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }
}
