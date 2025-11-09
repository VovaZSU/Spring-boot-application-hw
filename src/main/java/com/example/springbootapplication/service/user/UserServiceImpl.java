package com.example.springbootapplication.service.user;

import com.example.springbootapplication.dto.user.UserRegistrationRequestDto;
import com.example.springbootapplication.dto.user.UserResponseDto;
import com.example.springbootapplication.exception.RegistrationException;
import com.example.springbootapplication.mapper.UserMapper;
import com.example.springbootapplication.model.User;
import com.example.springbootapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Email already in use: " + requestDto.getEmail());
        }
        User user = userMapper.toModel(requestDto);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
