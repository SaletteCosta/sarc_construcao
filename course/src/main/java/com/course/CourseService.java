package com.course;

import com.course.entidade.Course;
import com.course.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Transactional
    public CourseDTO createCourse(CreateCourseRequest request) {
        Course course = new Course();
        course.setCourseCode(request.getCourseCode());
        course.setCourseName(request.getCourseName());
        course.setScheduleSlot(request.getScheduleSlot());
        
        Course saved = courseRepository.save(course);
        return toDTO(saved);
    }
    
    public List<CourseDTO> findByCourseCode(String courseCode) {
        List<Course> courses = courseRepository.findByCourseCode(courseCode);
        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public List<CourseDTO> findByScheduleSlot(String scheduleSlot) {
        List<Course> courses = courseRepository.findByScheduleSlot(scheduleSlot);
        return courses.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public CourseDTO findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));
        return toDTO(course);
    }
    
    public List<CourseDTO> findAll() {
        return courseRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    private CourseDTO toDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setCourseCode(course.getCourseCode());
        dto.setCourseName(course.getCourseName());
        dto.setScheduleSlot(course.getScheduleSlot());
        return dto;
    }
}
