package com.example.demo.controller;

import com.example.demo.service.StorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("storage")
public class StorageController {
    private final StorageService service;

    public StorageController(StorageService service) {
        this.service = service;
    }

    @GetMapping("{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) throws IOException {
        return  ResponseEntity.ok().contentType(MediaType.valueOf("image/png")).body(service.downloadImageFromFileSystem(fileName));
    }
}
