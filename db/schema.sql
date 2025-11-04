DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    registration_number VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_students_name ON students(name);
CREATE INDEX idx_students_registration ON students(registration_number);

CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL,
    course_name VARCHAR(255) NOT NULL,
    schedule_slot VARCHAR(1) NOT NULL CHECK (schedule_slot IN ('A', 'B', 'C', 'D', 'E', 'F', 'G')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_courses_code ON courses(course_code);
CREATE INDEX idx_courses_schedule ON courses(schedule_slot);
CREATE INDEX idx_courses_code_schedule ON courses(course_code, schedule_slot);

CREATE TABLE enrollments (
    id SERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'CANCELLED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) 
        REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_course FOREIGN KEY (course_id) 
        REFERENCES courses(id) ON DELETE CASCADE,
    
    CONSTRAINT unique_student_course UNIQUE (student_id, course_id)
);

CREATE INDEX idx_enrollments_student ON enrollments(student_id);
CREATE INDEX idx_enrollments_course ON enrollments(course_id);
CREATE INDEX idx_enrollments_date ON enrollments(enrollment_date);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_students_updated_at 
    BEFORE UPDATE ON students
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_courses_updated_at 
    BEFORE UPDATE ON courses
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_enrollments_updated_at 
    BEFORE UPDATE ON enrollments
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

COMMENT ON TABLE students IS 'Tabela de estudantes do sistema';
COMMENT ON COLUMN students.name IS 'Nome completo do estudante';
COMMENT ON COLUMN students.registration_number IS 'Número de matrícula único';

COMMENT ON TABLE courses IS 'Tabela de disciplinas oferecidas';
COMMENT ON COLUMN courses.course_code IS 'Código da disciplina (ex: MAT001)';
COMMENT ON COLUMN courses.course_name IS 'Nome da disciplina (ex: Matemática)';
COMMENT ON COLUMN courses.schedule_slot IS 'Horário da disciplina (A-G)';

COMMENT ON TABLE enrollments IS 'Tabela de matrículas de estudantes em disciplinas';
COMMENT ON COLUMN enrollments.student_id IS 'ID do estudante matriculado';
COMMENT ON COLUMN enrollments.course_id IS 'ID da disciplina';
COMMENT ON COLUMN enrollments.enrollment_date IS 'Data e hora da matrícula';
COMMENT ON COLUMN enrollments.status IS 'Status da matrícula (ACTIVE ou CANCELLED)';
