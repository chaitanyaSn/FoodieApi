package com.spring.foodapi.service;

import com.spring.foodapi.io.UserReponse;
import com.spring.foodapi.io.UserRequest;

public interface UserService {

    UserReponse registerUser(UserRequest userRequest);

    String findByUserId();
}
