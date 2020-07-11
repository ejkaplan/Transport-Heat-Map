package org.gatech.cogsci.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.model.LatLng;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
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
        String query = "";

        TextSearchRequest request = PlacesApi.textSearchQuery(context, query, location);
        map.put("request", request.location());

        return map;
    }

}
