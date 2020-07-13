package org.gatech.cogsci.controller;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.gatech.cogsci.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TravelController {

    private final GeoApiContext context;
    private final PlacesController placesController;

    @Autowired
    public TravelController(GeoApiContext context, PlacesController placesController) {
        this.context = context;
        this.placesController = placesController;
    }

    @GetMapping(value = "travelTime", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Point> testTravelTime(
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng,
            @RequestParam(value = "type", required = false, defaultValue = "STORE") PlaceType type,
            @RequestParam(value = "mode", required = false, defaultValue = "DRIVING") TravelMode mode,
            @RequestParam(value = "radius", required = false, defaultValue = "10") int radius,
            @RequestParam(value = "maxTime", required = false, defaultValue = "210") int maxTime
    ) {
        LatLng origin = new LatLng(lat, lng); // Starting point
        List<LatLng> destinations = placesController.retrievePlaces(lat, lng, type, radius);
        List<Long> times = getTimes(context, origin, destinations, mode);

        List<Point> points = new ArrayList<>();
        for (int i = 0; i < destinations.size(); i++) {
            LatLng destination = destinations.get(i);
            long time = times.get(i);
            Point point = new Point(destination, 100.0 / (time / 60.0), time);
            points.add(point);
        }

        return points;
    }

    private List<Long> getTimes(GeoApiContext context, LatLng origin, List<LatLng> destinations, TravelMode mode) {
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                .origins(origin)
                .destinations(destinations.toArray(LatLng[]::new))
                .mode(mode);
        List<Long> times = new ArrayList<>();
        try {
            DistanceMatrix response = request.await();
            for (DistanceMatrixRow row : response.rows) {
                for (DistanceMatrixElement element : row.elements) {
                    times.add(element.duration.inSeconds);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return times;
    }
}
