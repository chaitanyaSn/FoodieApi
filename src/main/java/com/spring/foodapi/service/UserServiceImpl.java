package com.spring.foodapi.service;

import com.spring.foodapi.entity.UserEntity;
import com.spring.foodapi.io.UserReponse;
import com.spring.foodapi.io.UserRequest;
import com.spring.foodapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private  UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserReponse registerUser(UserRequest userRequest) {
        if(userRepository.existsByEmail(userRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        UserEntity newUser= convertToEntity(userRequest);
        newUser=userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
        String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
        UserEntity loggedInUser = userRepository.findByEmail(loggedInUserEmail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return loggedInUser.getId();
    }


    private UserEntity convertToEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .build();
    }
    private UserReponse convertToResponse(UserEntity userEntity) {
        return UserReponse.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .email(userEntity.getEmail())
                .build();
    }
}
