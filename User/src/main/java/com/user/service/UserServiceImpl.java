package com.user.service;

import com.user.constants.ApiConstants;
import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.dto.UserMapperService;
import com.user.entity.User;
import com.user.exception.UserIdNotFoundException;
import com.user.exteranl.LoanService;
import com.user.repository.UserRepository;
import com.user.response.AppResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${upload.image.url}")
    private String uploadUrl;

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    private final LoanService loanService;


    private final UserMapperService userMapperService;

    public UserServiceImpl(UserMapperService userMapperService, LoanService loanService, UserRepository userRepository){
        this.userMapperService = userMapperService;
        this.loanService = loanService;
        this.userRepository = userRepository;
    }


    /**
     * Creates a new User in the database.
     *
     * @param createUserRequestDto and file The user object to be created.
     * @return The created user object into UserResponseDto.
     */
    @Override
    public AppResponse<UserResponseDto> createUser(CreateUserRequestDto createUserRequestDto, MultipartFile file) throws IOException {
        User user = userMapperService.getUser(createUserRequestDto);
        try {
            Path uploadPath = Paths.get(uploadUrl);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            user.setImagesName(fileName);
            log.info("Image Upload Successfully {}", fileName);
        } catch (IOException e) {
            log.error("e: ", e);
        }
        user.setActive(true);
        UserResponseDto userResponseDto = userMapperService.getResponseDto(userRepository.save(user));
        return  new AppResponse<>(ApiConstants.USER_CREATED, 201, userResponseDto);
    }

    /**
     * Fetches all users from the database.
     *
     * @return A list of all users.
     */
    @Override
    public AppResponse<List<UserResponseDto>> listOfUser() {

        List<User> userList = userRepository.findAll().stream().toList();
        List<UserResponseDto> listOfUserDTO = userList.stream().map(userMapperService::getResponseDto).toList();
        return new AppResponse<>(ApiConstants.ALL_USER_FETCH, 200, listOfUserDTO);
    }

    /**
     * Retrieves a user by their ID.
     * Throws an exception if the user is not found.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user object if found.
     * @throws UserIdNotFoundException if no user is found with the given ID.
     */
    @Override
    public AppResponse<UserResponseDto> getUserById(int userId) {
        User user = userRepository.findById(userId).filter(User::isActive)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+userId));
        user.setLoans(loanService.listOfLoansByUserId(user.getId()));
        UserResponseDto userResponseDto = userMapperService.getResponseDto(user);
        return new AppResponse<>(ApiConstants.USER_FETCH, 200, userResponseDto);
    }

    /**
     * Updates the details of an existing user.
     * If the user does not exist, an exception will be thrown.
     *
     * @param userId The ID of the user to update.
     * @param createUserRequestDto The user object with updated details.
     * @return The updated user object.
     * @throws UserIdNotFoundException if no user is found and not active with the given ID.
     */
    @Override
    public AppResponse<UserResponseDto> updateUser(int userId, CreateUserRequestDto createUserRequestDto) {
        User user = userMapperService.getUser(createUserRequestDto);
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
        if(user.getImagesName() == null){
            user.setImagesName(tempUser.getImagesName());
        }
        UserResponseDto userResponseDto = userMapperService.getResponseDto(userRepository.save(user));

        return new AppResponse<>(ApiConstants.USER_UPDATE, 200, userResponseDto);
    }

    /**
     * Deletes a user from the database(soft delete).
     * Throws an exception if the user does not exist.
     *
     * @param userId The ID of the user to delete.
     * @throws UserIdNotFoundException if no user is found and not active with the given ID.
     */
    @Override
    public AppResponse<String> deleteUserById(int userId) {
        User user = userRepository.findById(userId).filter(User::isActive)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+userId));
        user.setActive(false);
        userRepository.save(user);
        return new AppResponse<>(ApiConstants.DELETE_SUCCESSFULLY, 203, "Deleted Successfully");
    }

    //Practice code for test
    public Page<User> userListOnPagination(int page, int size){
        userRepository.findAll(PageRequest.of(page, size));
        return userRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Resource getImage(String name){
        File file = new File(uploadUrl +ApiConstants.SLASH+ name);
        return  new FileSystemResource(file);
    }
}
