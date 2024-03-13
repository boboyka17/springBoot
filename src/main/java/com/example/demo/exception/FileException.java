package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class FileException extends BaseException{


    public FileException(HttpStatus status, String message ) {
        super(status,message);
    }

    public static FileException FileNotFound(){
        return new FileException(HttpStatus.NOT_FOUND,"File Not Found");
    }

    public static FileException FileIsNull(){
        return new FileException(HttpStatus.BAD_REQUEST,"File is Null");
    }

    public static FileException FileNotSupport(){
        return new FileException(HttpStatus.BAD_REQUEST,"File Not Support");

    }

}
