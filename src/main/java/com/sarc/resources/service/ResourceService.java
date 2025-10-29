package com.sarc.resources.service;

import com.sarc.domain.Resource;
import com.sarc.resources.dto.ResourceDTO;
import com.sarc.repository.ResourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
                .orElseThrow(() -> new NoSuchElementException("Resource not found with id: " + id));
    }

    public Resource create(ResourceDTO dto) {
        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setCapacity(dto.getCapacity());
        resource.setLocalization(dto.getLocalization());
        return repository.save(resource);
    }

    public Resource update(Long id, ResourceDTO dto) {
        Resource existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found with id: " + id));

        existing.setName(dto.getName());
        existing.setType(dto.getType());
        existing.setCapacity(dto.getCapacity());
        existing.setLocalization(dto.getLocalization());
        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Resource not found with id: " + id);
        }
        repository.deleteById(id);
    }
}
