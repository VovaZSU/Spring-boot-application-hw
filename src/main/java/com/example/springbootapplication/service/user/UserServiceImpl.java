package com.example.springbootapplication.service.user;

import com.example.springbootapplication.dto.user.UserRegistrationRequestDto;
import com.example.springbootapplication.dto.user.UserResponseDto;
import com.example.springbootapplication.exception.RegistrationException;
import com.example.springbootapplication.mapper.UserMapper;
import com.example.springbootapplication.model.Role;
import com.example.springbootapplication.model.RoleName;
import com.example.springbootapplication.model.User;
import com.example.springbootapplication.repository.RoleRepository;
import com.example.springbootapplication.repository.UserRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto getByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toUserResponse)
                .orElseThrow(() -> new RuntimeException("Can't find user"));
    }

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RegistrationException("Email already in use: " + request.getEmail());
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new RegistrationException("Can't find role by name: "
                        + RoleName.ROLE_USER));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        return userMapper.toUserResponse(savedUser);
    }
}
