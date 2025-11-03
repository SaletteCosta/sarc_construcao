package com.sarc.user.service;

import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.UserRepository;
import com.sarc.user.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para UserService
 * Testa toda a lógica de negócio relacionada a usuários
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        // Configurar dados de teste
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(Role.TEACHER);
        testUser.setPassHash("default");

        testUserDTO = new UserDTO();
        testUserDTO.setName("New User");
        testUserDTO.setEmail("new@example.com");
        testUserDTO.setRole(Role.STUDENT);
    }

    @Test
    @DisplayName("Deve retornar todos os usuários")
    void testGetAll_Success() {
        // Arrange
        List<User> users = Arrays.asList(testUser, new User());
        when(userRepository.findAll()).thenReturn(users);

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertThat(result).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários")
    void testGetAll_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<User> result = userService.getAll();

        // Assert
        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar usuário por ID quando existe")
    void testGetById_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getById(1L);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando usuário não existe")
    void testGetById_NotFound() {
        // Arrange
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID 999 not found");
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Deve criar usuário com dados válidos")
    void testCreate_Success() {
        // Arrange
        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setName(testUserDTO.getName());
        savedUser.setEmail(testUserDTO.getEmail());
        savedUser.setRole(testUserDTO.getRole());
        savedUser.setPassHash("default");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.create(testUserDTO);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("New User");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        assertThat(result.getRole()).isEqualTo(Role.STUDENT);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando email é nulo")
    void testCreate_NullEmail() {
        // Arrange
        testUserDTO.setEmail(null);

        // Act & Assert
        assertThatThrownBy(() -> userService.create(testUserDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email cannot be empty");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando email está vazio")
    void testCreate_BlankEmail() {
        // Arrange
        testUserDTO.setEmail("   ");

        // Act & Assert
        assertThatThrownBy(() -> userService.create(testUserDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email cannot be empty");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve criar usuário com passHash default")
    void testCreate_DefaultPassHash() {
        // Arrange
        User savedUser = new User();
        savedUser.setUserId(1L);
        savedUser.setPassHash("default");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = userService.create(testUserDTO);

        // Assert
        assertThat(result.getPassHash()).isEqualTo("default");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve deletar usuário quando existe")
    void testDelete_Success() {
        // Arrange
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // Act
        userService.delete(1L);

        // Assert
        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao deletar usuário inexistente")
    void testDelete_NotFound() {
        // Arrange
        when(userRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> userService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID 999 not found");
        verify(userRepository, times(1)).existsById(999L);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve criar usuário com todos os roles")
    void testCreate_DifferentRoles() {
        // Arrange
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act & Assert - TEACHER
        testUserDTO.setRole(Role.TEACHER);
        User teacher = userService.create(testUserDTO);
        assertThat(teacher.getRole()).isEqualTo(Role.TEACHER);

        // Act & Assert - ADMIN
        testUserDTO.setRole(Role.ROOT);
        User admin = userService.create(testUserDTO);
        assertThat(admin.getRole()).isEqualTo(Role.ROOT);

        // Act & Assert - STUDENT
        testUserDTO.setRole(Role.STUDENT);
        User student = userService.create(testUserDTO);
        assertThat(student.getRole()).isEqualTo(Role.STUDENT);

        verify(userRepository, times(3)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar usuário com dados válidos")
    void testUpdate_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setEmail("updated@example.com");

        // Act
        User result = userService.update(1L, updateDTO);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas campos fornecidos")
    void testUpdate_PartialUpdate() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("Only Name Updated");
        // email, role e passHash não são fornecidos

        // Act
        User result = userService.update(1L, updateDTO);

        // Assert
        assertThat(result.getName()).isEqualTo("Only Name Updated");
        assertThat(result.getEmail()).isEqualTo("test@example.com"); // manteve o antigo
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao atualizar usuário inexistente")
    void testUpdate_NotFound() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        UserDTO updateDTO = new UserDTO();
        updateDTO.setName("New Name");

        // Act & Assert
        assertThatThrownBy(() -> userService.update(999L, updateDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with ID 999 not found");
        verify(userRepository, times(1)).findById(999L);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Deve atualizar role do usuário")
    void testUpdate_ChangeRole() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDTO updateDTO = new UserDTO();
        updateDTO.setRole(Role.ROOT);

        // Act
        User result = userService.update(1L, updateDTO);

        // Assert
        assertThat(result.getRole()).isEqualTo(Role.ROOT);
        verify(userRepository, times(1)).save(any(User.class));
    }
}
