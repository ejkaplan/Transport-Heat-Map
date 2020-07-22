// Create the script tag, set the appropriate attributes
var script = document.createElement('script');
script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyC3s-gEdeHL7yQNj3idvajOhehOe66IQGk&libraries=visualization&callback=initMap';
script.defer = true;
script.async = false;

var map = null;
var marker = null;
var markers = [];
var originPoint;
var selectedMode = "DRIVING";
var selectedPlace = "GROCERY_OR_SUPERMARKET";
var widget = null;

function setMode(mode) {
    selectedMode = mode;
}

function setPlace(place) {
    selectedPlace = place;
}

function getWalkscoreMode() {
    if (selectedMode === "DRIVING") {
        return walkscore.TravelTime.Mode.DRIVE;
    } else if (selectedMode === "WALKING") {
        return walkscore.TravelTime.Mode.WALK;
    } else if (selectedMode === "BICYCLING") {
        return walkscore.TravelTime.Mode.BIKE;
    } else if (selectedMode === "TRANSIT") {
        return walkscore.TravelTime.Mode.TRANSIT;
    }
}

function displayTime(seconds) {
    const minutes = Math.round(seconds / 60);
    return minutes + " minute(s) away.";
}

function placeInitialMarker(location) {
    placeMarker(new google.maps.LatLng(location.coords.latitude, location.coords.longitude));
}

function displayDefault() {
    const georgiaAquarium = new google.maps.LatLng(33.763068, -84.393761);
    placeMarker(georgiaAquarium);
}

function placeMarker(location) {
    originPoint = location;
    displayLocation();
    if (marker === null) {
        marker = new google.maps.Marker({
            position: location,
            map: map,
            animation: google.maps.Animation.DROP,
            icon: {
                url: "http://maps.google.com/mapfiles/ms/icons/blue-dot.png"
            }
        });
    } else {
        marker.setPosition(location);
    }
}

function getPoints(map, lat, lng, type, mode, radius, maxTime) {
    return new Promise(resolve => {
        $.ajax({
            url: '/travelTime?' +
                'lat=' + lat +
                '&lng=' + lng +
                '&type=' + type.toUpperCase() +
                '&mode=' + mode.toUpperCase() +
                '&radius=' + radius +
                '&maxTime=' + maxTime
        }).then(function (data) {

            for (let i = 0; i < markers.length; i++) {
                markers[i].setMap(null); // removes
            }
            markers = [];

            const points = [];
            for (let i = 0; i < data.length; i++) {
                const latlng = new google.maps.LatLng(data[i].location.lat, data[i].location.lng);
                const marker = new google.maps.Marker({
                    position: latlng,
                    map: map
                });
                const infowindow = new google.maps.InfoWindow({
                    content: displayTime(data[i].timeInSeconds)
                });

                marker.addListener('click', function () {
                    infowindow.open(map, marker);
                });

                markers.push(marker);

                points.push({
                    location: latlng,
                    weight: data[i].weight
                })
            }
            resolve(points);
        });
    });
}

function displayLocation() {
    if (map === null) {
        map = new google.maps.Map(document.getElementById('map'), {
            center: originPoint,
            zoom: 13,
            mapTypeId: 'roadmap'
        });
        google.maps.event.addListener(map, 'click', function (event) {
            placeMarker(event.latLng);
        });
    }

    if (widget === null) {
        widget = new walkscore.TravelTimeWidget({
            map: map,
            origin: originPoint.lat() + ',' + originPoint.lng(),
            show: true,
            mode: getWalkscoreMode()
        });
    } else {
        widget.setMode(getWalkscoreMode());
        widget.setOrigin(originPoint.lat() + ',' + originPoint.lng());
    }

    /* Data points defined as a mixture of WeightedLocation and LatLng objects */
    getPoints(map, originPoint.lat(), originPoint.lng(), selectedPlace, selectedMode, 100, 120)
        .then(function () {});
}

// Attach your callback function to the `window` object
window.initMap = function () {
    navigator.geolocation.getCurrentPosition(placeInitialMarker, displayDefault);
};

// Append the 'script' element to 'head'
document.head.appendChild(script);
