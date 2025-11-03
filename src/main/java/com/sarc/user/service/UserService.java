package com.sarc.user.service;

import com.sarc.domain.User;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.UserRepository;
import com.sarc.user.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));
    }

    public User create(UserDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BadRequestException("Email cannot be empty");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        user.setPassHash("default"); 

        return repository.save(user);
    }

    public User update(Long id, UserDTO dto) {
        User existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with ID " + id + " not found"));

        if (dto.getName() != null && !dto.getName().isBlank()) {
            existing.setName(dto.getName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            existing.setEmail(dto.getEmail());
        }
        if (dto.getRole() != null) {
            existing.setRole(dto.getRole());
        }
        if (dto.getPassHash() != null && !dto.getPassHash().isBlank()) {
            existing.setPassHash(dto.getPassHash());
        }

        return repository.save(existing);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("User with ID " + id + " not found");
        }
        repository.deleteById(id);
    }
}
