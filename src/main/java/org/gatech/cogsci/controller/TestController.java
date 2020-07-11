package org.gatech.cogsci.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.StaticMapsRequest;
import com.google.maps.StaticMapsRequest.Markers;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.Size;
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
public class TestController {

    @GetMapping("testRetrievedLocations")
    public Map testRetrievedLocations() {

        // Create context
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();

        // Get nearby locations
        LatLng location = new LatLng(33.781556,-84.407480); // Intersection at NE corner of gatech
        String query = "road"; // Search query
        int radius = 100; // Search radius in meters
        String type = "point_of_interest"; // Type parameter
        List<LatLng> locations = getLocations(context, location, query, radius, type);

        // Turn coordinates into markers
        Markers markers = locationsToMarkers(locations);

        // Create map
        Size size = new Size(1000,1000);
        makeMap(context,size,markers,location);

        // Dummy map to show on webpage
        Map<String, String> map = new HashMap<>();
        map.put("Message","check root directory for image of generated map");

        return map;
    }

    private List<LatLng> getLocations(GeoApiContext context, LatLng location, String query, int radius, String type){

        // Create the request
        TextSearchRequest request = new TextSearchRequest(context)
                .location(location)
                .query(query)
                .radius(radius);

        // Get list of coordinates
        List<LatLng> locations = new ArrayList<LatLng>();
        try{
            PlacesSearchResponse response = request.await();
            for (PlacesSearchResult r : response.results)
            {
                // Print locations of all places with "point_of_interest" types
                List<String> list = Arrays.asList(r.types);
                if(list.contains("point_of_interest")){
                    locations.add(r.geometry.location);
                }
            }
        } catch(Exception e) {
            System.out.println(e);
        }

        return locations;
    }

    private Markers locationsToMarkers(List<LatLng> locations){
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
