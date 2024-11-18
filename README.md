# Parallel HTTP Clients Example

This project demonstrates how to use multiple HTTP clients in a Spring Boot application to make parallel API requests. The application fetches data from the [Open Notify API](http://api.open-notify.org/astros.json), which provides information about astronauts currently in space.

## Features

- Uses five different HTTP clients:
    - **WebClient** (Spring WebFlux)
    - **RestTemplate** (Spring)
    - **HttpClient** (Java 11+)
    - **RestClient** (Spring 6+)
    - **OpenFeign** (Spring Cloud)
- Executes all requests in parallel using `CompletableFuture`.
- Returns a sorted list of results, showing the duration of each request.

## Technologies Used

- **Java 17**
- **Spring Boot 3**
- **Spring Web**
- **Spring WebFlux**
- **Spring Cloud OpenFeign**
- **Java 11 HttpClient**

## Getting Started

### Prerequisites

- Java 17 or later
- Maven
- Internet connection to access the [Open Notify API](http://api.open-notify.org/astros.json).

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   
2. Build the project:
   ```bash
   mvn clean install
   
3. Run the application:
   ```bash
   mvn spring-boot:run
   
## API Endpoint

- #### GET /execute-requests
- Fetches astronaut data from the API using all five clients.
- Returns a JSON array with the results sorted by request duration.

### Example Response

```json
[
    {
        "client": "WebClient",
        "duration": 450,
        "response": "{...}"
    },
    {
        "client": "HttpClient",
        "duration": 550,
        "response": "{...}"
    },
    ...
] 
```

## Project Structure
- `client`: Contains Feign client interface.
- `configuration`: Configuration classes for RestClient, RestTemplate, and WebClient.
- `controller`: REST controller to expose the API endpoint.
- `dto`: Data transfer objects (e.g., Result).
- `service`: Core business logic for executing parallel requests.
- `ParallelHttpClients1Application`: Main class for running the application.