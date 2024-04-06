package com.example.demo.repository;

import com.example.demo.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilesRepository extends JpaRepository<Files,Long> {
    Optional<Files> findByName(String fileName);
}
