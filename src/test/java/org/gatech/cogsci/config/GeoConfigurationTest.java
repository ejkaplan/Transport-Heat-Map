package org.gatech.cogsci.config;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.TextSearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class GeoConfigurationTest {

    private GeoApiContext testObject;

    @Before
    public void setUp() {
        testObject = new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();
    }

    @Test
    public void contextWorksAsExpected() throws InterruptedException, ApiException, IOException {
        LatLng location = new LatLng(33.781556,-84.407480);
        String query = "food";

        TextSearchRequest request = PlacesApi.textSearchQuery(testObject, query, location);
        request.radius(100);
        PlacesSearchResponse response = request.await();

        // Printing to show everything returned from Google
        for (PlacesSearchResult result : response.results) {
            System.out.println(result.name);
        }

        Assert.assertEquals("Chick-fil-A", response.results[0].name);
    }
}