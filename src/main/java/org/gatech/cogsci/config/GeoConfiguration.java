package org.gatech.cogsci.config;

import com.google.maps.GeoApiContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeoConfiguration {

    /**
     * GeoApiContext Object for spring autowiring
     */
    @Bean("geo")
    GeoApiContext context() {
        return new GeoApiContext.Builder()
                .apiKey("AIzaSyBIzAPCTlqrpzFDbFqRQFmGfg81I_DvmsA")
                .build();
    }
}
