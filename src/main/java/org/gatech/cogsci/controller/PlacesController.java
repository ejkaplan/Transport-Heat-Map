package org.gatech.cogsci.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.StaticMapsRequest;
import com.google.maps.StaticMapsRequest.Markers;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.google.maps.model.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.maps.StaticMapsApi.newRequest;

@RestController
public class PlacesController {

    private final GeoApiContext context;

    @Autowired
    public PlacesController(GeoApiContext context) {
        this.context = context;
    }

    /**
     * GET request for retriving places image
     * /places?lat=33.781556&lng=-84.407480
     */
    @GetMapping(value = "placesImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] retrievePlacesImage(
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng,
            @RequestParam(value = "type", required = false, defaultValue = "STORE") PlaceType type,
            @RequestParam(value = "radius", required = false, defaultValue = "10") int radius
    ) {
        LatLng origin = new LatLng(lat, lng);
        List<LatLng> destinations = retrievePlaces(lat, lng, type, radius);

        // Turn coordinates into markers
        Markers markers = locationsToMarkers(destinations);

        // Create map
        Size size = new Size(1000, 1000);
        return makeMap(context, size, markers, origin);

    }

    /**
     * GET request for retriving places as latlng coords
     * /places?lat=33.781556&lng=-84.407480
     * @return
     */
    @GetMapping(value = "places", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LatLng> retrievePlaces(
            @RequestParam("lat") Double lat,
            @RequestParam("lng") Double lng,
            @RequestParam(value = "type", required = false, defaultValue = "STORE") PlaceType type,
            @RequestParam(value = "radius", required = false, defaultValue = "10") int radius
    ) {

        LatLng origin = new LatLng(lat, lng);

        // Create the request
        TextSearchRequest request = new TextSearchRequest(context)
                .location(origin)
                .type(type)
                .radius(radius);

        // Get list of coordinates
        List<LatLng> locations = new ArrayList<>();
        try {
            PlacesSearchResponse response = request.await();
            for (PlacesSearchResult r : response.results) {
                // Print locations of all places with "point_of_interest" types
                List<String> list = Arrays.asList(r.types);
                locations.add(r.geometry.location);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return locations;
    }



    private Markers locationsToMarkers(List<LatLng> locations) {
        Markers markers = new Markers();
        for (LatLng location : locations) {
            markers.addLocation(location);
        }

        return markers;
    }

    private byte[] makeMap(GeoApiContext context, Size size, Markers markers, LatLng center) {
        StaticMapsRequest request = newRequest(context, size)
                .markers(markers)
                .center(center);
        ImageResult imageResult = new ImageResult(null, null);
        try {
            imageResult = request.await();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageResult.imageData);
            byte[] image = bis.readAllBytes();
//            BufferedImage image = ImageIO.read(bis);
//            ImageIO.write(image, "jpg", new File("map.jpg"));
            System.out.println("image created");
            return image;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

}
