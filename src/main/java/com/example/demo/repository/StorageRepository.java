package com.example.demo.repository;

import com.example.demo.model.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<ImageData,Long> {
}
