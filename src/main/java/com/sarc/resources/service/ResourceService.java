package com.sarc.resources.service;

import com.sarc.domain.Resource;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.ResourceRepository;
import com.sarc.resources.dto.ResourceDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository repository;

    public ResourceService(ResourceRepository repository) {
        this.repository = repository;
    }

    public List<Resource> getAll() {
        return repository.findAll();
    }

    public Resource getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Resource with ID " + id + " not found"));
    }

    public Resource create(ResourceDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Resource name cannot be empty");
        }

        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setCapacity(dto.getCapacity());
        resource.setLocalization(dto.getLocalization());

        return repository.save(resource);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("Resource with ID " + id + " not found");
        }
        repository.deleteById(id);
    }

    public Resource update(Long id, ResourceDTO dto) {
    Resource existing = repository.findById(id)
            .orElseThrow(() -> new NotFoundException("Resource with ID " + id + " not found"));

    if (dto.getName() != null && !dto.getName().isBlank()) {
        existing.setName(dto.getName());
    }
    if (dto.getType() != null) {
        existing.setType(dto.getType());
    }
    if (dto.getCapacity() != null) {
        existing.setCapacity(dto.getCapacity());
    }
    if (dto.getLocalization() != null && !dto.getLocalization().isBlank()) {
        existing.setLocalization(dto.getLocalization());
    }

    return repository.save(existing);
}

}
