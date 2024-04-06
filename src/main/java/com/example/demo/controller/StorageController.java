package com.example.demo.controller;

import com.example.demo.model.Files;
import com.example.demo.repository.FilesRepository;
import com.example.demo.service.StorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("storage")
public class StorageController {
    private final StorageService service;
    private final FilesRepository filesRepository;

    public StorageController(StorageService service, FilesRepository filesRepository) {
        this.service = service;
        this.filesRepository = filesRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Files> postStream(HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(service.UploadLargeFile(request));
    }

    @GetMapping("{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) throws IOException {
        Map<String, Object> response = service.downloadImageFromFileSystem(fileName);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(response.get("ContentType").toString())).body(response.get("Bytes"));
    }
}
