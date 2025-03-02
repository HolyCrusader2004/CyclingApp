# Cycling App

A Cycling App built with Jetpack Compose in Kotlin, utilizing Google Maps API for real-time location tracking and route visualization. The app allows users to save their cycling routes and view performance statistics. It follows the MVVM architecture and uses Room Database for data storage.

## Features
- **Real-Time Tracking**: Uses Google Maps API to track and display the user’s current position and cycling route in real time.
- **Route Visualization**: Displays the complete cycling route on an interactive map.
- **Ride History**: Stores completed rides in a local Room Database.
- **Performance Statistics**: Shows detailed statistics on saved rides.
- **Modern UI**: Built with Jetpack Compose for a smooth and intuitive experience.

## Technologies Used
- **Kotlin**
- **Jetpack Compose**
- **Google Maps API**
- **Room Database**
- **MVVM Architecture**

## Screenshots
![Screenshot 2025-03-02 132203](https://github.com/user-attachments/assets/67f4725b-7af4-461c-94a6-fec926c9c140)

### Home Screen
![Screenshot 2025-03-02 132305](https://github.com/user-attachments/assets/c7460933-bb1a-4729-8a24-73c9113381e7)


### Statistics Screen
![Screenshot 2025-03-02 132837](https://github.com/user-attachments/assets/f950bbfe-2f5b-4b4e-818f-40dce980a5c7)


### Add Cycle Run Screen
![Screenshot 2025-03-02 132435](https://github.com/user-attachments/assets/331869cd-a51e-4b54-8187-eaba559dbc38)


## Installation
1. Clone the repository:
   ```sh
   git clone https://github.com/HolyCrusader2004/cycling-app.git
   cd cycling-app
   ```
2. Open the project in Android Studio.
3. Configure Google Maps API:
   - Go to the [Google Cloud Console](https://console.cloud.google.com/).
   - Enable the Maps SDK for Android.
   - Generate an API key and add it to your `local.properties` file.
4. Build and run the app on an emulator or physical device.
