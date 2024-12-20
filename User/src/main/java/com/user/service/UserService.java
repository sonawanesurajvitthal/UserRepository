package com.user.service;

import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.response.AppResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

     AppResponse<UserResponseDto> createUser(CreateUserRequestDto createUserRequestDto, MultipartFile file) throws IOException;

     AppResponse<List<UserResponseDto>> listOfUser();
//
//     UserDTO getUserById(int id);
//
//     UserDTO updateUser(UserDTO user);
//
//     void deleteUserById(int id);
}
