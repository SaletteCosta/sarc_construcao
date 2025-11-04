package com.admin.repository;

import com.admin.entity.ClassStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassStudentRepository extends JpaRepository<ClassStudent, Long> {
    List<ClassStudent> findByClassId(Long classId);
    List<ClassStudent> findByStudentId(Long studentId);
    Optional<ClassStudent> findByClassIdAndStudentId(Long classId, Long studentId);
    boolean existsByClassIdAndStudentId(Long classId, Long studentId);
    void deleteByClassIdAndStudentId(Long classId, Long studentId);
}
