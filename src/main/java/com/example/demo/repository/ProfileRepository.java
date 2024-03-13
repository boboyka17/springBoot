package com.example.demo.repository;

import com.example.demo.model.Profile;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {
    List<Profile> findAllByUserId(Integer user_id);
}
