# Chirag Farmer

An agricultural e-commerce marketplace and AI-powered crop advisory Android app built with Jetpack Compose.

## Features

- **Buy & Sell** – Browse/search farming supplies (seeds, tractors, sprayers, drones, etc.) and list products for sale with image uploads.
- **Cart & Checkout** – Add items to cart, manage quantities, place orders with delivery address selection via OpenStreetMap.
- **Payments** – In-app UPI payments through PhonePe Intent SDK with fallback UPI deep-link.
- **Orders & Reviews** – Track order status, cancel items, and leave product ratings/reviews.
- **AI Crop Advisory** – Chat with an AI assistant about crop issues or upload a photo for disease identification with remedy recommendations.
- **Service Booking** – Book agricultural services (e.g., drone spraying) with location-based scheduling.
- **OTP Authentication** – Phone number-based login via MSG91.
- **Push Notifications** – Order updates and alerts via Firebase Cloud Messaging.
- **Product Sharing** – Share product links via deep links.

## Tech Stack

- **UI** – Jetpack Compose, Material3, Navigation Compose, Paging 3, Coil, Shimmer
- **DI** – Hilt (Dagger)
- **Network** – Retrofit, OkHttp, Gson
- **Local Storage** – Room, DataStore Preferences
- **Maps** – OpenStreetMap (osmdroid)
- **Payments** – PhonePe Intent SDK
- **Cloud** – Cloudinary (images), Firebase (messaging)
- **Architecture** – Clean Architecture (data/domain/ui/di layers)

## Build

Requires Android SDK 36, Kotlin 2.0+, and a `local.properties` file with the following keys:

```
OSM_NOMINATIM_BASE_URL=<url>
CLOUD_NAME=<cloudinary_cloud_name>
CLOUDINARY_UPLOAD_PRESET=<upload_preset>
PHONEPE_MERCHANT_ID=<merchant_id>
HASHIDS_SALT=<salt>
```

## License

Private project.
