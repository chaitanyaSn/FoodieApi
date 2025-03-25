package com.spring.foodapi.controller;


import com.spring.foodapi.io.UserReponse;
import com.spring.foodapi.io.UserRequest;
import com.spring.foodapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserReponse register(@RequestBody UserRequest userRequest) {
         return userService.registerUser(userRequest);

    }
}
