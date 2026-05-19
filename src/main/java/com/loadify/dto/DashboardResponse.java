package com.loadify.dto;

public class DashboardResponse {
    private long totalTrips;
    private long activeBookings;
    private double availableCapacity;
    private long deliveredOrders;

    public DashboardResponse(long totalTrips, long activeBookings, double availableCapacity, long deliveredOrders) {
        this.totalTrips = totalTrips;
        this.activeBookings = activeBookings;
        this.availableCapacity = availableCapacity;
        this.deliveredOrders = deliveredOrders;
    }

    public long getTotalTrips() { return totalTrips; }
    public long getActiveBookings() { return activeBookings; }
    public double getAvailableCapacity() { return availableCapacity; }
    public long getDeliveredOrders() { return deliveredOrders; }
}
