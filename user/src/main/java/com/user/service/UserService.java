package com.user.service;

import com.user.dto.UserDTO;
import com.user.entity.User;
import com.user.enums.UserType;
import com.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.existsByRegistration(userDTO.getRegistration())) {
            throw new RuntimeException("User with registration " + userDTO.getRegistration() + " already exists");
        }
        
        User user = new User();
        user.setName(userDTO.getName());
        user.setRegistration(userDTO.getRegistration());
        user.setType(userDTO.getType() != null ? UserType.valueOf(userDTO.getType()) : UserType.STUDENT);
        
        User saved = userRepository.save(user);
        return toDTO(saved);
    }
    
    public UserDTO getUserByRegistration(String registration) {
        User user = userRepository.findByRegistration(registration)
            .orElseThrow(() -> new RuntimeException("User not found with registration: " + registration));
        return toDTO(user);
    }
    
    public List<UserDTO> getUsersByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<UserDTO> getUsersByType(String type) {
        UserType userType = UserType.valueOf(type.toUpperCase());
        return userRepository.findByType(userType)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        user.setName(userDTO.getName());
        if (userDTO.getType() != null) {
            user.setType(UserType.valueOf(userDTO.getType()));
        }
        User updated = userRepository.save(user);
        return toDTO(updated);
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(user.getId(), user.getName(), user.getRegistration(), user.getType().name());
    }
}
