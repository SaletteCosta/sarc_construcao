package com.student;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {
    
    @NotBlank(message = "Nome é obrigatório")
    private String name;
    
    @NotBlank(message = "Número de matrícula é obrigatório")
    private String registrationNumber;
}
