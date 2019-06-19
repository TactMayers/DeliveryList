# Delivery List Demo

This project is an demonstration Android app for displaying a list of delivery items. When an item is selected, the
details and location of delivery.

# Project setup

After checking out the project from git, please edit app/module-settings.properties file and fill in
appropriate value for the following keys:
* GOOGLE_MAP_API_KEY - API key for Google Map Android SDK (**Do not** enclose in double quotation mark)
* API_URL - Base URL for the API (**Must** be enclosed in double quotation mark)

Example
```
GOOGLE_MAP_API_KEY=0123456789abcdefghijklmnopqrstuvwxyzABC
API_URL="https://www.example.com"
```

# Usage and flow

When user open the app for the first time, it will retrieve the first 20 items (a page) of delivery list via network. By scrolling down to the end of the list, another page will be retrieved until the network API returns no more results. Retrieved data will be cached and remains available even after user go to details page, goes back to home screen or the app is terminated. To retrieve a fresh list from the network again, user can scroll to the top of the list and perform a swipe-down gesture to refresh the list again from network.

If a network error occurs, a message will appear telling user so. User can retry retrieving data by scrolling up pass the "Loading more..." footer and scroll down again.

Clicking an item on the list will bring user to detail page. In detail page, the location of the delivery will be marked on Google Map. Clicking on the marker will show the description of the delivery. If user navigate the map away from the marker, user can click on the re-center button on the top right corner to bring the map back to delivery location.

# Technical Details

## Libraries used
* Android Architecture Components
  * ViewModel
  * Room (Database)
* Android widgets
  * SwipeRefreshLayout
  * Snackbar
* Google Map Android SDK
* GSON
* Glide (image loading)
* Retrofit (HTTP request/response)
