INSERT INTO students (name, registration_number) VALUES
('João Silva', '202301234'),
('Maria Santos', '202301235'),
('Pedro Oliveira', '202301236'),
('Ana Costa', '202301237'),
('Carlos Souza', '202301238'),
('Juliana Lima', '202301239'),
('Rafael Pereira', '202301240'),
('Beatriz Almeida', '202301241'),
('Lucas Ferreira', '202301242'),
('Camila Rodrigues', '202301243'),
('Fernando Martins', '202301244'),
('Larissa Carvalho', '202301245'),
('Gustavo Ribeiro', '202301246'),
('Isabela Nascimento', '202301247'),
('Thiago Gomes', '202301248'),
('Amanda Barbosa', '202301249'),
('Bruno Dias', '202301250'),
('Natália Fernandes', '202301251'),
('Vinícius Castro', '202301252'),
('Gabriela Freitas', '202301253');

('MAT001', 'Cálculo I', 'A'),
('MAT001', 'Cálculo I', 'B'),
('MAT001', 'Cálculo I', 'E'),
('MAT002', 'Álgebra Linear', 'C'),
('MAT002', 'Álgebra Linear', 'D'),
('MAT003', 'Geometria Analítica', 'F');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('FIS001', 'Física I', 'A'),
('FIS001', 'Física I', 'D'),
('FIS002', 'Física II', 'B'),
('FIS002', 'Física II', 'G');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('PRG001', 'Introdução à Programação', 'A'),
('PRG001', 'Introdução à Programação', 'C'),
('PRG002', 'Estruturas de Dados', 'B'),
('PRG002', 'Estruturas de Dados', 'E'),
('PRG003', 'Algoritmos Avançados', 'D');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('ENG001', 'Engenharia de Software I', 'C'),
('ENG001', 'Engenharia de Software I', 'F'),
('ENG002', 'Arquitetura de Software', 'A'),
('ENG003', 'Testes de Software', 'G');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('BDD001', 'Banco de Dados I', 'B'),
('BDD001', 'Banco de Dados I', 'E'),
('BDD002', 'Banco de Dados II', 'D');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('RED001', 'Redes de Computadores', 'A'),
('RED001', 'Redes de Computadores', 'F'),
('RED002', 'Segurança de Redes', 'C');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('SOP001', 'Sistemas Operacionais', 'B'),
('SOP001', 'Sistemas Operacionais', 'G'),
('SOP002', 'Sistemas Distribuídos', 'E');

INSERT INTO courses (course_code, course_name, schedule_slot) VALUES
('INT001', 'Inteligência Artificial', 'D'),
('INT002', 'Machine Learning', 'F'),
('INT003', 'Processamento de Linguagem Natural', 'G');

INSERT INTO enrollments (student_id, course_id) VALUES
(1, 1),   
(1, 7),  
(1, 13),
(1, 16), 
(1, 21); 

INSERT INTO enrollments (student_id, course_id) VALUES
(2, 2),  
(2, 9),  
(2, 11), 
(2, 20);  

INSERT INTO enrollments (student_id, course_id) VALUES
(3, 1), 
(3, 4), 
(3, 11),
(3, 18), 
(3, 24),  
(3, 27);  

INSERT INTO enrollments (student_id, course_id) VALUES
(4, 12),
(4, 14), 
(4, 22); 

INSERT INTO enrollments (student_id, course_id) VALUES
(5, 3),
(5, 8),  
(5, 15), 
(5, 23),  
(5, 30);  

INSERT INTO enrollments (student_id, course_id) VALUES
(6, 6),   
(6, 17), 
(6, 25), 
(6, 31); 

INSERT INTO enrollments (student_id, course_id) VALUES
(7, 10),  
(7, 19), 
(7, 28); 

INSERT INTO enrollments (student_id, course_id) VALUES
(8, 1),  
(8, 11), 
(8, 16),  
(8, 26); 

INSERT INTO enrollments (student_id, course_id) VALUES
(9, 2),   
(9, 13), 
(9, 20),  
(9, 27), 
(9, 9);   

INSERT INTO enrollments (student_id, course_id) VALUES
(10, 4), 
(10, 12),
(10, 16); 

INSERT INTO enrollments (student_id, course_id) VALUES
(11, 5),  
(11, 8),  
(11, 15),
(11, 30);

INSERT INTO enrollments (student_id, course_id) VALUES
(12, 3),  
(12, 14), 
(12, 29); 

INSERT INTO enrollments (student_id, course_id) VALUES
(13, 6), 
(13, 17),
(13, 25),
(13, 31); 

INSERT INTO enrollments (student_id, course_id) VALUES
(14, 10), 
(14, 32); 

INSERT INTO enrollments (student_id, course_id) VALUES
(15, 1), 
(15, 7), 
(15, 18),
(15, 24), 
(15, 11); 

SELECT 'Estudantes cadastrados:' AS info, COUNT(*) AS total FROM students
UNION ALL
SELECT 'Disciplinas cadastradas:', COUNT(*) FROM courses
UNION ALL
SELECT 'Matrículas realizadas:', COUNT(*) FROM enrollments;

SELECT 
    c.course_code,
    c.course_name,
    c.schedule_slot,
    COUNT(e.id) as total_matriculas
FROM courses c
LEFT JOIN enrollments e ON c.id = e.course_id
GROUP BY c.id, c.course_code, c.course_name, c.schedule_slot
ORDER BY total_matriculas DESC, c.course_code, c.schedule_slot
LIMIT 10;

SELECT 
    s.name,
    s.registration_number,
    COUNT(e.id) as total_disciplinas
FROM students s
LEFT JOIN enrollments e ON s.id = e.student_id
GROUP BY s.id, s.name, s.registration_number
ORDER BY total_disciplinas DESC
LIMIT 10;
