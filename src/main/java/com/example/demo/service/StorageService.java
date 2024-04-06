package com.example.demo.service;

import com.example.demo.exception.BaseException;
import com.example.demo.exception.FileException;
import com.example.demo.model.Files;
import com.example.demo.repository.FilesRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload2.core.DiskFileItemFactory;
import org.apache.commons.fileupload2.core.FileItem;
import org.apache.commons.fileupload2.jakarta.JakartaServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class StorageService {
    final private FilesRepository filesRepository;
    final private String FOLDER_PATH = "/Users/gbymacminim1/Desktop/Kia/uploads/";
    final private String HOST_PATH = "http://localhost:8080/storage/";

    final private List<String> FILE_SUPPORT = Arrays.asList("image/jpeg", "image/png", "image/jpg", "application/octet-stream");


    @Deprecated
    public String getExtensionByStringHandling(String filename) {
        Optional<String> newFile = Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
        return newFile.orElse("");
    }


    public String GenerateName(String fileName) {
        return UUID.randomUUID() + "-" + fileName;
    }

    public StorageService(FilesRepository filesRepository) {
        this.filesRepository = filesRepository;
    }

    public Files uploadImageToFileSystem(MultipartFile file) throws IOException, BaseException {

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
        Files imageDataSave = Files.builder().name(newName).type(file.getContentType()).path(filePath).fullPath(fullPath).build();
        file.transferTo(new File(filePath));
        return imageDataSave;
    }

    public Files UploadLargeFile(HttpServletRequest request) throws IOException, BaseException {
        boolean isMultipart = JakartaServletFileUpload.isMultipartContent(request);
        DiskFileItemFactory factory = DiskFileItemFactory.builder().get();
        JakartaServletFileUpload upload = new JakartaServletFileUpload(factory);

        List items = upload.parseRequest(request);
        if (items.isEmpty()) {
            throw FileException.FileIsNull();
        }
        Iterator iter = items.iterator();
        String contentType = null;
        String filePath = null;
        String fileName = null;
        while (iter.hasNext()) {

            FileItem item = (FileItem) iter.next();

            // Validate File
            contentType = item.getContentType();
            if (contentType == null) {
                throw FileException.FileNotSupport();
            }
            if (!contentType.contains(contentType)) {
                throw FileException.FileNotSupport();
            }
            if (!item.isFormField()) {
                fileName = UUID.randomUUID() + "-" + item.getName();
                filePath = FOLDER_PATH + fileName;
                try (
                        InputStream uploadedStream = item.getInputStream();
                        OutputStream out = new FileOutputStream(filePath);
                ) {
                    IOUtils.copy(uploadedStream, out);
                }
            }
        }
        String fullPath = HOST_PATH + fileName;
        Files fileSave = Files.builder().name(fileName).type(contentType).path(filePath).fullPath(fullPath).build();
        filesRepository.save(fileSave);
        return fileSave;

    }

    public boolean removeFile(Long file_id) {
        Optional<Files> imageDataOpt = filesRepository.findById(file_id);
        if (imageDataOpt.isEmpty()) {
            throw FileException.FileNotFound();
        }
        Files imageData = imageDataOpt.get();
        File fileToDelete = new File(imageData.getPath());
        return fileToDelete.delete();

    }

    public Map<String,Object> downloadImageFromFileSystem(String fileName) throws BaseException, IOException {
        Optional<Files> files = filesRepository.findByName(fileName);
        if (files.isPresent()) {
            String filePath = files.get().getPath();
            Map<String,Object> map = new HashMap<>();
            byte[] bytes = java.nio.file.Files.readAllBytes(new File(filePath).toPath());
            map.put("ContentType",files.get().getType());
            map.put("Bytes",bytes);
            return map;
        }
        throw FileException.FileNotFound();
    }


}

