package com.course.entidade;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "course_code", nullable = false)
    private String courseCode;
    
    @Column(name = "course_name", nullable = false)
    private String courseName;
    
    @Column(name = "schedule_slot", nullable = false)
    private String scheduleSlot; 
}
