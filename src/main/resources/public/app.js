// Create the script tag, set the appropriate attributes
var script = document.createElement('script');
script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyC3s-gEdeHL7yQNj3idvajOhehOe66IQGk&libraries=visualization&callback=initMap';
script.defer = true;
script.async = false;

var map;
var marker;

function placeMarker(location) {
  if (marker == undefined){
    marker = new google.maps.Marker({
      position: location,
      map: map,
      animation: google.maps.Animation.DROP,
    });
  }
  else{
    marker.setPosition(location);
  }
}

function getPoints(lat, lng, type, mode, radius, maxTime) {
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
      console.log("resolving promise");
      var points = [];
      for (var i = 0; i < data.length; i++) {
        points.push({
          location: new google.maps.LatLng(data[i].location.lat, data[i].location.lng),
          weight: data[i].weight
        })
      }
      resolve(points);
    });
  });
}

// Attach your callback function to the `window` object
window.initMap = function() {
  /* Data points defined as a mixture of WeightedLocation and LatLng objects */
  getPoints(37.774546, -122.433523, "DENTIST", "WALKING", 100, 120)
      .then(function (heatMapData) {
        console.log(heatMapData);
        var sanFrancisco = new google.maps.LatLng(37.774546, -122.433523);

        map = new google.maps.Map(document.getElementById('map'), {
          center: sanFrancisco,
          zoom: 13,
          mapTypeId: 'satellite'
        });

        var heatmap = new google.maps.visualization.HeatmapLayer({
          data: heatMapData
        });
        heatmap.setMap(map);

        google.maps.event.addListener(map, 'click', function(event) {
          placeMarker(event.latLng);
        });
      });
};

// Append the 'script' element to 'head'
document.head.appendChild(script);
