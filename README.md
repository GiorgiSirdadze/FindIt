# 📱 FindIt - Lost & Found App

**FindIt** is a modern, feature-packed Android app designed to help users find and report lost items using a clean, efficient, and intuitive interface. This app leverages **Firebase Authentication**, **Firestore**, **Firebase Storage**, and follows the **Clean Architecture** and **MVI** pattern to ensure scalability, maintainability, and smooth performance.

---

## 🚀 Features

- 🔐 **Authentication**: Sign up and log in using Firebase Authentication.
- 📝 **Post Creation**: Users can create posts for lost and found items, including adding photos, descriptions, and locations.
- 💬 **Chat**: Secure messaging between users for item retrieval or reporting.
- 📍 **Location Services**: Mark locations on the map for found items or lost items.
- 📸 **Image Upload**: Upload photos for posts with Firebase Storage.
- 🧠 **MVI Pattern**: The app follows the Model-View-Intent (MVI) architecture for clear separation of concerns and better state management.

---

## 🏗️ Architecture

This app is built with the **Clean Architecture** and **MVI pattern**, ensuring that each layer is independent and can be tested individually.

### 📚 Layers

1. **Data Layer**: Handles interaction with Firebase (Firestore, Firebase Authentication, and Firebase Storage).
2. **Domain Layer**: Contains the business logic and use cases.
3. **Presentation Layer**: Manages the UI with MVI, keeping the UI responsive and maintaining a clean state.

---

## 🧰 Tech Stack

- **Firebase**:
    - 🔐 Authentication
    - 🔥 Firestore (Database)
    - ☁️ Firebase Storage
- 🧩 **Dagger Hilt**: Dependency injection framework for Android to simplify the management of dependencies.
- 🧑‍💻 **Kotlin**: The main programming language used.
- ⏱️ **Coroutines**: For managing background tasks.
- 🧭 **Jetpack Navigation**: Used for navigating between fragments and activities.
- 🗂️ **RecyclerView**: For displaying lists in a smooth, scrollable manner.

---

## 🛠️ Getting Started

### ✅ Prerequisites

- Android Studio (latest version recommended)
- Firebase Project setup for Authentication, Firestore, and Firebase Storage
- Basic knowledge of Kotlin and Android development

### 📦 Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/yourusername/findit.git
    cd findit
    ```

2. **Add Firebase credentials**:
    - Go to [Firebase Console](https://console.firebase.google.com/), create a new project, and add your Android app.
    - Download the `google-services.json` and place it in the `app/` directory.

3. **Enable Firebase services**:
    - Authentication: Enable Email/Password authentication in Firebase Authentication.
    - Firestore: Enable Firestore in your Firebase project.
    - Firebase Storage: Enable Firebase Storage for image uploads.

4. **Install dependencies**:  
   Make sure to sync your project with Gradle to install all dependencies, including Dagger Hilt, Firebase SDKs, and others.

5. **Run the app**:  
   You should now be able to run the app on an emulator or a real device.

---

## 🧱 Code Structure

- **Base classes**: The app uses base fragments for consistent UI design and behavior across screens. For example, `BaseFragment` for general fragment logic, and `BaseBottomSheetFragment` for BottomSheet dialogs.

- **Fragments**: The app's core UI components are based on fragments, each corresponding to a specific feature.

- **ViewModels**: Every screen in the app is paired with a ViewModel, following the MVI pattern, ensuring clear separation of concerns.

- **Use Cases**: Each business operation (like creating a post, fetching chats, etc.) is encapsulated in a use case, ensuring the app remains scalable.

---

### 🐛 Issues and Feature Requests

If you find any bugs or want to request new features, please open an issue.

---

## 🌱 Future Improvements

- 🔔 **Push Notifications**: Add real-time notifications for new messages or posts to enhance user engagement.
- 🗺️ **Map Integration**: Enhance the map functionality with better location markers and geofencing for item locations.

## 👥 Team Members

- **Giorgi Sirdadze** 
- **Sergi Arabidze** 

---

## 🔁 Repo Note

The original repository was hosted on [sergiarabidze/FindIt](https://github.com/sergiarabidze/FindIt).  
I created this version to showcase my own contributions and improvements. All code pushed here was written or co-developed by me.

---

