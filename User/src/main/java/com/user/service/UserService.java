package com.user.service;

import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.entity.User;
import com.user.response.AppResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

     AppResponse<UserResponseDto> createUser(CreateUserRequestDto createUserRequestDto, MultipartFile file) throws IOException;

     AppResponse<List<UserResponseDto>> listOfUser();

     AppResponse<UserResponseDto> getUserById(int userId);

     AppResponse<UserResponseDto> updateUser(int userId, CreateUserRequestDto createUserRequestDto);

     AppResponse<String> deleteUserById(int userId);

     Page<User> userListOnPagination(int page, int size);

     Resource getImage(String imName);
}
