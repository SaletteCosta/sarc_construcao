
package com.sarc.courseclass.service;

import com.sarc.courseclass.dto.CourseClassDTO;
import com.sarc.domain.CourseClass;
import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.CourseClassRepository;
import com.sarc.repository.UserRepository;
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
 * Testes unitários para CourseClassService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CourseClassService - Unit Tests")
class CourseClassServiceTest {

    @Mock
    private CourseClassRepository courseClassRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseClassService courseClassService;

    private User testTeacher;
    private User testStudent;
    private CourseClass testClass;
    private CourseClassDTO testDTO;

    @BeforeEach
    void setUp() {
        testTeacher = new User();
        testTeacher.setUserId(1L);
        testTeacher.setName("Test Teacher");
        testTeacher.setEmail("teacher@example.com");
        testTeacher.setRole(Role.TEACHER);

        testStudent = new User();
        testStudent.setUserId(2L);
        testStudent.setName("Test Student");
        testStudent.setEmail("student@example.com");
        testStudent.setRole(Role.STUDENT);

        testClass = new CourseClass();
        testClass.setClassId(1L);
        testClass.setName("Test Class");
        testClass.setTeacher(testTeacher);

        testDTO = new CourseClassDTO();
        testDTO.setName("New Class");
        testDTO.setTeacherId(1L);
    }

    @Test
    @DisplayName("Deve retornar todas as turmas")
    void testGetAll_Success() {
        List<CourseClass> classes = Arrays.asList(testClass, new CourseClass());
        when(courseClassRepository.findAll()).thenReturn(classes);

        List<CourseClass> result = courseClassService.getAll();

        assertThat(result).hasSize(2);
        verify(courseClassRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar turma por ID")
    void testGetById_Success() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));

        CourseClass result = courseClassService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getClassId()).isEqualTo(1L);
        verify(courseClassRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando turma não existe")
    void testGetById_NotFound() {
        when(courseClassRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseClassService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass with ID 999 not found");
    }

    @Test
    @DisplayName("Deve retornar turmas por ID do professor")
    void testGetByTeacherId_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(courseClassRepository.findByTeacher_UserId(1L)).thenReturn(Arrays.asList(testClass));

        List<CourseClass> result = courseClassService.getByTeacherId(1L);

        assertThat(result).hasSize(1);
        verify(userRepository, times(1)).findById(1L);
        verify(courseClassRepository, times(1)).findByTeacher_UserId(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando professor não existe")
    void testGetByTeacherId_TeacherNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseClassService.getByTeacherId(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Teacher with ID 999 not found");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando usuário não é professor")
    void testGetByTeacherId_NotATeacher() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(testStudent));

        assertThatThrownBy(() -> courseClassService.getByTeacherId(2L))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("is not a TEACHER");
    }

    @Test
    @DisplayName("Deve criar turma com dados válidos")
    void testCreate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(courseClassRepository.save(any(CourseClass.class))).thenReturn(testClass);

        CourseClass result = courseClassService.create(testDTO);

        assertThat(result).isNotNull();
        verify(userRepository, times(1)).findById(1L);
        verify(courseClassRepository, times(1)).save(any(CourseClass.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando nome está vazio")
    void testCreate_EmptyName() {
        testDTO.setName("");

        assertThatThrownBy(() -> courseClassService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Class name cannot be empty");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando teacher ID é nulo")
    void testCreate_NullTeacherId() {
        testDTO.setTeacherId(null);

        assertThatThrownBy(() -> courseClassService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Teacher ID cannot be null");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando usuário não é professor")
    void testCreate_UserNotTeacher() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(testStudent));
        testDTO.setTeacherId(2L);

        assertThatThrownBy(() -> courseClassService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("is not a TEACHER");
    }

    @Test
    @DisplayName("Deve atualizar turma existente")
    void testUpdate_Success() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testClass));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testTeacher));
        when(courseClassRepository.save(any(CourseClass.class))).thenReturn(testClass);

        CourseClass result = courseClassService.update(1L, testDTO);

        assertThat(result).isNotNull();
        verify(courseClassRepository, times(1)).save(any(CourseClass.class));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao atualizar turma inexistente")
    void testUpdate_NotFound() {
        when(courseClassRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseClassService.update(999L, testDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass with ID 999 not found");
    }

    @Test
    @DisplayName("Deve deletar turma existente")
    void testDelete_Success() {
        when(courseClassRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseClassRepository).deleteById(1L);

        courseClassService.delete(1L);

        verify(courseClassRepository, times(1)).existsById(1L);
        verify(courseClassRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao deletar turma inexistente")
    void testDelete_NotFound() {
        when(courseClassRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> courseClassService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass with ID 999 not found");
    }
}
