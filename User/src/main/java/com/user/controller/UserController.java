package com.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.constants.ApiConstants;
import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.entity.User;
import com.user.response.AppResponse;
import com.user.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    private final UserService userService;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Handles the HTTP GET request to get a user by its ID.
     *
     * @param userId The ID of the user to be fetched.
     * @return ResponseEntity containing the fetched User and the HTTP status code.
     */
    @Operation(
            tags = "GET USER",
            description = "Get User.",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),

            }
    )
    @GetMapping(ApiConstants.SLASH_ID)
    public ResponseEntity<AppResponse<UserResponseDto>> getUserById(@PathVariable("id") int userId){
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * Handles the HTTP POST request to create a new user.
     *
     * This method takes a User object from the request body, passes it to the UserService,
     * and returns the created user along with an HTTP 201 Created status.
     *
     * @param createUserRequestDto The User object to be created.
     * @param file The image/file of User.
     * @return ResponseEntity containing the created User and the HTTP status code.
     */
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
    public ResponseEntity<AppResponse<UserResponseDto>> saveUser(@RequestParam("user")String userRequestDto,
                                                                 @RequestParam( value = "file", required = false) MultipartFile file) throws IOException {
        CreateUserRequestDto createUserRequestDto = new ObjectMapper().readValue(userRequestDto, CreateUserRequestDto.class);
        return new  ResponseEntity<>(userService.createUser(createUserRequestDto, file), HttpStatus.CREATED);
    }

    /**
     * Handles the HTTP PUT request to update an existing user's data.
     *
     * @param userId The ID of the user to be updated.
     * @param  userRequestDto The user object containing updated data.
     * @return ResponseEntity containing the updated user data or 404 if not found.
     */
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
    @PutMapping(ApiConstants.SLASH_ID)
    public ResponseEntity<AppResponse<UserResponseDto>> updateUser(@PathVariable("id") int userId,
                                                                   @RequestParam("user") String userRequestDto,
                                                                   @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        CreateUserRequestDto createUserRequestDto = new ObjectMapper().readValue(userRequestDto, CreateUserRequestDto.class);
        return new  ResponseEntity<>(userService.updateUser(userId, createUserRequestDto, file), HttpStatus.OK);
    }

    /**
     * Handles the HTTP GET request to retrieve all users.
     *
     * This method calls the UserService to fetch all users from the database and
     * returns the list of users along with an HTTP 200 OK status.
     *
     * @return ResponseEntity containing the list of all users and HTTP status 200.
     */
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
    @GetMapping(ApiConstants.USER_LIST)
    public ResponseEntity<AppResponse<List<UserResponseDto>>> listUser(){
        return new  ResponseEntity<>(userService.listOfUser(), HttpStatus.OK);
    }

    /**
     * Handles the HTTP DELETE request to delete a user by its ID.
     *
     * @param userId The ID of the user to be deleted (extracted from the URL).
     * @return ResponseEntity with HTTP status indicating the result of the deletion operation.
     */
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
    @DeleteMapping(ApiConstants.SLASH_ID)
    public ResponseEntity<AppResponse<String>> deleteUser(@PathVariable("id") int userId){
        AppResponse<String> response = userService.deleteUserById(userId);
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @Operation(
            tags = "PAGE LIST OF USER",
            description = "Fetch user by page and size.",
            responses = {
                    @ApiResponse(
                            description = "Fetched",
                            responseCode = "200"
                    )
            }
    )


    // Practice code
    @GetMapping(ApiConstants.PAGE_USER_LIST)
    public ResponseEntity<AppResponse<Page<User>>> listByPagination(@RequestParam( value = "page", defaultValue = "1") int page,
                                                                    @RequestParam( value = "size", defaultValue = "10") int size){
        return new ResponseEntity<>(userService.userListOnPagination(page,size), HttpStatus.OK);
    }


    // Endpoint to fetch the image by filename
    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getImage(@PathVariable("imageName") String imageName) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // Or the appropriate media type
                .body(userService.getImage(imageName));
    }

    @GetMapping(ApiConstants.SORT_USER_LIST)
    public ResponseEntity<AppResponse<List<UserResponseDto>>> sortUsers(@PathVariable("field") String field){
        return new ResponseEntity<>(userService.sortUsersByField(field), HttpStatus.OK);
    }

}
