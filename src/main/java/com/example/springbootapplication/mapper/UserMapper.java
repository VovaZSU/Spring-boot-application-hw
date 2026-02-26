package com.example.springbootapplication.mapper;

import com.example.springbootapplication.config.MapperConfig;
import com.example.springbootapplication.dto.user.UserRegistrationRequestDto;
import com.example.springbootapplication.dto.user.UserResponseDto;
import com.example.springbootapplication.model.user.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserResponseDto toUserResponse(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
