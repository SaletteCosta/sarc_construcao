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
class CourseClassServiceTest {

    @Mock
    private CourseClassRepository courseClassRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseClassService courseClassService;

    private CourseClass testCourseClass;
    private CourseClassDTO testCourseClassDTO;
    private User teacher;

    @BeforeEach
    void setUp() {
        teacher = new User();
        teacher.setUserId(1L);
        teacher.setName("Teacher");
        teacher.setEmail("teacher@example.com");
        teacher.setRole(Role.TEACHER);

        testCourseClass = new CourseClass();
        testCourseClass.setClassId(1L);
        testCourseClass.setName("Math 101");
        testCourseClass.setTeacher(teacher);

        testCourseClassDTO = new CourseClassDTO();
        testCourseClassDTO.setName("Math 101");
        testCourseClassDTO.setTeacherId(1L);
    }

    @Test
    void getAll_ShouldReturnAllCourseClasses() {
        CourseClass courseClass2 = new CourseClass();
        courseClass2.setClassId(2L);
        courseClass2.setName("Physics 101");

        when(courseClassRepository.findAll()).thenReturn(Arrays.asList(testCourseClass, courseClass2));

        List<CourseClass> result = courseClassService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Math 101");
        assertThat(result.get(1).getName()).isEqualTo("Physics 101");
        verify(courseClassRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnCourseClass_WhenExists() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.of(testCourseClass));

        CourseClass result = courseClassService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getClassId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Math 101");
        verify(courseClassRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(courseClassRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseClassService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass not found");

        verify(courseClassRepository, times(1)).findById(1L);
    }

    @Test
    void create_ShouldCreateCourseClass_WhenDtoIsValid() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(courseClassRepository.save(any(CourseClass.class))).thenReturn(testCourseClass);

        CourseClass result = courseClassService.create(testCourseClassDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Math 101");
        assertThat(result.getTeacher()).isEqualTo(teacher);
        verify(userRepository, times(1)).findById(1L);
        verify(courseClassRepository, times(1)).save(any(CourseClass.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenNameIsNull() {
        testCourseClassDTO.setName(null);

        assertThatThrownBy(() -> courseClassService.create(testCourseClassDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("CourseClass name cannot be empty");

        verify(courseClassRepository, never()).save(any(CourseClass.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenNameIsBlank() {
        testCourseClassDTO.setName("   ");

        assertThatThrownBy(() -> courseClassService.create(testCourseClassDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("CourseClass name cannot be empty");

        verify(courseClassRepository, never()).save(any(CourseClass.class));
    }

    @Test
    void create_ShouldThrowNotFoundException_WhenTeacherDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseClassService.create(testCourseClassDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Teacher not found");

        verify(userRepository, times(1)).findById(1L);
        verify(courseClassRepository, never()).save(any(CourseClass.class));
    }

    @Test
    void delete_ShouldDeleteCourseClass_WhenExists() {
        when(courseClassRepository.existsById(1L)).thenReturn(true);
        doNothing().when(courseClassRepository).deleteById(1L);

        courseClassService.delete(1L);

        verify(courseClassRepository, times(1)).existsById(1L);
        verify(courseClassRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(courseClassRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> courseClassService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CourseClass not found");

        verify(courseClassRepository, times(1)).existsById(1L);
        verify(courseClassRepository, never()).deleteById(1L);
    }
}
