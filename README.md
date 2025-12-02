# Local server URL for Retrofit
- In local.properties create the following line: 
BASE_URL=http://<your_local_ip>:8080/

# Notes on Networking

- For local development, `android:usesCleartextTraffic="true"` is enabled in Manifest.
- In production, all HTTP traffic should use HTTPS and proper network security configuration.
