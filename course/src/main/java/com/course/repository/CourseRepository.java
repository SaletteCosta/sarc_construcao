package com.course.repository;

import com.course.entidade.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByCourseCode(String courseCode);
    List<Course> findByCourseCodeAndScheduleSlot(String courseCode, String scheduleSlot);
    List<Course> findByScheduleSlot(String scheduleSlot);
}
