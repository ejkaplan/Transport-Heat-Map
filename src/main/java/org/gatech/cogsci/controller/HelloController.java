package org.gatech.cogsci.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("hello")
    public Map hello() {
        Map<String, String> map = new HashMap<>();
        map.put("message 1", "hello");
        map.put("message 2", "there");
        return map;
    }

}
