package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ProfileException extends BaseException{


    public ProfileException(HttpStatus status, String message ) {
        super(status,message);
    }
    public static ProfileException ProfileIdIsEmpty(){
        return new ProfileException(HttpStatus.BAD_REQUEST,"ProfileId is empty");
    }


    public static ProfileException ProfileNotFound(){
        return new ProfileException(HttpStatus.NOT_FOUND,"Profile Not Found");
    }


}
