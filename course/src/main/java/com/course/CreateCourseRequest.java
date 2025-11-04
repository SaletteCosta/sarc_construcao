package com.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseRequest {
    
    @NotBlank(message = "Código da disciplina é obrigatório")
    private String courseCode;
    
    @NotBlank(message = "Nome da disciplina é obrigatório")
    private String courseName;
    
    @NotBlank(message = "Horário é obrigatório")
    @Pattern(regexp = "[A-G]", message = "Horário deve ser uma letra entre A e G")
    private String scheduleSlot;
}
