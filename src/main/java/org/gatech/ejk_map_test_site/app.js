// Create the script tag, set the appropriate attributes
var script = document.createElement('script');
script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyC3s-gEdeHL7yQNj3idvajOhehOe66IQGk&libraries=visualization&callback=initMap';
script.defer = true;
script.async = true;

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

// Attach your callback function to the `window` object
window.initMap = function() {
  /* Data points defined as a mixture of WeightedLocation and LatLng objects */
  var heatMapData = [
    {location: new google.maps.LatLng(37.782, -122.447), weight: 0.5},
    new google.maps.LatLng(37.782, -122.445),
    {location: new google.maps.LatLng(37.782, -122.443), weight: 2},
    {location: new google.maps.LatLng(37.782, -122.441), weight: 3},
    {location: new google.maps.LatLng(37.782, -122.439), weight: 2},
    new google.maps.LatLng(37.782, -122.437),
    {location: new google.maps.LatLng(37.782, -122.435), weight: 0.5},

    {location: new google.maps.LatLng(37.785, -122.447), weight: 3},
    {location: new google.maps.LatLng(37.785, -122.445), weight: 2},
    new google.maps.LatLng(37.785, -122.443),
    {location: new google.maps.LatLng(37.785, -122.441), weight: 0.5},
    new google.maps.LatLng(37.785, -122.439),
    {location: new google.maps.LatLng(37.785, -122.437), weight: 2},
    {location: new google.maps.LatLng(37.785, -122.435), weight: 3}
  ];

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

}

// Append the 'script' element to 'head'
document.head.appendChild(script);
