package com.user.service;

import com.user.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public User createUser(User user, MultipartFile file) throws IOException;

    public List<User> listOfUser();

    public User getUserById(int id);

    public User updateUser(User user);

    public void deleteUserById(int id);
}
