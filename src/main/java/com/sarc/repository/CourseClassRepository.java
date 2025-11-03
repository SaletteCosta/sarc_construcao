package com.sarc.repository;

import com.sarc.domain.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {
    List<CourseClass> findByTeacher_UserId(Long userId);
}
