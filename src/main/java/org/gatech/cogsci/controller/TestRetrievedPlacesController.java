package org.gatech.cogsci.controller;

import com.google.maps.*;
import com.google.maps.StaticMapsRequest.Markers;
import com.google.maps.model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


import java.util.*;

import static com.google.maps.StaticMapsApi.newRequest;

@RestController
public class TestRetrievedPlacesController {

    @GetMapping("testRetrievedPlaces")
    public Map testRetrievedPlaces() {

        // Create context
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();

        // Get nearby locations
        LatLng origin = new LatLng(33.781556,-84.407480); // Intersection at NE corner of gatech
        String query = "all"; // Search query
        PlaceType type = PlaceType.STORE;
        int radius = 10; // Search radius in meters
        //LatLng[] destinations = getLocationsByQuery(context, origin, query, radius);
        LatLng[] destinations = getLocationsByType(context, origin, type, radius);

        // Get times from origin to locations
        TravelMode mode = TravelMode.DRIVING;
        getTimes(context, origin, destinations, mode);

        // Turn coordinates into markers
        Markers markers = locationsToMarkers(destinations);

        // Create map
        Size size = new Size(1000,1000);
        makeMap(context,size,markers,origin);

        // Dummy map to show on webpage
        Map<String, String> map = new HashMap<>();
        map.put("Message","check root directory for image of generated map");

        return map;
    }

    private LatLng[] getLocationsByQuery(GeoApiContext context, LatLng location, String query, int radius){

        // Create the request
        TextSearchRequest request = new TextSearchRequest(context)
                .location(location)
                .query(query)
                .radius(radius);

        // Get list of coordinates
        List<LatLng> locations = new ArrayList<LatLng>();
        try{
            PlacesSearchResponse response = request.await();
            while (true) {
                for (PlacesSearchResult r : response.results) {
                    // Print locations of all places with "point_of_interest" types
                    List<String> list = Arrays.asList(r.types);
                    locations.add(r.geometry.location);
                }
                if (response.nextPageToken != null){
                    response = new TextSearchRequest(context).pageToken(response.nextPageToken).await();
                    continue;
                }
                break;
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return locations.toArray(LatLng[]::new);
    }

    private LatLng[] getLocationsByType(GeoApiContext context, LatLng location, PlaceType type, int radius){

        // Create the request
        TextSearchRequest request = new TextSearchRequest(context)
                .location(location)
                .type(type)
                .radius(radius);

        // Get list of coordinates
        List<LatLng> locations = new ArrayList<LatLng>();
        try{
            PlacesSearchResponse response = request.await();
            for (PlacesSearchResult r : response.results)
            {
                // Print locations of all places with "point_of_interest" types
                List<String> list = Arrays.asList(r.types);
                locations.add(r.geometry.location);
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return locations.toArray(LatLng[]::new);
    }

    private void getTimes(GeoApiContext context, LatLng origin, LatLng[] destinations, TravelMode mode){
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                .origins(origin)
                .destinations(destinations)
                .mode(mode);
        try {
            DistanceMatrix response = request.await();
            for (DistanceMatrixRow row : response.rows) {
                for (DistanceMatrixElement element : row.elements){
                    System.out.println(element.duration.humanReadable);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private Markers locationsToMarkers(LatLng[] locations){
        Markers markers = new Markers();
        for(LatLng location : locations){
            markers.addLocation(location);
        }

        return markers;
    }


    private void makeMap(GeoApiContext context, Size size, Markers markers, LatLng center) {
        StaticMapsRequest request = newRequest(context,size)
                .markers(markers)
                .center(center);
        ImageResult imageResult = new ImageResult(null,null);
        try {
            imageResult = request.await();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageResult.imageData);
            BufferedImage image = ImageIO.read(bis);
            ImageIO.write(image, "jpg", new File("map.jpg"));
            System.out.println("image created");
        } catch(Exception e){
            System.out.println(e);
        }
    }

}
