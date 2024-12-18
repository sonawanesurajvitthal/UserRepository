package com.user.service;

import com.user.constants.ApiConstants;
import com.user.entity.User;
import com.user.exception.UserIdNotFoundException;
import com.user.exception.UserRequiredFields;
import com.user.reposiotry.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public User createUser(User user, MultipartFile file) throws IOException {
        user.setFile(file.getBytes());
        return userRepository.save(user);
    }

    @Override
    public List<User> listOfUser() {

        return userRepository.findAll();
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+id));
    }

    @Override
    public User updateUser(User user) {
        User tempUser = userRepository.findById(user.getId())
                .orElseThrow(()->new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+user.getId()));
        if(user.getName() == null){
            user.setName(tempUser.getName());
        }
        if(user.getAddress() == null){
            user.setAddress(tempUser.getAddress());
        }
        if(user.getPhone() == null){
            user.setPhone(tempUser.getPhone());
        }
        if(user.getEmail() == null){
            user.setEmail(tempUser.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.findById(id)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+id));
        userRepository.deleteById(id);
    }
}
