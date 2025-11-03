package com.sarc.courseclass.service;

import com.sarc.domain.CourseClass;
import com.sarc.domain.User;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.CourseClassRepository;
import com.sarc.repository.UserRepository;
import com.sarc.courseclass.dto.CourseClassDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseClassService {

    private final CourseClassRepository classRepo;
    private final UserRepository userRepo;

    public CourseClassService(CourseClassRepository classRepo, UserRepository userRepo) {
        this.classRepo = classRepo;
        this.userRepo = userRepo;
    }

    public List<CourseClass> getAll() {
        return classRepo.findAll();
    }

    public CourseClass getById(Long id) {
        return classRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("CourseClass not found"));
    }

    public CourseClass create(CourseClassDTO dto) {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("CourseClass name cannot be empty");
        }

        User teacher = userRepo.findById(dto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        CourseClass courseClass = new CourseClass();
        courseClass.setName(dto.getName());
        courseClass.setTeacher(teacher);

        return classRepo.save(courseClass);
    }

    public void delete(Long id) {
        if (!classRepo.existsById(id)) {
            throw new NotFoundException("CourseClass not found");
        }
        classRepo.deleteById(id);
    }
}
