package org.gatech.cogsci.controller;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    private final GeoApiContext geoApiContext;

    /**
     * Spring constructor
     *
     * @param geoApiContext Injected API context from GeoConfiguration.java
     */
    @Autowired
    public TestController(@Qualifier("geo") GeoApiContext geoApiContext) {
        this.geoApiContext = geoApiContext;
    }

    @GetMapping("test")
    public PlacesSearchResult[] test() throws InterruptedException, ApiException, IOException {
        LatLng location = new LatLng(33.781556, -84.407480);
        String query = "food";

        TextSearchRequest request = PlacesApi.textSearchQuery(geoApiContext, query, location);
        request.radius(100);
        return request.await().results;
    }

}
