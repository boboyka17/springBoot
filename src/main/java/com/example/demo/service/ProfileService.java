package com.example.demo.service;

import com.example.demo.Response.CommonResponse;
import com.example.demo.exception.BaseException;
import com.example.demo.exception.ProfileException;
import com.example.demo.model.ImageData;
import com.example.demo.model.Profile;
import com.example.demo.model.User;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private  final AuthService authService;
    private final ProfileRepository profileRepository;
    private final StorageService service;
    private  final UserRepository userRepository;
    private  final  Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    private Integer getUserId(){
        User user =  (User) authentication.getPrincipal();
        return user.getId();
    }
    public ProfileService(AuthService authService, ProfileRepository repository, StorageService service, UserRepository userRepository) {
        this.authService = authService;
        this.profileRepository = repository;
        this.service = service;
        this.userRepository = userRepository;
    }

    public CommonResponse createProfile(MultipartFile file, String fullname,String detail) throws IOException {
        User userAuth = authService.getInfo();
        Optional<User> user = userRepository.findById(userAuth.getId());
        ImageData imageData =  service.uploadImageToFileSystem(file);
        Profile profile = new Profile();
        profile.setFullname(fullname);
        profile.setDetail(detail);
        user.ifPresent(profile::setUser);
        profile.setImageData(imageData);
        profileRepository.save(profile);
        return new CommonResponse("Create profile successfully");
    }

    public Profile updateProfile(Integer profileId,MultipartFile file, String fullname,String detail) throws IOException {
        Profile profile = profileRepository.findById(profileId).orElseThrow(ProfileException::ProfileNotFound);
        profile.setFullname(fullname);
        profile.setDetail(detail);
        if (file != null){
            service.removeFile(profile.getImageData().getId());
            ImageData imageData =  service.uploadImageToFileSystem(file);
            profile.setImageData(imageData);
        }
        profileRepository.save(profile);
        return profile;
    }
    public CommonResponse removeProfile(Integer profileId) throws BaseException {
        if(profileId == null){
            throw  ProfileException.ProfileIdIsEmpty();
        }
        Optional<Profile> profileOpt = profileRepository.findById(profileId);
        if(profileOpt.isEmpty()){
            throw  ProfileException.ProfileNotFound();
        }
        Profile profile = profileOpt.get();
        service.removeFile(profile.getImageData().getId());
        profileRepository.delete(profile);
        return new CommonResponse("Delete Profile Success");
    }

    public List<Profile> getProfile(){
        return profileRepository.findAllByUserId(getUserId());
    }
    public Profile getProfileOne(Integer profile_id){
        return profileRepository.findById(profile_id).orElseThrow(ProfileException::ProfileNotFound);
    }
}
