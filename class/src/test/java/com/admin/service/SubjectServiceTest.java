package com.admin.service;

import com.admin.dto.SubjectDTO;
import com.admin.entity.Subject;
import com.admin.repository.SubjectRepository;
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
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;
    private SubjectDTO subjectDTO;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(1L);
        subject.setCode("PRG001");
        subject.setName("Programming I");

        subjectDTO = new SubjectDTO(null, "PRG001", "Programming I");
    }

    @Test
    void testCreateSubject() {
        when(subjectRepository.existsByCode(anyString())).thenReturn(false);
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        SubjectDTO result = subjectService.createSubject(subjectDTO);

        assertNotNull(result);
        assertEquals("PRG001", result.getCode());
        assertEquals("Programming I", result.getName());
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void testCreateSubject_AlreadyExists() {
        when(subjectRepository.existsByCode("PRG001")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> subjectService.createSubject(subjectDTO));
        verify(subjectRepository, never()).save(any(Subject.class));
    }

    @Test
    void testGetSubjectByCode() {
        when(subjectRepository.findByCode("PRG001")).thenReturn(Optional.of(subject));

        SubjectDTO result = subjectService.getSubjectByCode("PRG001");

        assertNotNull(result);
        assertEquals("PRG001", result.getCode());
    }

    @Test
    void testGetSubjectByCode_NotFound() {
        when(subjectRepository.findByCode("XXX999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subjectService.getSubjectByCode("XXX999"));
    }

    @Test
    void testGetSubjectsByName() {
        List<Subject> subjects = Arrays.asList(subject);
        when(subjectRepository.findByNameContainingIgnoreCase("Programming")).thenReturn(subjects);

        List<SubjectDTO> result = subjectService.getSubjectsByName("Programming");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming I", result.get(0).getName());
    }

    @Test
    void testGetAllSubjects() {
        List<Subject> subjects = Arrays.asList(subject);
        when(subjectRepository.findAll()).thenReturn(subjects);

        List<SubjectDTO> result = subjectService.getAllSubjects();

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
