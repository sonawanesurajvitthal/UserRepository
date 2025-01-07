package com.user.mapper;

import com.user.domain.request.CreateUserRequestDto;
import com.user.domain.response.UserResponseDto;
import com.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);


    User requestToUser(CreateUserRequestDto createUserRequestDto);
    UserResponseDto userToResponse(User user);
}
