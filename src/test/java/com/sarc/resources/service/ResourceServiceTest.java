
package com.sarc.resources.service;

import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.ResourceRepository;
import com.sarc.resources.dto.ResourceDTO;
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
 * Testes unitários para ResourceService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ResourceService - Unit Tests")
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    private Resource testResource;
    private ResourceDTO testDTO;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Test Lab");
        testResource.setType(ResourceType.LAB);
        testResource.setCapacity(30);
        testResource.setLocalization("Building A - Room 101");

        testDTO = new ResourceDTO();
        testDTO.setName("New Lab");
        testDTO.setType(ResourceType.LAB);
        testDTO.setCapacity(25);
        testDTO.setLocalization("Building B - Room 202");
    }

    @Test
    @DisplayName("Deve retornar todos os recursos")
    void testGetAll_Success() {
        List<Resource> resources = Arrays.asList(testResource, new Resource());
        when(resourceRepository.findAll()).thenReturn(resources);

        List<Resource> result = resourceService.getAll();

        assertThat(result).hasSize(2);
        verify(resourceRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar recurso por ID")
    void testGetById_Success() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));

        Resource result = resourceService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getResourceId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Lab");
        verify(resourceRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando recurso não existe")
    void testGetById_NotFound() {
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceService.getById(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 999 not found");
    }

    @Test
    @DisplayName("Deve criar recurso com dados válidos")
    void testCreate_Success() {
        when(resourceRepository.save(any(Resource.class))).thenReturn(testResource);

        Resource result = resourceService.create(testDTO);

        assertThat(result).isNotNull();
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando nome está vazio")
    void testCreate_EmptyName() {
        testDTO.setName("");

        assertThatThrownBy(() -> resourceService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource name cannot be empty");
    }

    @Test
    @DisplayName("Deve lançar BadRequestException quando nome é nulo")
    void testCreate_NullName() {
        testDTO.setName(null);

        assertThatThrownBy(() -> resourceService.create(testDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource name cannot be empty");
    }

    @Test
    @DisplayName("Deve atualizar recurso existente")
    void testUpdate_Success() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(testResource);

        Resource result = resourceService.update(1L, testDTO);

        assertThat(result).isNotNull();
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas campos fornecidos")
    void testUpdate_PartialUpdate() {
        ResourceDTO partialDTO = new ResourceDTO();
        partialDTO.setName("Updated Name");
        
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(resourceRepository.save(any(Resource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Resource result = resourceService.update(1L, partialDTO);

        assertThat(result.getName()).isEqualTo("Updated Name");
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao atualizar recurso inexistente")
    void testUpdate_NotFound() {
        when(resourceRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceService.update(999L, testDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 999 not found");
    }

    @Test
    @DisplayName("Deve deletar recurso existente")
    void testDelete_Success() {
        when(resourceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRepository).deleteById(1L);

        resourceService.delete(1L);

        verify(resourceRepository, times(1)).existsById(1L);
        verify(resourceRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao deletar recurso inexistente")
    void testDelete_NotFound() {
        when(resourceRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> resourceService.delete(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 999 not found");
    }

    @Test
    @DisplayName("Deve criar recursos com diferentes tipos")
    void testCreate_DifferentTypes() {
        when(resourceRepository.save(any(Resource.class))).thenAnswer(invocation -> invocation.getArgument(0));

        testDTO.setType(ResourceType.ROOM);
        Resource classroom = resourceService.create(testDTO);
        assertThat(classroom.getType()).isEqualTo(ResourceType.ROOM);

        testDTO.setType(ResourceType.LAB);
        Resource lab = resourceService.create(testDTO);
        assertThat(lab.getType()).isEqualTo(ResourceType.LAB);

        testDTO.setType(ResourceType.ROOM);
        Resource auditorium = resourceService.create(testDTO);
        assertThat(auditorium.getType()).isEqualTo(ResourceType.ROOM);

        verify(resourceRepository, times(3)).save(any(Resource.class));
    }
}
