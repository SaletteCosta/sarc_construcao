package com.user.repository;

import com.user.entity.User;
import com.user.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByRegistration(String registration);
    List<User> findByNameContainingIgnoreCase(String name);
    List<User> findByType(UserType type);
    boolean existsByRegistration(String registration);
}
