package com.example.demo.service;

import com.example.demo.exception.BaseException;
import com.example.demo.exception.FileException;
import com.example.demo.exception.ProfileException;
import com.example.demo.model.ImageData;
import com.example.demo.model.Profile;
import com.example.demo.repository.ImageRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UID;
import java.util.*;

@Service
public class StorageService {
    final private ImageRepository imageRepository;
    final private String FOLDER_PATH = "/Users/gbymacminim1/Desktop/Kia/uploads/";
    final private String HOST_PATH = "http://localhost:8080/storage/";

    final private List<String> FILE_SUPPORT = Arrays.asList("image/jpeg", "image/png", "image/jpg","application/octet-stream");


    public String getExtensionByStringHandling(String filename) {
        Optional<String> newFile = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        return newFile.orElse("");
    }

    public String GenerateName(String fileName) {
        return UUID.randomUUID() + String.valueOf(System.currentTimeMillis()) + "." + getExtensionByStringHandling(fileName);
    }

    public StorageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ImageData uploadImageToFileSystem(MultipartFile file) throws IOException, BaseException {

        if (file == null) {
            throw FileException.FileIsNull();
        }
        String contentType = file.getContentType();
        if (contentType == null) {
            throw FileException.FileNotSupport();
        }
        if (!FILE_SUPPORT.contains(contentType)) {
            throw FileException.FileNotSupport();
        }
        String newName = GenerateName(file.getOriginalFilename());
        String filePath = FOLDER_PATH + newName;
        String fullPath = HOST_PATH + newName;
        ImageData imageDataSave = ImageData.builder().name(newName).type(file.getContentType()).path(filePath).fullPath(fullPath).build();
        file.transferTo(new File(filePath));
        return imageDataSave;
    }

    public void removeFile(Long file_id){
        Optional<ImageData> imageDataOpt =  imageRepository.findById(file_id);
        if(imageDataOpt.isEmpty()){
            throw  FileException.FileNotFound();
        }
        ImageData imageData = imageDataOpt.get();
        File fileToDelete = new File(imageData.getPath());
        fileToDelete.delete();
    }
    public byte[] downloadImageFromFileSystem(String fileName) throws BaseException, IOException {
        Optional<ImageData> imageData = imageRepository.findByName(fileName);
        if (imageData.isPresent()) {
            String filePath = imageData.get().getPath();
            return Files.readAllBytes(new File(filePath).toPath());
        }
        throw FileException.FileNotFound();
    }


}

