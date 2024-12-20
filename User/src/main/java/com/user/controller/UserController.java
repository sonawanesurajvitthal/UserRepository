package com.user.controller;

import com.user.constants.ApiConstants;
import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.response.AppResponse;
import com.user.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(ApiConstants.USER_CONTROLLER)
@OpenAPIDefinition(
        info = @Info(
                title = "USER API",
                description = "User API for mange user details like create, delete, fetch, etc ",
                version = "v1"
        )
)
public class UserController {

    @Autowired
    private UserService userService;

//    @Operation(
//            tags = "GET USER",
//            description = "Get User by using user id.",
//            responses = {
//                    @ApiResponse(
//                            description = "Success",
//                            responseCode = "200"
//                    ),
//                    @ApiResponse(
//                            description = "Data Not Found",
//                            responseCode = "404"
//                    )
//            }
//    )
//    @GetMapping("/{id}")
//    public ResponseEntity<AppResponse<UserDTO>> getUser(@PathVariable int id){
//        AppResponse<UserDTO> response =
//                new AppResponse<>(ApiConstants.USER_FETCH,
//                        200,
//                        userService.getUserById(id));
//        return new  ResponseEntity<>(response, HttpStatus.OK);
//    }

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
    public ResponseEntity<AppResponse<UserResponseDto>> saveUser(@ModelAttribute CreateUserRequestDto createUserRequestDto,
                                                                 @RequestParam("image") MultipartFile file) throws IOException {
        return new  ResponseEntity<>(userService.createUser(createUserRequestDto, file), HttpStatus.CREATED);
    }

//    @Operation(
//            tags = "UPDATE USER",
//            description = "Update User.",
//            responses = {
//                    @ApiResponse(
//                            description = "Success",
//                            responseCode = "200"
//                    ),
//                    @ApiResponse(
//                            description = "Bad Request",
//                            responseCode = "400"
//                    )
//            }
//    )
//    @PutMapping
//    public ResponseEntity<AppResponse<UserDTO>> updateUser(@RequestBody UserDTO userDTO){
//        AppResponse<UserDTO> response =
//                new AppResponse<>(ApiConstants.USER_UPDATE,
//                200,
//                        userService.updateUser(userDTO));
//        return new  ResponseEntity<>(response, HttpStatus.OK);
//    }
//
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
    public ResponseEntity<AppResponse<List<UserResponseDto>>> listUser(){
        return new  ResponseEntity<>(userService.listOfUser(), HttpStatus.OK);
    }
//
//    @Operation(
//            tags = "DELETE USER",
//            description = "Delete User by using user id.",
//            responses = {
//                    @ApiResponse(
//                            description = "Deleted",
//                            responseCode = "203"
//                    ),
//                    @ApiResponse(
//                            description = "Data Not Found",
//                            responseCode = "404"
//                    )
//            }
//    )
//    @DeleteMapping("/{id}")
//    public ResponseEntity<AppResponse<String>> deleteUser(@PathVariable int id){
//        userService.deleteUserById(id);
//        AppResponse<String> response =
//                new AppResponse<>(ApiConstants.DELETE_SUCCESSFULLY,
//                        203,
//                        "No content");
//        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
//    }

}
