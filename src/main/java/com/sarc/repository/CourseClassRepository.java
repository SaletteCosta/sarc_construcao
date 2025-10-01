package com.sarc.repository;

import com.sarc.domain.CourseClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseClassRepository extends JpaRepository<CourseClass, Long> {}
