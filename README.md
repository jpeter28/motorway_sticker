# Summary 
- The project was made with Kotlin language, for Android platform

# Testing the application
- In local.properties create the following line: 
BASE_URL=http://<your_local_ip>:8080/
- Need to run docker with the files in /api folder (which was provided for the task, in the repository this folder not available)

# Notes on Networking

- For local development, `android:usesCleartextTraffic="true"` is enabled in Manifest.
- In production, all HTTP traffic should use HTTPS and proper network security configuration.

# Possible improvements

- The API should be able to accept a language parameter and it can send back result in that language, easier to scale for multiple languages 
- The API should be able to accept a list of vehicle types parameter so it will only return with the results for that vehicles
- The Application only handles one vehicle, but it is possible that one user has more
- The Map is not finished, it will look differently on different devices