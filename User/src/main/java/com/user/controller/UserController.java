package com.user.controller;

import com.user.constants.ApiConstants;
import com.user.dto.UserDTO;
import com.user.entity.User;
import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.USER_CONTROLLER)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id){
        UserDTO userDTO = new UserDTO().convertUserToDTO(userService.getUserById(id));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@RequestBody User user){
        UserDTO userDTO = new UserDTO().convertUserToDTO(userService.createUser(user));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user){
        UserDTO userDTO = new UserDTO().convertUserToDTO(userService.updateUser(user));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUser(){
        List<UserDTO> listUserDTO = new ArrayList<>();
        List<User> list =userService.listOfUser();
        for(User user: list){
            listUserDTO.add(new UserDTO().convertUserToDTO(user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listUserDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiConstants.DELETE_SUCCESSFULLY);
    }

}
