# Transport-Heat-Map
Project for CS-6795

### Map view

Run the app locally in IntelliJ or with 
```bash
mvn spring-boot:run
```

Then go to http://127.0.0.1:8080/ to view the map.

### Endpoints

**/places**

URL Params
 - lat (required) - Latitude coordinate of origin
 - lng (required) - Longitude coordinate of origin
 - type (optional) - Type of destination. Default is `STORE`.
 - mode (optional) - Mode of tranport. Default is `DRIVING`. 
 
Returns a JSON List response of the lat/long coordinates of places nearby.
```json
[
  {
    "lat":33.7820631,
    "lng":-84.4070467,
  },
  ...
]
```

**/travelTime**
URL Params
 - lat (required) - Latitude coordinate of origin
 - lng (required) - Longitude coordinate of origin
 - type (optional) - Type of destination. Default is `STORE`.
 - mode (optional) - Mode of tranport. Default is `DRIVING`. 
 - radius (optional) - Radius from the origin to look.
 - maxTime (optional) - Maximum travel time allowed.

Returns a JSON List response of the lat/long coordinates of places nearby, weight for the heatmap, and travel time in seconds.
```json
[
  {
    "location": {
      "lat": 33.7820631,
      "lng": -84.4070467,
    },
    "timeInSeconds": 33,
    "weight":181.8181818181818,
  },
  ...
]
```

### Weight Calculation

WORK IN PROGRESS

100 / timeInMinutes

### Available Transportation Modes
 - DRIVING
 - WALKING
 - BICYCLING
 - TRANSIT
 - UNKNOWN
 
### Available Destination Types

 -  ACCOUNTING
 -  AIRPORT
 -  AMUSEMENT_PARK
 -  AQUARIUM
 -  ART_GALLERY
 -  ATM
 -  BAKERY
 -  BANK
 -  BAR
 -  BEAUTY_SALON
 -  BICYCLE_STORE
 -  BOOK_STORE
 -  BOWLING_ALLEY
 -  BUS_STATION
 -  CAFE
 -  CAMPGROUND
 -  CAR_DEALER
 -  CAR_RENTAL
 -  CAR_REPAIR
 -  CAR_WASH
 -  CASINO
 -  CEMETERY
 -  CHURCH
 -  CITY_HALL
 -  CLOTHING_STORE
 -  CONVENIENCE_STORE
 -  COURTHOUSE
 -  DENTIST
 -  DEPARTMENT_STORE
 -  DOCTOR
 -  DRUGSTORE
 -  ELECTRICIAN
 -  ELECTRONICS_STORE
 -  EMBASSY
 -  FIRE_STATION
 -  FLORIST
 -  FUNERAL_HOME
 -  FURNITURE_STORE
 -  GAS_STATION
 -  GROCERY_OR_SUPERMARKET
 -  GYM
 -  HAIR_CARE
 -  HARDWARE_STORE
 -  HINDU_TEMPLE
 -  HOME_GOODS_STORE
 -  HOSPITAL
 -  INSURANCE_AGENCY
 -  JEWELRY_STORE
 -  LAUNDRY
 -  LAWYER
 -  LIBRARY
 -  LIGHT_RAIL_STATION
 -  LIQUOR_STORE
 -  LOCAL_GOVERNMENT_OFFICE
 -  LOCKSMITH
 -  LODGING
 -  MEAL_DELIVERY
 -  MEAL_TAKEAWAY
 -  MOSQUE
 -  MOVIE_RENTAL
 -  MOVIE_THEATER
 -  MOVING_COMPANY
 -  MUSEUM
 -  NIGHT_CLUB
 -  PAINTER
 -  PARK
 -  PARKING
 -  PET_STORE
 -  PHARMACY
 -  PHYSIOTHERAPIST
 -  PLUMBER
 -  POLICE
 -  POST_OFFICE
 -  PRIMARY_SCHOOL
 -  REAL_ESTATE_AGENCY
 -  RESTAURANT
 -  ROOFING_CONTRACTOR
 -  RV_PARK
 -  SCHOOL
 -  SECONDARY_SCHOOL
 -  SHOE_STORE
 -  SHOPPING_MALL
 -  SPA
 -  STADIUM
 -  STORAGE
 -  STORE
 -  SUBWAY_STATION
 -  SUPERMARKET
 -  SYNAGOGUE
 -  TAXI_STAND
 -  TOURIST_ATTRACTION
 -  TRAIN_STATION
 -  TRANSIT_STATION
 -  TRAVEL_AGENCY
 -  UNIVERSITY
 -  VETERINARY_CARE
 -  ZOO