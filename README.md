<img src="https://github.com/user-attachments/assets/77860500-927c-4ae5-ba62-d5eac8cbdb7f" width="20%"  alt="ModernADB Logo"/>

# Modern ADB | A modern GUI ADB application for desktop

**Unlock the full potential of Android Debug Bridge (ADB) with this powerful tool!**

This project provides a comprehensive and user-friendly interface to streamline your Android development and debugging workflow.

<img src="https://github.com/user-attachments/assets/31cccda8-2783-4522-9e28-a28300f3b1b9" width="50%" alt="ModernADB Screenshot">

## ✨ Key Features

- **Cross-Platform:** Single codebase runs natively on Windows, macOS, and Linux.
- **Intuitive Controls:** Simple buttons for common navigation (Home, Back, Recent), swipes, and power controls.
- **Device Management:** View a list of connected devices and select a target for your commands.
- **Text & Command Input:** A dedicated input field to send text or custom ADB shell commands directly to your device.
- **Adaptive UI:** The layout adjusts gracefully for different window sizes.

Planned:
*   **Device Management:** Easily manage multiple connected devices and emulators. [planned]
*   **File System Navigation:** Browse and manage files on your Android device with ease. [planned]
*   **Logcat Viewer:** Advanced logcat filtering and viewing capabilities. [planned]
*   **Screenshot & Screen Recording:** Capture screenshots and record screen activity effortlessly. [planned]

## 🚀 Getting Started

### Prerequisites

You need to have [ADB Tools](https://developer.android.com/tools/releases/platform-tools) ADB Tools installed and configured in your system's PATH for the app to function correctly. Ensure your Android device has USB Debugging enabled in Developer Options.

### Installation

1. Go to the [Releases](https://github.com/laetuz/ModernADB/releases) page of this repository.
2. Download the latest package for your operating system (.dmg for macOS, .msi for Windows, or .deb for Linux).
3. Install the application as you would any other desktop app.

## 🔧 How to Use

1. **Connect Your Device:** Connect your Android device to your computer via USB.
2. **Authorize Connection:** On your device, you may see a prompt asking you to "Allow USB debugging". Check "Always allow from this computer" and tap Allow.
3. **Launch ModernADB:** Open the application.
4. **Select Device:** Tap the device list on the top right to use your target device. (It is automatically selecting the first on the list on start).
5. **Use Controls:**
   - Click buttons like Home, Back, or Swipe Up to send commands.
   - Use the Input Field to type text to send or to enter custom commands.
   - Toggle between "Write" mode (to send text) and "Command" mode (to send shell commands).

## 🛠️ Built With

- **[Kotlin](https://kotlinlang.org)** - The primary programming language.
- **[Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform)** - The declarative UI toolkit for building the desktop application.
- **[Gradle](https://gradle.org)** - The build automation tool.
- **[GitHub Actions](https://github.com/features/actions)** - For automated cross-platform builds and releases.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.

---
Visit us at **[Neotica](https://neotica.id)**
