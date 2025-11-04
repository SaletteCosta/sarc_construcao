package com.sarc.resources.service;

import com.sarc.domain.Resource;
import com.sarc.domain.ResourceType;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.ResourceRepository;
import com.sarc.resources.dto.ResourceDTO;
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
class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;

    private Resource testResource;
    private ResourceDTO testResourceDTO;

    @BeforeEach
    void setUp() {
        testResource = new Resource();
        testResource.setResourceId(1L);
        testResource.setName("Room 101");
        testResource.setType(ResourceType.ROOM);
        testResource.setCapacity(30);
        testResource.setLocalization("Building A");

        testResourceDTO = new ResourceDTO();
        testResourceDTO.setName("Room 101");
        testResourceDTO.setType(ResourceType.ROOM);
        testResourceDTO.setCapacity(30);
        testResourceDTO.setLocalization("Building A");
    }

    @Test
    void getAll_ShouldReturnAllResources() {
        Resource resource2 = new Resource();
        resource2.setResourceId(2L);
        resource2.setName("Lab 201");

        when(resourceRepository.findAll()).thenReturn(Arrays.asList(testResource, resource2));

        List<Resource> result = resourceService.getAll();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Room 101");
        assertThat(result.get(1).getName()).isEqualTo("Lab 201");
        verify(resourceRepository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnResource_WhenExists() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));

        Resource result = resourceService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getResourceId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Room 101");
        verify(resourceRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 1 not found");

        verify(resourceRepository, times(1)).findById(1L);
    }

    @Test
    void create_ShouldCreateResource_WhenDtoIsValid() {
        when(resourceRepository.save(any(Resource.class))).thenReturn(testResource);

        Resource result = resourceService.create(testResourceDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Room 101");
        assertThat(result.getType()).isEqualTo(ResourceType.ROOM);
        assertThat(result.getCapacity()).isEqualTo(30);
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenNameIsNull() {
        testResourceDTO.setName(null);

        assertThatThrownBy(() -> resourceService.create(testResourceDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource name cannot be empty");

        verify(resourceRepository, never()).save(any(Resource.class));
    }

    @Test
    void create_ShouldThrowBadRequestException_WhenNameIsBlank() {
        testResourceDTO.setName("   ");

        assertThatThrownBy(() -> resourceService.create(testResourceDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Resource name cannot be empty");

        verify(resourceRepository, never()).save(any(Resource.class));
    }

    @Test
    void update_ShouldUpdateResource_WhenExists() {
        ResourceDTO updateDTO = new ResourceDTO();
        updateDTO.setName("Room 101 Updated");
        updateDTO.setCapacity(40);

        when(resourceRepository.findById(1L)).thenReturn(Optional.of(testResource));
        when(resourceRepository.save(any(Resource.class))).thenReturn(testResource);

        Resource result = resourceService.update(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, times(1)).save(any(Resource.class));
    }

    @Test
    void update_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(resourceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceService.update(1L, testResourceDTO))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 1 not found");

        verify(resourceRepository, times(1)).findById(1L);
        verify(resourceRepository, never()).save(any(Resource.class));
    }

    @Test
    void delete_ShouldDeleteResource_WhenExists() {
        when(resourceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resourceRepository).deleteById(1L);

        resourceService.delete(1L);

        verify(resourceRepository, times(1)).existsById(1L);
        verify(resourceRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowNotFoundException_WhenDoesNotExist() {
        when(resourceRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> resourceService.delete(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Resource with ID 1 not found");

        verify(resourceRepository, times(1)).existsById(1L);
        verify(resourceRepository, never()).deleteById(1L);
    }
}
