package com.spring.foodapi.service;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


public interface AuthenticationFacade {
    Authentication getAuthentication();
}
