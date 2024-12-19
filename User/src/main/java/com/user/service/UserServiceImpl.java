package com.user.service;

import com.user.constants.ApiConstants;
import com.user.dto.UserDTO;
import com.user.dto.UserMapper;
import com.user.dto.UserMapperService;
import com.user.entity.User;
import com.user.exception.UserIdNotFoundException;
import com.user.exception.UserRequiredFields;
import com.user.exteranlClient.LoanService;
import com.user.reposiotry.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class UserServiceImpl implements UserService {

    private static final String UPLOAD_DIR = "C:/Users/User/Desktop/java/UserRepository/User/src/main/images";
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanService loanService;


    private final UserMapperService userMapperService;

    public UserServiceImpl(UserMapperService userMapperService){
        this.userMapperService = userMapperService;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO, MultipartFile file) throws IOException {

        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = file.getOriginalFilename();
            assert fileName != null;
            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());
            userDTO.setImagesName(fileName);
            log.info("Image Upload Successfully {}", fileName);

        } catch (IOException e) {
            log.error("e: ", e);
        }
        return userMapperService.getUserDTO(userRepository.save(userMapperService.convertToUser(userDTO)));
    }

    @Override
    public List<UserDTO> listOfUser() {

        List<User> userList = userRepository.findAll()
                .stream()
                .peek(map -> map.setLoans(loanService.listOfLoansByUserId(map.getId()))
                ).toList();
        return userList.stream().map(userMapperService::getUserDTO).toList();
    }

    @Override
    public UserDTO getUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+id));
        if(!user.isActive()){
            throw new UserRequiredFields(ApiConstants.USER_NOT_ACTIVATED);
        }
        user.setLoans(loanService.listOfLoansByUserId(user.getId()));
        return userMapperService.getUserDTO(user);
    }

    @Override
    public UserDTO updateUser(UserDTO user) {
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
        return userMapperService.getUserDTO(userRepository.save(userMapperService.convertToUser(user)));
    }

    @Override
    public void deleteUserById(int id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserIdNotFoundException(ApiConstants.USER_ID_NOT_FOUND+id));
        user.setActive(false);
        userRepository.save(user);
    }
}
