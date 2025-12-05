
INSERT INTO subjects (code, name) VALUES
('MAT001', 'Cálculo I'),
('MAT002', 'Álgebra Linear'),
('MAT003', 'Geometria Analítica'),
('FIS001', 'Física I'),
('FIS002', 'Física II'),
('PRG001', 'Introdução à Programação'),
('PRG002', 'Estruturas de Dados'),
('PRG003', 'Algoritmos Avançados'),
('ENG001', 'Engenharia de Software I'),
('ENG002', 'Arquitetura de Software'),
('ENG003', 'Testes de Software'),
('BDD001', 'Banco de Dados I'),
('BDD002', 'Banco de Dados II'),
('RED001', 'Redes de Computadores'),
('SIS001', 'Sistemas Operacionais');

INSERT INTO classes (code, subject_id, schedule) VALUES
('MAT001-A', 1, 'SEG-08'),
('MAT001-B', 1, 'TER-10'),
('MAT002-A', 2, 'QUA-14'),
('FIS001-A', 4, 'QUI-08'),
('PRG001-A', 6, 'SEX-10'),
('PRG002-A', 7, 'SEG-14'),
('ENG001-A', 9, 'TER-08'),
('BDD001-A', 12, 'QUA-10');

INSERT INTO users (name, registration, type) VALUES
('João Silva', '202301234', 'STUDENT'),
('Maria Santos', '202301235', 'STUDENT'),
('Pedro Oliveira', '202301236', 'STUDENT'),
('Ana Costa', '202301237', 'STUDENT'),
('Carlos Souza', '202301238', 'STUDENT'),
('Júlia Lima', '202301239', 'STUDENT'),
('Rafael Pereira', '202301240', 'STUDENT'),
('Beatriz Almeida', '202301241', 'STUDENT'),
('Lucas Ferreira', '202301242', 'STUDENT'),
('Camila Rodrigues', '202301243', 'STUDENT');

INSERT INTO users (name, registration, type) VALUES
('Prof. Roberto Mendes', 'PROF001', 'TEACHER'),
('Prof. Fernanda Souza', 'PROF002', 'TEACHER'),
('Prof. Eduardo Lima', 'PROF003', 'TEACHER'),
('Prof. Márcia Oliveira', 'PROF004', 'TEACHER');

INSERT INTO users (name, registration, type) VALUES
('Administrador do Sistema', 'ADMIN001', 'ADMIN');

INSERT INTO class_students (class_id, student_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), -- MAT001-A
(2, 6), (2, 7), (2, 8), (2, 9), (2, 10), -- MAT001-B
(3, 1), (3, 3), (3, 5), (3, 7), (3, 9), -- MAT002-A
(4, 2), (4, 4), (4, 6), (4, 8), (4, 10), -- FIS001-A
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6), -- PRG001-A
(6, 7), (6, 8), (6, 9), (6, 10), -- PRG002-A
(7, 1), (7, 4), (7, 7), (7, 10), -- ENG001-A
(8, 2), (8, 5), (8, 8); -- BDD001-A

INSERT INTO items (code, type, name, available) VALUES
('LAB-001', 'LABORATORY', 'Laboratório de Informática 1', TRUE),
('LAB-002', 'LABORATORY', 'Laboratório de Informática 2', TRUE),
('LAB-003', 'LABORATORY', 'Laboratório de Física', TRUE),
('LAB-004', 'LABORATORY', 'Laboratório de Química', TRUE),
('LAB-005', 'LABORATORY', 'Laboratório de Redes', TRUE);

INSERT INTO items (code, type, name, available) VALUES
('PER-001', 'PERIPHERAL', 'Projetor Multimídia', TRUE),
('PER-002', 'PERIPHERAL', 'Notebook Dell i5', TRUE),
('PER-003', 'PERIPHERAL', 'Câmera Digital Canon', TRUE),
('PER-004', 'PERIPHERAL', 'Microfone Sem Fio', TRUE),
('PER-005', 'PERIPHERAL', 'Switch 24 Portas', TRUE),
('PER-006', 'PERIPHERAL', 'Roteador Wi-Fi', TRUE);

INSERT INTO items (code, type, name, available) VALUES
('SALA-001', 'ROOM', 'Sala de Aula 101', TRUE),
('SALA-002', 'ROOM', 'Sala de Aula 102', TRUE),
('SALA-003', 'ROOM', 'Auditório Principal', TRUE),
('SALA-004', 'ROOM', 'Sala de Reuniões', TRUE);

INSERT INTO reservations (code, user_id, item_id, schedule, reservation_date, status) VALUES
('RES-001', 1, 1, 'SEG-14', '2025-11-05', 'ACTIVE'),
('RES-002', 2, 2, 'TER-10', '2025-11-05', 'ACTIVE'),
('RES-003', 3, 6, 'QUA-08', '2025-11-06', 'ACTIVE'),
('RES-004', 4, 7, 'QUI-14', '2025-11-06', 'ACTIVE'),
('RES-005', 5, 3, 'SEX-10', '2025-11-07', 'ACTIVE'),
('RES-006', 11, 11, 'SEG-08', '2025-11-08', 'ACTIVE'),
('RES-007', 12, 12, 'TER-14', '2025-11-08', 'ACTIVE');

INSERT INTO schedules (day_week, due_hour, start_hour) VALUES
('MONDAY', '10:00:00', '08:00:00'),
('MONDAY', '12:00:00', '10:00:00'),
('MONDAY', '16:00:00', '14:00:00'),
('TUESDAY', '10:00:00', '08:00:00'),
('TUESDAY', '12:00:00', '10:00:00'),
('TUESDAY', '16:00:00', '14:00:00'),
('WEDNESDAY', '10:00:00', '08:00:00'),
('WEDNESDAY', '12:00:00', '10:00:00'),
('WEDNESDAY', '16:00:00', '14:00:00'),
('THURSDAY', '10:00:00', '08:00:00'),
('THURSDAY', '12:00:00', '10:00:00'),
('THURSDAY', '16:00:00', '14:00:00'),
('FRIDAY', '10:00:00', '08:00:00'),
('FRIDAY', '12:00:00', '10:00:00'),
('FRIDAY', '16:00:00', '14:00:00');
