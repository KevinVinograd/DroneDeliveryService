# Example Drone Service API

### This is an example Kotlin project that implements a simple Drone Service API using Ktor and Koin.

## Getting started

To run the application, you need to have Kotlin and Gradle installed.

1. Clone the repository:

```
git clone https://github.com/your-username/drone-delivery-service.git
```

2. Build the project with Gradle.

```
./gradlew build
```

3. Create docker container with postgres database:
```
cd drone-delivery-service
./gradlew composeUp
```


4. Run the application.

```
./gradlew run
```

5. Stop docker container.

```
./gradlew composeDown
```


### Dependencies
- Ktor
- Koin
- Java 11
- Gradle 7.6
## Endpoints



### GET /drones/{serialNumber}/battery

Returns the battery level of the drone with the given serial number.

### Request
```kotlin
GET /drones/1234/battery HTTP/1.1
Host: localhost:8080
```

### Response
```kotlin
HTTP/1.1 200 OK
Content-Type: application/json
{
    60
}
```
---------

### GET /drones/available

Returns a list of all available drones.

### Request
```kotlin
GET /drones/available HTTP/1.1
Host: localhost:8080
```

### Response
```kotlin
HTTP/1.1 200 OK
Content-Type: application/json
[
    {
        "serialNumber": "4567-8901",
        "model": "Modelo 3",
        "weightLimit": 10000,
        "batteryCapacity": 80,
        "state": "IDLE"
    },
    {
        "serialNumber": "5678-9012",
        "model": "Modelo 1",
        "weightLimit": 5000,
        "batteryCapacity": 60,
        "state": "LOADED"
    },
    {
        "serialNumber": "6789-0123",
        "model": "Modelo 2",
        "weightLimit": 7500,
        "batteryCapacity": 50,
        "state": "DELIVERING"
    }
]
```
---------

### POST /drones

Create drone.

### Request
```kotlin
POST /drones
Host: localhost:8080
```

### Response
```kotlin
HTTP/1.1 200 OK
```
---------

### POST /drones/{serialNumber}/medications

Loads medications onto the drone with the given serial number.

### Request
```kotlin
POST /drones/1234/medications HTTP/1.1
Host: localhost:8080
```

### Response
```kotlin
HTTP/1.1 200 OK
```
---------

### GET /drones/{serialNumber}/medications

Returns a list of medications loaded onto the drone with the given serial number.

### Request
```kotlin
GET /drones/1234/medications HTTP/1.1
Host: localhost:8080
```

### Response
```kotlin
HTTP/1.1 200 OK
Content-Type: application/json
[
    {
        "name": "Medicación 4",
        "weight": 15,
        "code": "JKL-456",
        "image": "https://example.com/image4.png"
    },
    {
        "name": "Medicación 5",
        "weight": 25,
        "code": "MNO-789",
        "image": "https://example.com/image5.png"
    }
]
```
---------

