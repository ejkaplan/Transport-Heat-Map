package org.gatech.cogsci.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TestController {

    @GetMapping("test")
    public Map test() {
        Map<String, String> map = new HashMap<>();


        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();

        LatLng location = new LatLng(33.781556,-84.407480);
        String query = "road";
        int radius = 100;

        TextSearchRequest request = new TextSearchRequest(context)
                .location(location)
                .query(query)
                .radius(radius);
        try{
            PlacesSearchResponse response = request.await();
            for (PlacesSearchResult r : response.results)
            {

                List<String> list = Arrays.asList(r.types);

                if(list.contains("intersection")){
                    System.out.println(r.geometry.location);
                }

            }
        } catch(Exception e) {
            map.put("Error",e.toString());
        }

        return map;
    }
}
