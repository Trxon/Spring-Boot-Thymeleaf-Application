package com.example.hotelbooking.exception;

public class HotelAlreadyExistsException extends RuntimeException{

    public HotelAlreadyExistsException(String message) {
        super(message);
    }
}
