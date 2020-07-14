// Create the script tag, set the appropriate attributes
var script = document.createElement('script');
script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyC3s-gEdeHL7yQNj3idvajOhehOe66IQGk&libraries=visualization&callback=initMap';
script.defer = true;
script.async = false;

var map;
var marker;
var markers = [];
var originLocation;
var selectedMode = "DRIVING";
var selectedPlace = "GROCERY_OR_SUPERMARKET";
var widget = null;

function setMode(mode) {
    selectedMode = mode;
}

function setPlace(place) {
    selectedPlace = place;
}

function placeMarker(location) {
    if (marker == undefined) {
        marker = new google.maps.Marker({
            position: location,
            map: map,
            animation: google.maps.Animation.DROP,
        });
    } else {
        marker.setPosition(location);
    }
}

function displayTime(seconds) {
    var minutes = Math.round(seconds / 60);
    return minutes + " minute(s) away.";
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

            const currentMarker = new google.maps.Marker({
                position: new google.maps.LatLng(lat, lng),
                map: map,
                label: "Current Location"
            });

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

                marker.addListener('click', function() {
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

function displayDefault() {
    var gerogiaAquarium = {
        coords: {
            // St louis
            // latitude: 38.600420,
            // longitude: -90.235566
            // New York
            // latitude: 40.744934,
            // longitude: -73.951381
            // Atlanta
            latitude: 33.763068,
            longitude: -84.393761
        }
    };
    displayLocation(gerogiaAquarium);
}

function displayLocation(position) {
    originLocation = position;
    var location = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);
    map = new google.maps.Map(document.getElementById('map'), {
        center: location,
        zoom: 13,
        mapTypeId: 'roadmap'
    });

    if (widget === null) {
        widget = new walkscore.TravelTimeWidget({
            map: map,
            origin: position.coords.latitude + ',' + position.coords.longitude,
            show: true,
            mode: walkscore.TravelTime.Mode.DRIVE
        });
    } else {
        widget.setMap(map);
    }

    /* Data points defined as a mixture of WeightedLocation and LatLng objects */
    getPoints(map, position.coords.latitude, position.coords.longitude, selectedPlace, selectedMode, 100, 120)
        .then(function (heatMapData) {
            var heatmap = new google.maps.visualization.HeatmapLayer({
                data: heatMapData
            });
            heatmap.setMap(map);

            google.maps.event.addListener(map, 'click', function (event) {
                placeMarker(event.latLng);
            });
        });
}

// Attach your callback function to the `window` object
window.initMap = function () {
    navigator.geolocation.getCurrentPosition(displayLocation, displayDefault);
};

// Append the 'script' element to 'head'
document.head.appendChild(script);
