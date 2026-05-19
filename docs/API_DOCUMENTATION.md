# LOADIFY API Documentation

Base URL:

```text
http://localhost:9090
```

## Authentication

### Signup

`POST /auth/signup`

```json
{
  "fullName": "Arjun Transport Co",
  "email": "provider@loadify.com",
  "phone": "9876543210",
  "password": "secret123",
  "confirmPassword": "secret123",
  "role": "TRANSPORT_PROVIDER"
}
```

### Login

`POST /auth/login`

```json
{
  "email": "provider@loadify.com",
  "password": "secret123"
}
```

## Truck APIs

### Create Truck Return Trip

`POST /trucks`

The request uses original route fields. The service automatically stores the return route for booking.

```json
{
  "uploadedBy": 1,
  "truckNumber": "KA01AB4587",
  "truckType": "Container",
  "driverName": "Ravi Kumar",
  "driverPhone": "9988776655",
  "capacity": 18,
  "availableCapacity": 18,
  "source": "Bangalore",
  "destination": "Chennai",
  "departureDate": "2026-05-10",
  "returnDate": "2026-05-12",
  "estimatedArrivalTime": "2026-05-12T10:30:00",
  "pricePerTon": 4200,
  "description": "Clean container truck for packaged goods",
  "expectedDeliveryTime": "12 hours"
}
```

Stored searchable route:

```text
Chennai -> Bangalore on 2026-05-12
```

### Get All Trucks

`GET /trucks`

### Get Truck By ID

`GET /trucks/{id}`

### Update Truck

`PUT /trucks/{id}`

### Delete Truck

`DELETE /trucks/{id}`

### Search Trucks

`GET /trucks/search?source=Chennai&destination=Bangalore&date=2026-05-12&capacity=5&page=0&size=9&sortBy=pricePerTon&direction=asc`

Supported query parameters:

- `source`
- `destination`
- `date`
- `capacity`
- `truckType`
- `minRating`
- `maxPrice`
- `status`
- `page`
- `size`
- `sortBy`
- `direction`

## Booking APIs

### Create Booking

`POST /bookings`

```json
{
  "truckId": 1,
  "customerId": 2,
  "customerName": "Meera Foods",
  "customerPhone": "9123456780",
  "goodsType": "Packaged snacks",
  "weight": 4.5,
  "pickupLocation": "Chennai Warehouse",
  "dropLocation": "Bangalore Distribution Hub",
  "bookingDate": "2026-05-12",
  "requiredTrucks": 1,
  "bookingNotes": "Pickup after 10 AM"
}
```

Capacity is validated and `availableCapacity` is updated in real time.

### Get All Bookings

`GET /bookings`

### Get Booking By ID

`GET /bookings/{id}`

### Update Booking

`PUT /bookings/{id}`

### Delete Booking

`DELETE /bookings/{id}`

### Customer Bookings

`GET /bookings/customer/{customerId}`

### Provider Booking Requests

`GET /bookings/provider/{providerId}`

## Dashboard APIs

- `GET /dashboard/admin`
- `GET /dashboard/provider`
- `GET /dashboard/customer`

## Standard Response

```json
{
  "statusCode": 200,
  "message": "Search completed",
  "data": {},
  "timestamp": "2026-05-10T10:30:00"
}
```
