
package com.sarc.courseclass.service;

import com.sarc.domain.CourseClass;
import com.sarc.domain.Role;
import com.sarc.domain.User;
import com.sarc.exception.BadRequestException;
import com.sarc.exception.NotFoundException;
import com.sarc.repository.CourseClassRepository;
import com.sarc.repository.UserRepository;
import com.sarc.courseclass.dto.CourseClassDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço para gerenciamento de Turmas (CourseClass)
 * Implementa toda a lógica de negócio relacionada a turmas
 */
@Service
@Transactional
public class CourseClassService {

    private final CourseClassRepository courseClassRepository;
    private final UserRepository userRepository;

    public CourseClassService(CourseClassRepository courseClassRepository, UserRepository userRepository) {
        this.courseClassRepository = courseClassRepository;
        this.userRepository = userRepository;
    }

    /**
     * Lista todas as turmas cadastradas
     * @return Lista de todas as turmas
     */
    public List<CourseClass> getAll() {
        return courseClassRepository.findAll();
    }

    /**
     * Busca uma turma por ID
     * @param id ID da turma
     * @return Turma encontrada
     * @throws NotFoundException se a turma não existir
     */
    public CourseClass getById(Long id) {
        return courseClassRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CourseClass with ID " + id + " not found"));
    }

    /**
     * Busca todas as turmas de um professor específico
     * @param teacherId ID do professor
     * @return Lista de turmas do professor
     */
    public List<CourseClass> getByTeacherId(Long teacherId) {
        // Verifica se o professor existe
        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Teacher with ID " + teacherId + " not found"));
        
        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("User with ID " + teacherId + " is not a TEACHER");
        }
        
        return courseClassRepository.findByTeacher_UserId(teacherId);
    }

    /**
     * Cria uma nova turma
     * @param dto Dados da turma a ser criada
     * @return Turma criada
     * @throws BadRequestException se os dados forem inválidos
     * @throws NotFoundException se o professor não existir
     */
    public CourseClass create(CourseClassDTO dto) {
        // Validações
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new BadRequestException("Class name cannot be empty");
        }
        
        if (dto.getTeacherId() == null) {
            throw new BadRequestException("Teacher ID cannot be null");
        }

        // Busca o professor
        User teacher = userRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new NotFoundException("Teacher with ID " + dto.getTeacherId() + " not found"));

        // Valida se o usuário é realmente um professor
        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("User with ID " + dto.getTeacherId() + " is not a TEACHER");
        }

        // Cria a turma
        CourseClass courseClass = new CourseClass();
        courseClass.setName(dto.getName());
        courseClass.setTeacher(teacher);

        return courseClassRepository.save(courseClass);
    }

    /**
     * Atualiza uma turma existente
     * @param id ID da turma a ser atualizada
     * @param dto Novos dados da turma
     * @return Turma atualizada
     * @throws NotFoundException se a turma ou professor não existirem
     * @throws BadRequestException se os dados forem inválidos
     */
    public CourseClass update(Long id, CourseClassDTO dto) {
        CourseClass existing = courseClassRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("CourseClass with ID " + id + " not found"));

        // Atualiza o nome se fornecido
        if (dto.getName() != null && !dto.getName().isBlank()) {
            existing.setName(dto.getName());
        }

        // Atualiza o professor se fornecido
        if (dto.getTeacherId() != null) {
            User teacher = userRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new NotFoundException("Teacher with ID " + dto.getTeacherId() + " not found"));

            if (teacher.getRole() != Role.TEACHER) {
                throw new BadRequestException("User with ID " + dto.getTeacherId() + " is not a TEACHER");
            }

            existing.setTeacher(teacher);
        }

        return courseClassRepository.save(existing);
    }

    /**
     * Deleta uma turma
     * @param id ID da turma a ser deletada
     * @throws NotFoundException se a turma não existir
     */
    public void delete(Long id) {
        if (!courseClassRepository.existsById(id)) {
            throw new NotFoundException("CourseClass with ID " + id + " not found");
        }
        courseClassRepository.deleteById(id);
    }
}
