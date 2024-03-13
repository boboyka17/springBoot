package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public/info")
public class InfoController {


    @GetMapping
    public Object getInfo() {
        final String BASE_URL = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
        String os = System.getProperty("os.name");
        Map<String, String> map = new HashMap<>();
        map.put("hostname", BASE_URL);
        map.put("os", os);
        return map;
    }
}
