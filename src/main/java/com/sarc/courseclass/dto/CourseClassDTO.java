
package com.sarc.courseclass.dto;

/**
 * DTO para transferência de dados de Turma (CourseClass)
 * Usado para criar e atualizar turmas através da API
 */
public class CourseClassDTO {
    private String name;
    private Long teacherId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
}
