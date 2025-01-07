package com.user.service;

import com.user.constants.ApiConstants;
import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.entity.Loan;
import com.user.exception.FileUploadException;
import com.user.exception.PageNumberException;
import com.user.exception.UserRequiredFields;
import com.user.exteranl.LoanClient;
import com.user.mapper.UserMapperImpl;
import com.user.entity.User;
import com.user.exception.UserIdNotFoundException;
import com.user.repository.UserRepository;
import com.user.response.AppResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Value("${upload.image.url}")
    private String uploadUrl;

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;


    private final UserMapperImpl userMapperImpl;

    private final LoanClient loanClient;

    public UserServiceImpl(UserRepository userRepository,                           UserMapperImpl userMapperImpl, LoanClient loanClient){
        this.userRepository = userRepository;
        this.userMapperImpl = userMapperImpl;
        this.loanClient = loanClient;
    }


    /**
     * Creates a new User in the database.
     *
     * @param createUserRequestDto and file The user object to be created.
     * @return The created user object into UserResponseDto.
     */
    @Override
    public AppResponse<UserResponseDto> createUser(CreateUserRequestDto createUserRequestDto, MultipartFile file) throws IOException {
        if(file != null) {
            try {
                Path uploadPath = Paths.get(uploadUrl);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String fileName = file.getOriginalFilename();
                if (fileName != null) {
                    Path filePath = uploadPath.resolve(fileName);
                    file.transferTo(filePath.toFile());
                    createUserRequestDto.setImagesName(fileName);
                }
            } catch (IOException e) {
                log.error("e: ", e);
                throw new FileUploadException(ApiConstants.UPLOAD_FILE_FAILED);
            }
        }
        if(!createUserRequestDto.isActive()){
            createUserRequestDto.setActive(true);
        }
        User user = userRepository.save(userMapperImpl.requestToUser(createUserRequestDto));
        if(user.getLoans() != null ) {
            user.setLoans(user.getLoans().stream().map(loan -> {
                loan.setUserId(user.getId());
                return Objects.requireNonNull(loanClient.createLoan(loan).getBody()).getData();
            }).toList());
        }
        UserResponseDto userResponseDto = userMapperImpl.userToResponse(user);
        return  new AppResponse<>(ApiConstants.USER_CREATED, 201, userResponseDto);
    }

    /**
     * Fetches all users from the database.
     *
     * @return A list of all users.
     */
    @Override
    public AppResponse<List<UserResponseDto>> listOfUser() {
        List<User> userList = userRepository.findAll().stream().filter(User::isActive)
                .map(user -> {
                    user.setLoans(Objects.requireNonNull(loanClient.findLoanByUserId(user.getId()).getBody()).getData());
                    return user;
                })
                .toList();
        List<UserResponseDto> listOfUserDTO = userList.stream().map(userMapperImpl::userToResponse).toList();
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
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND + userId));
        if(!user.isActive()){
            throw new UserIdNotFoundException(ApiConstants.USER_NOT_ACTIVATED);
        }
        user.setLoans(Objects.requireNonNull(loanClient.findLoanByUserId(user.getId()).getBody()).getData());
        UserResponseDto userResponseDto = userMapperImpl.userToResponse(user);
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
    public AppResponse<UserResponseDto> updateUser(int userId, CreateUserRequestDto createUserRequestDto, MultipartFile file) {
        User tempUser = userRepository.findById(userId)
                .orElseThrow(()->new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND + userId));
        createUserRequestDto.setId(userId);
        if(createUserRequestDto.getName() == null){
            createUserRequestDto.setName(tempUser.getName());
        }
        if(createUserRequestDto.getAddress() == null){
            createUserRequestDto.setAddress(tempUser.getAddress());
        }
        if(createUserRequestDto.getPhone() == null){
            createUserRequestDto.setPhone(tempUser.getPhone());
        }
        if(createUserRequestDto.getEmail() == null){
            createUserRequestDto.setEmail(tempUser.getEmail());
        }
        if(createUserRequestDto.getImagesName() == null){
            createUserRequestDto.setImagesName(tempUser.getImagesName());
        } else if(file != null) {
            try {
                Path uploadPath = Paths.get(uploadUrl);
                String fileName = file.getOriginalFilename();
                if (fileName != null) {
                    Path filePath = uploadPath.resolve(fileName);
                    file.transferTo(filePath.toFile());
                    createUserRequestDto.setImagesName(fileName);
                }
            } catch (IOException e) {
                log.error("e: ", e);
                throw new FileUploadException(ApiConstants.UPLOAD_FILE_FAILED);
            }
        }
        createUserRequestDto.setActive(tempUser.isActive());
        if(createUserRequestDto.getLoans() != null){
            createUserRequestDto.setLoans(createUserRequestDto.getLoans().stream().map(loan->{
                loan.setUserId(createUserRequestDto.getId());
                if(loan.getId() == 0){
                    return Objects.requireNonNull(loanClient.createLoan(loan).getBody()).getData();
                }
                return Objects.requireNonNull(loanClient.updateLoan(loan.getId(), loan).getBody()).getData();
            }).toList());
        }
        UserResponseDto userResponseDto = userMapperImpl.userToResponse(
                userRepository.save(
                        userMapperImpl.requestToUser(createUserRequestDto)
                ));
        userResponseDto.setLoans(createUserRequestDto.getLoans());
        return new AppResponse<>(ApiConstants.USER_UPDATE, HttpStatus.OK.value(), userResponseDto);
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
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND + userId));
        user.setActive(false);
        userRepository.save(user);


        for(Loan loan : Objects.requireNonNull(loanClient.findLoanByUserId(userId).getBody()).getData()){
            loanClient.deleteById(loan.getId());
        }
        return new AppResponse<>(ApiConstants.DELETE_SUCCESSFULLY, 204, ApiConstants.DELETE_SUCCESSFULLY);
    }

    //Practice code for test
    public AppResponse<Page<User>> userListOnPagination(int page, int size){
        if (page <= 0){
            throw new PageNumberException(ApiConstants.PAGE_NUMBER_VALIDATION);
        }
        Page<User> users = userRepository.findAll(PageRequest.of(page-1, size));
        return new AppResponse<>(ApiConstants.ALL_USER_FETCH, HttpStatus.OK.value(),users);
    }

    @Override
    public Resource getImage(String name){
        File file = new File(uploadUrl +ApiConstants.SLASH+ name);
        return  new FileSystemResource(file);
    }

    @Override
    public AppResponse<List<UserResponseDto>> sortUsersByField(String field) {
       List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, field));
       List<UserResponseDto> userResponseDtoList = users.stream().map(userMapperImpl::userToResponse).toList();
       return new AppResponse<>(ApiConstants.ALL_USER_FETCH, HttpStatus.OK.value(), userResponseDtoList);
    }
}
