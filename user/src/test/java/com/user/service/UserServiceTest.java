package com.user.service;

import com.user.dto.UserDTO;
import com.user.entity.User;
import com.user.enums.UserType;
import com.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setRegistration("202301234");
        user.setType(UserType.STUDENT);

        userDTO = new UserDTO(null, "John Doe", "202301234", "STUDENT");
    }

    @Test
    void testCreateUser() {
        when(userRepository.existsByRegistration(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("202301234", result.getRegistration());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_AlreadyExists() {
        when(userRepository.existsByRegistration("202301234")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByRegistration() {
        when(userRepository.findByRegistration("202301234")).thenReturn(Optional.of(user));

        UserDTO result = userService.getUserByRegistration("202301234");

        assertNotNull(result);
        assertEquals("202301234", result.getRegistration());
    }

    @Test
    void testGetUserByRegistration_NotFound() {
        when(userRepository.findByRegistration("999999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByRegistration("999999"));
    }

    @Test
    void testGetUsersByName() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(users);

        List<UserDTO> result = userService.getUsersByName("John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
