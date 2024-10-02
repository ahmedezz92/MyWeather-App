# Weather Forecast App

A simple Android application built with Jetpack Compose displays the current weather for a searched city and navigates to a 5-day forecast screen showing the results in a list. 
The application is built using modern Android development practices, including Kotlin coroutines, Retrofit for network requests, Jetpack compose for UI desiging, Room for local caching, and the MVVM architectural pattern with Hilt for dependency injection.

## Features

- **Search for City**: Users can input a city name to retrieve current weather conditions.
- **Current Weather Display**: Shows the current weather for the searched city, including temperature, humidity, wind speed, and weather conditions.
- **5-Day Forecast**: On selecting a city, users are navigated to a detailed 5-day weather forecast screen.
- **Data Caching**: Weather data is cached in a Room database for 30 minutes to reduce network calls and improve performance.

## Technologies Used

- **Kotlin**: The programming language used for developing the application.
- **Coroutines**: Used for asynchronous programming to handle network requests and data processing without blocking the main thread.
- **Retrofit**: A type-safe HTTP client for Android and Java to make network calls to a weather API.
- **Room**: A persistence library that provides an abstraction layer over SQLite for local database management and caching.
- **Hilt**: A dependency injection library for Android that simplifies the process of managing dependencies.
- **MVVM**: The Model-View-ViewModel architectural pattern is used to separate UI data from the business logic.
- **Unit Testing**: 
  - **MockK**: For mocking dependencies in unit tests.
  - **Turbine**: For testing flows in coroutines.
  - **Hilt Testing**: For testing Hilt components and providing test dependencies.
  - **Coroutine Testing**: For testing coroutines to ensure they are functioning correctly.

## Getting Started

### Prerequisites

- Android Studio (latest version)
- Kotlin (latest version)

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/ahmedezz92/MyWeather-App
   cd MyWeather-App
