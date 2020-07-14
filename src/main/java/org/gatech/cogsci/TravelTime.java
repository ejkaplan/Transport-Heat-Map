package org.gatech.cogsci;

import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.ImageResult;
import com.google.maps.StaticMapsRequest;
import com.google.maps.model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.maps.StaticMapsApi.newRequest;

public class TravelTime {

    public List<List<LatLng>> getGroupedPoints(GeoApiContext context, LatLng origin, double dist, int n, TravelMode mode, int maxTime, int sep) {

        // Get nearby locations in a grid
        LatLng[] destinations = getLocations(context, origin, dist, n);

        // Get times from originLocation to locations
        long[] times = getTimes(context, origin, destinations, mode);

        // Filter locations
        List<List<LatLng>> groupedPoints = groupLocationsByTime(destinations, times, maxTime, sep);

        return groupedPoints;
    }

    public void mapGroupedPoints(GeoApiContext context, List<List<LatLng>> groupedPoints, LatLng origin, int maxTime, int sep){
        // Turn coordinates into markers
        List<StaticMapsRequest.Markers> groupedMarkers = new ArrayList<StaticMapsRequest.Markers>();
        String[] colors = {"white", "red", "orange", "yellow", "green", "blue", "purple", "gray", "brown", "black"};
        int c = 0;
        for(int i=maxTime; i > 0; i=i-sep){
            groupedMarkers.add(locationsToMarkers(groupedPoints.get(c), colors[c]));
            c += 1;
        }

        // Create maps
        Size size = new Size(1000,1000);
        String title = "map.jpg";
        makeMap(context,size,groupedMarkers,origin,title);
    }

    private LatLng[] getLocations(GeoApiContext context, LatLng origin, double dist, int n){

        // Create 2D grid of LatLng
        LatLng[][] grid = new LatLng[n][n];
        double lat_t = origin.lat - dist * (double) Math.floor(n/2);
        double lng_l = origin.lng - dist * (double) Math.floor(n/2);

        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                LatLng location = new LatLng(lat_t + (dist * i), lng_l + (dist * j));
                grid[i][j] = location;
            }
        }

        // Flatten into 1D array
        LatLng[] flat = new LatLng[n*n];
        for (int i=0; i<n; i++){
            for (int j=0; j<n; j++){
                flat[i*n + j] = grid[i][j];
            }
        }

        return flat;
    }

    private long[] getTimes(GeoApiContext context, LatLng origin, LatLng[] destinations, TravelMode mode){

        // Make request for distance matrix
        DistanceMatrixApiRequest request = new DistanceMatrixApiRequest(context)
                .origins(origin)
                .destinations(destinations)
                .mode(mode);

        // Extract integer times
        long[] times = new long[destinations.length];
        try {
            DistanceMatrix response = request.await();
            for (DistanceMatrixRow row : response.rows) {
                int i = 0;
                for (DistanceMatrixElement element : row.elements){
                    times[i] = element.duration.inSeconds/60;
                    i++;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return times;
    }

    private List<List<LatLng>> groupLocationsByTime(LatLng[] locations, long[] times, int maxTime, int sep) {

        // Initialize structure to group locations
        List<List<LatLng>> filteredLocations = new ArrayList<>();

        // Group locations by time range
        int c = 0;
        for (int i=maxTime; i>0; i=i-sep){
            List<LatLng> temp = new ArrayList<LatLng>();
            for (int j=0; j<locations.length; j++){
                if(times[j] >= i - (double) sep/2 && times[j] < i + (double) sep/2) {
                    temp.add(locations[j]);
                }
            }
            filteredLocations.add(temp);
        }

        // Reverse locations so that index 0 is shortest time.
        Collections.reverse(filteredLocations);

        return filteredLocations;
    }

    private StaticMapsRequest.Markers locationsToMarkers(List<LatLng> locations, String color){
        StaticMapsRequest.Markers markers = new StaticMapsRequest.Markers();
        markers.color(color);
        for(LatLng location : locations){
            markers.addLocation(location);
        }

        return markers;
    }


    private void makeMap(GeoApiContext context, Size size, StaticMapsRequest.Markers markers, LatLng center, String title) {
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
            ImageIO.write(image, "jpg", new File(title));
            System.out.println("image created");
        } catch(Exception e){
            System.out.println(e);
        }
    }

    private void makeMap(GeoApiContext context, Size size, List<StaticMapsRequest.Markers> groupedMarkers, LatLng center, String title) {
        StaticMapsRequest request = newRequest(context,size)
                .center(center);

        for(StaticMapsRequest.Markers markers : groupedMarkers){
            request.markers(markers);
        }

        ImageResult imageResult = new ImageResult(null,null);
        try {
            imageResult = request.await();
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(imageResult.imageData);
            BufferedImage image = ImageIO.read(bis);
            ImageIO.write(image, "jpg", new File(title));
            System.out.println("image created");
        } catch(Exception e){
            System.out.println(e);
        }
    }

}