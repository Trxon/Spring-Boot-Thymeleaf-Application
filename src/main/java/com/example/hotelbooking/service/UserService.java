package com.example.hotelbooking.service;


import com.example.hotelbooking.dto.UserDto;
import com.example.hotelbooking.model.User;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
}
