package com.spring.foodapi.service;

import com.spring.foodapi.io.FoodRequest;
import com.spring.foodapi.io.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoodService {

    String uploadFile(MultipartFile file);

    FoodResponse addFood(FoodRequest foodRequest,MultipartFile file);

    List<FoodResponse> getFoods();

    FoodResponse readFood(String id);

    boolean deleteFile(String fileName);

    void deleteFood(String id);
}
