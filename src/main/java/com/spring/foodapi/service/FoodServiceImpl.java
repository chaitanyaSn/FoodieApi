package com.spring.foodapi.service;

import com.spring.foodapi.entity.FoodEntity;
import com.spring.foodapi.io.FoodRequest;
import com.spring.foodapi.io.FoodResponse;
import com.spring.foodapi.repository.FoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService {
    @Autowired
    private  S3Client s3Client;
    @Autowired
    private  FoodRepository foodRepository;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileNameExtension=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1);
        String key=UUID.randomUUID().toString()+"."+fileNameExtension;
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("myfood-full")
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();
            PutObjectResponse response=s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            if(response.sdkHttpResponse().isSuccessful()){
                return "https://myfood-full.s3.amazonaws.com/"+key;
            }else{
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the actual error
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload file: " + e.getMessage());
        }

    }

    @Override
    public FoodResponse addFood(FoodRequest request, MultipartFile file) {
      FoodEntity newFood=convertToEntity(request);
      String imageUrl=uploadFile(file);
      newFood.setImageUrl(imageUrl);
      newFood=foodRepository.save(newFood);
      return convertToResponse(newFood);

    }

    @Override
    public List<FoodResponse> getFoods() {
        List<FoodEntity> foodEntries=foodRepository.findAll();
        return foodEntries.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public FoodResponse readFood(String id) {
        FoodEntity existingFood=foodRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));
        return convertToResponse(existingFood);
    }

    @Override
    public boolean deleteFile(String fileName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket("myfood-full")
                .key(fileName)
                .build();
        s3Client.deleteObject(deleteObjectRequest);
        return true;
    }

    @Override
    public void deleteFood(String id) {
        FoodResponse response=readFood(id);
        String imageUrl=response.getImageUrl();
        String fileName=imageUrl.substring(imageUrl.lastIndexOf("/")+1);
        boolean isDeleted=deleteFile(fileName);
        if(isDeleted) {
            foodRepository.deleteById(response.getId());
        }
    }


    private FoodEntity convertToEntity(FoodRequest request){
        return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

    }
    private FoodResponse convertToResponse(FoodEntity entity){
        return FoodResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .category(entity.getCategory())
                .imageUrl(entity.getImageUrl())
                .build();
    }

}
