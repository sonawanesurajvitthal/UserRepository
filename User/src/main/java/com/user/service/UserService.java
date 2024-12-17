package com.user.service;

import com.user.entity.User;

import java.util.List;

public interface UserService {

    public User createUser(User user);

    public List<User> listOfUser();

    public User getUserById(int id);

    public User updateUser(User user);

    public void deleteUserById(int id);
}
