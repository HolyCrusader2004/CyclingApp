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
![pic4](https://github.com/user-attachments/assets/3cee6c47-15ac-4951-91d5-4a6e752a731d)

### Home Screen
![pic3](https://github.com/user-attachments/assets/3fbbd471-83b2-4971-8cb7-0b970f8ec886)

### Statistics Screen
![pic1](https://github.com/user-attachments/assets/17e088e6-6cdc-4c45-9e64-eea176faaced)

### Add Cycle Run Screen
![pic2](https://github.com/user-attachments/assets/1813394f-5c14-4c45-8fd1-ba42a6bf65d1)

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
