package org.gatech.cogsci;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeatMapApplication {
    public static void main(String[] args) {
        Test.test(args);
        SpringApplication.run(HeatMapApplication.class, args);
    }
}
