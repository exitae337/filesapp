# FilesApp 📱
A simple mobile application for file transfer through a server. Student project.

# About
This is my educational project for university defense. Allows users to exchange files through a central server.

# Main Features

🔐 User registration and login

📤 File upload to server

📥 File download from server

🔑 5-digit key generation for file receiving

📱 Simple mobile interface

# How It Works
1. Receiver generates a unique 5-digit code in the app
2. Sender enters this code and selects a file to send
3. File gets uploaded to the server
4. Receiver sees the file in their list and can download it from dir on server (dirName = 5-digit code)

# Technologies
Client: Kotlin (Android)
Server: Ktor (Kotlin)
Protocol: HTTP/REST API

Installation
For Developers
1. Clone the repository:
```bash
git clone https://github.com/exitae337/filesapp.git
```
2. Open project in Android Studio
3. Configure server URL
4. Run the app on emulator or device

# Developer
Chernyshev Maxim

Contact: exitae337@gmail.com

This is an educational project developed for university defense.
