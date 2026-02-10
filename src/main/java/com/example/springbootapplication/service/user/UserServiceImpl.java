package com.example.springbootapplication.service.user;

import com.example.springbootapplication.dto.user.UserRegistrationRequestDto;
import com.example.springbootapplication.dto.user.UserResponseDto;
import com.example.springbootapplication.exception.EntityNotFoundException;
import com.example.springbootapplication.exception.RegistrationException;
import com.example.springbootapplication.mapper.UserMapper;
import com.example.springbootapplication.model.role.Role;
import com.example.springbootapplication.model.role.RoleName;
import com.example.springbootapplication.model.shoppingcart.ShoppingCart;
import com.example.springbootapplication.model.user.User;
import com.example.springbootapplication.repository.RoleRepository;
import com.example.springbootapplication.repository.UserRepository;
import com.example.springbootapplication.repository.shoppingcart.ShoppingCartRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto request)
            throws RegistrationException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RegistrationException("Email already in use: " + request.getEmail());
        }
        User user = userMapper.toModel(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new EntityNotFoundException("Can't find role by name: "
                        + RoleName.ROLE_USER));
        user.setRoles(Set.of(userRole));
        User savedUser = userRepository.save(user);
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(savedUser);
        shoppingCartRepository.save(shoppingCart);
        return userMapper.toUserResponse(savedUser);
    }
}
