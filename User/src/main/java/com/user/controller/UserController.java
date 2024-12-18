package com.user.controller;

import com.user.constants.ApiConstants;
import com.user.dto.UserDTO;
import com.user.entity.User;
import com.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.USER_CONTROLLER)
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(
            tags = "GET USER",
            description = "Get User by using user id.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Data Not Found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable int id){
        UserDTO userDTO = new UserDTO().convertUserToDTO(userService.getUserById(id));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Operation(
            tags = "CREATE USER",
            description = "Create User.",
            responses = {
                    @ApiResponse(
                            description = "Created",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<UserDTO> saveUser(@ModelAttribute User user, @RequestParam("file") MultipartFile file) throws IOException {


        UserDTO userDTO = new UserDTO().convertUserToDTO(userService.createUser(user, file));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Operation(
            tags = "UPDATE USER",
            description = "Update User.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Bad Request",
                            responseCode = "400"
                    )
            }
    )
    @PutMapping
    public ResponseEntity<UserDTO> updateUser(@RequestBody User user){
        UserDTO userDTO = new UserDTO().convertUserToDTO(userService.updateUser(user));
        return ResponseEntity.status(HttpStatus.OK).body(userDTO);
    }

    @Operation(
            tags = "GET ALL USERS",
            description = "Get All Users.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),

            }
    )
    @GetMapping
    public ResponseEntity<List<UserDTO>> listUser(){
        List<UserDTO> listUserDTO = new ArrayList<>();
        List<User> list =userService.listOfUser();
        for(User user: list){
            listUserDTO.add(new UserDTO().convertUserToDTO(user));
        }
        return ResponseEntity.status(HttpStatus.OK).body(listUserDTO);
    }

    @Operation(
            tags = "DELETE USER",
            description = "Delete User by using user id.",
            responses = {
                    @ApiResponse(
                            description = "Deleted",
                            responseCode = "203"
                    ),
                    @ApiResponse(
                            description = "Data Not Found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable int id){
        userService.deleteUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiConstants.DELETE_SUCCESSFULLY);
    }

}
