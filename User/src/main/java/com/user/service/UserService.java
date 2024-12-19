package com.user.service;

import com.user.dto.UserDTO;
import com.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public UserDTO createUser(UserDTO user, MultipartFile file) throws IOException;

    public List<UserDTO> listOfUser();

    public UserDTO getUserById(int id);

    public UserDTO updateUser(UserDTO user);

    public void deleteUserById(int id);
}
