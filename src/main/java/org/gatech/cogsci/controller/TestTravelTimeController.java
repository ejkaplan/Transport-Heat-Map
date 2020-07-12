package org.gatech.cogsci.controller;

import java.util.*;
import com.google.maps.*;
import com.google.maps.model.*;
import org.gatech.cogsci.TravelTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestTravelTimeController {

    @GetMapping("testTravelTime")
    public Map testTravelTime(){

        // Dummy map to show on web page
        Map<String, String> map = new HashMap<>();
        map.put("Message","View generated map in root directory");

        // Create context
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();

        // Get grouped locations
        TravelTime tt = new TravelTime(); // Instance of TravelTime Class
        LatLng origin = new LatLng(33.781556,-84.407480); // Starting point, NE corner of GaTech campus
        double dist = 0.05; // Distance increment in latitude and longitude
        int n = 9; // Length of side for nxn grid with origin in center
        TravelMode mode = TravelMode.DRIVING; // Travel mode
        int maxTime = 30; // The max time considered
        int sep = 4; // The "bin size" for grouping points together

        // Get points grouped by time
        List<List<LatLng>> groupedPoints = tt.getGroupedPoints(context, origin, dist, n, mode, maxTime, sep);

        // Plot points on map and save to root directory
        tt.mapGroupedPoints(context, groupedPoints, origin, maxTime, sep);

        return map;
    }
}
