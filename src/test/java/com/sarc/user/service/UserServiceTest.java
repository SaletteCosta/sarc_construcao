package com.sarc.user.service;

import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.UserRepository;
import com.sarc.user.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.STUDENT);
        testUser.setPassHash("default");

        testUserDTO = new UserDTO();
        testUserDTO.setName("Test User");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setRole(Role.STUDENT);
    }

    @Test
    void getAll_ShouldReturnAllUsers() {
        User user2 = new User();
        user2.setUserId(2L);
        user2.setName("Another User");
        user2.setEmail("another@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        List<User> result = userService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Test User");
        assertThat(result.get(1).getName()).isEqualTo("Another User");
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        User result = userService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test User");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID 1 not found");

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void create_ShouldCreateUser_WhenDtoIsValid() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.create(testUserDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenEmailIsNull() {
        testUserDTO.setEmail(null);

        assertThatThrownBy(() -> userService.create(testUserDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email cannot be empty");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenEmailIsBlank() {
        testUserDTO.setEmail("   ");

        assertThatThrownBy(() -> userService.create(testUserDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email cannot be empty");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void delete_ShouldDeleteUser_WhenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.delete(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> userService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID 1 not found");

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, never()).deleteById(1L);
    }
}
