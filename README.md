# Running the Mobile Application

Follow these steps to set up and run the BlinkID Android mobile application (`BlinkID-Android-App`).

## Prerequisites

Before you begin, ensure you have the following installed on your machine:

- [Android Studio](https://developer.android.com/studio)
- [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)
- An Android device or emulator for testing

## Step-by-Step Guide

### 1. Clone the Repository

Open a terminal or command prompt and clone the repository using Git:

```bash
git clone https://github.com/your-username/BlinkID-Android-App.git
```

### 2. Open the Project in Android Studio

1. Open Android Studio.
2. Click on `Open an Existing Project`.
3. Navigate to the directory where you cloned the repository and select the `BlinkID-Android-App` folder.

### 3. Configure the Application

#### 3.1 Firebase Configuration

Ensure you have a Firebase project set up for the application. Follow these steps to configure Firebase:

1. Go to the [Firebase Console](https://console.firebase.google.com/).
2. Select your project or create a new one.
3. Add an Android app to your project:
    - Register your app with your application's package name (e.g., `com.example.blinkid`).
    - Download the `google-services.json` file provided by Firebase.
4. Place the `google-services.json` file in the `app` directory of your Android project.

#### 3.2 Update Dependencies

Ensure all dependencies are updated in the `build.gradle` files:

- `build.gradle (Project: BlinkID-Android-App)`:
  
  ```gradle
  buildscript {
      repositories {
          google()
          mavenCentral()
      }
      dependencies {
          classpath "com.android.tools.build:gradle:7.0.2"
          classpath 'com.google.gms:google-services:4.3.10'
      }
  }
  
  allprojects {
      repositories {
          google()
          mavenCentral()
      }
  }
  ```

- `build.gradle (Module: app)`:
  
  ```gradle
  apply plugin: 'com.android.application'
  apply plugin: 'com.google.gms.google-services'
  
  android {
      compileSdkVersion 31
      defaultConfig {
          applicationId "com.example.blinkid"
          minSdkVersion 21
          targetSdkVersion 31
          versionCode 1
          versionName "1.0"
          testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
      }
      buildTypes {
          release {
              minifyEnabled false
              proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
          }
      }
  }
  
  dependencies {
      implementation 'androidx.core:core-ktx:1.6.0'
      implementation 'androidx.appcompat:appcompat:1.3.1'
      implementation 'com.google.android.material:material:1.4.0'
      implementation 'androidx.constraintlayout:constraintlayout:2.1.0'
      implementation 'com.google.firebase:firebase-auth:21.0.1'
      implementation 'com.google.firebase:firebase-database:20.0.3'
      implementation 'com.google.firebase:firebase-storage:20.0.0'
      testImplementation 'junit:junit:4.13.2'
      androidTestImplementation 'androidx.test.ext:junit:1.1.3'
      androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
  }
  ```

### 4. Build the Project

1. Sync the project with Gradle files by clicking `Sync Now` in the notification bar or by going to `File > Sync Project with Gradle Files`.
2. Build the project by clicking on `Build > Make Project` or pressing `Ctrl+F9`.

### 5. Run the Application

1. Connect your Android device via USB or start an emulator in Android Studio.
2. Click on `Run > Run 'app'` or press `Shift+F10`.

### 6. Verify the Application

Once the application is installed on your device or emulator, verify that it is working correctly by going through the registration and authentication processes, including facial recognition and push notifications.

## Conclusion

You have successfully set up and run the BlinkID Android mobile application. For further customization or deployment, refer to the additional documentation and resources available in the repository.
