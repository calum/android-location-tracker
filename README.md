# Android Location Tracker

This application sends your current location to an API every 60 seconds.

Open the application and enter your API address and the following will be posted as JSON:
```
{
  "location" : {
    "latitude": currentLatitude,
    "longitude": currentLongitude
  },
  "timestamp": epochTime
}
```

_Warning: The service runs in the background and will NEVER die_
