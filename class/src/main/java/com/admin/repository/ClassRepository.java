package com.admin.repository;

import com.admin.entity.ClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long> {
    Optional<ClassEntity> findByCode(String code);
    List<ClassEntity> findBySubjectId(Long subjectId);
    
    @Query("SELECT c FROM ClassEntity c JOIN ClassStudent cs ON c.id = cs.classId WHERE cs.studentId = :studentId")
    List<ClassEntity> findByStudentId(@Param("studentId") Long studentId);
    
    boolean existsByCode(String code);
}
