# Sample JSON Responses

## Signup Response

```json
{
  "statusCode": 201,
  "message": "Signup successful",
  "data": {
    "userId": 1,
    "fullName": "Arjun Transport Co",
    "email": "provider@loadify.com",
    "phone": "9876543210",
    "role": "TRANSPORT_PROVIDER"
  },
  "timestamp": "2026-05-10T10:30:00"
}
```

## Truck Search Response

```json
{
  "statusCode": 200,
  "message": "Search completed",
  "data": {
    "content": [
      {
        "truckId": 1,
        "providerName": "Arjun Transport Co",
        "truckNumber": "KA01AB4587",
        "truckType": "Container",
        "capacity": 18,
        "availableCapacity": 18,
        "source": "Chennai",
        "destination": "Bangalore",
        "departureDate": "2026-05-12",
        "pricePerTon": 4200,
        "rating": 4.8,
        "totalReviews": 42,
        "status": "AVAILABLE"
      }
    ],
    "pageable": {},
    "totalElements": 1,
    "totalPages": 1
  },
  "timestamp": "2026-05-10T10:30:00"
}
```

## Booking Response

```json
{
  "statusCode": 201,
  "message": "Booking created",
  "data": {
    "bookingId": 1,
    "truckId": 1,
    "customerName": "Meera Foods",
    "customerPhone": "9123456780",
    "goodsType": "Packaged snacks",
    "weight": 4.5,
    "pickupLocation": "Chennai Warehouse",
    "dropLocation": "Bangalore Distribution Hub",
    "bookingDate": "2026-05-12",
    "requiredTrucks": 1,
    "bookingStatus": "PENDING",
    "totalPrice": 18900
  },
  "timestamp": "2026-05-10T10:30:00"
}
```
