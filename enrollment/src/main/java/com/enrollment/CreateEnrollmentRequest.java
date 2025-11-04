package com.enrollment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnrollmentRequest {
    
    @NotNull(message = "ID do estudante é obrigatório")
    private Long studentId;
    
    @NotNull(message = "ID da disciplina é obrigatório")
    private Long courseId;
}
