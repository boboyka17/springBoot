package com.example.demo.controller;


import com.example.demo.Response.CommonResponse;
import com.example.demo.model.Profile;
import com.example.demo.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/profile")
public class ProfileController {
    private  final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<CommonResponse> createProfile(@RequestParam("file") MultipartFile file, @RequestParam("fullname") String fullname,@RequestParam("detail") String detail) throws IOException {
        return ResponseEntity.ok(service.createProfile(file,fullname,detail));
    }

    @PutMapping("/update/{profile_id}")
    public ResponseEntity<Profile> updateProfile( @RequestParam(value = "file",required = false) MultipartFile file ,@RequestParam("fullname") String fullname,@RequestParam("detail") String detail,@PathVariable int profile_id) throws IOException {
        return  ResponseEntity.ok(service.updateProfile(profile_id,file,fullname,detail));
    }

    @DeleteMapping("/delete")
    public  ResponseEntity<CommonResponse> removeProfile(@RequestParam("profile_id") int profile_id){
        return ResponseEntity.ok(service.removeProfile(profile_id));
    }

    @GetMapping("/")
    public ResponseEntity<List<Profile>> getProfile(){
        return  ResponseEntity.ok(service.getProfile());
    }

    @GetMapping("/{profile_id}")
    public ResponseEntity<Profile> getProfileOne(@PathVariable int profile_id){
        return ResponseEntity.ok(service.getProfileOne(profile_id));
    }

}
