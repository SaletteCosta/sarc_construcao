
INSERT INTO subjects (code, name) VALUES
('MAT001', 'Calculus I'),
('MAT002', 'Linear Algebra'),
('MAT003', 'Analytic Geometry'),
('PHY001', 'Physics I'),
('PHY002', 'Physics II'),
('PRG001', 'Introduction to Programming'),
('PRG002', 'Data Structures'),
('PRG003', 'Advanced Algorithms'),
('ENG001', 'Software Engineering I'),
('ENG002', 'Software Architecture'),
('ENG003', 'Software Testing'),
('DBS001', 'Database I'),
('DBS002', 'Database II'),
('NET001', 'Computer Networks'),
('SYS001', 'Operating Systems');

INSERT INTO classes (code, subject_id, schedule) VALUES
('MAT001-A', 1, 'MON-08'),
('MAT001-B', 1, 'TUE-10'),
('MAT002-A', 2, 'WED-14'),
('PHY001-A', 4, 'THU-08'),
('PRG001-A', 6, 'FRI-10'),
('PRG002-A', 7, 'MON-14'),
('ENG001-A', 9, 'TUE-08'),
('DBS001-A', 12, 'WED-10');

INSERT INTO users (name, registration, type) VALUES
('John Silva', '202301234', 'STUDENT'),
('Mary Santos', '202301235', 'STUDENT'),
('Peter Oliveira', '202301236', 'STUDENT'),
('Anna Costa', '202301237', 'STUDENT'),
('Charles Souza', '202301238', 'STUDENT'),
('Julia Lima', '202301239', 'STUDENT'),
('Rafael Pereira', '202301240', 'STUDENT'),
('Beatrice Almeida', '202301241', 'STUDENT'),
('Luke Ferreira', '202301242', 'STUDENT'),
('Camila Rodrigues', '202301243', 'STUDENT');

INSERT INTO users (name, registration, type) VALUES
('Prof. Robert Mendes', 'PROF001', 'TEACHER'),
('Prof. Fernanda Souza', 'PROF002', 'TEACHER'),
('Prof. Edward Lima', 'PROF003', 'TEACHER'),
('Prof. Marcia Oliveira', 'PROF004', 'TEACHER');

INSERT INTO users (name, registration, type) VALUES
('System Admin', 'ADMIN001', 'ADMIN');

INSERT INTO class_students (class_id, student_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), -- MAT001-A
(2, 6), (2, 7), (2, 8), (2, 9), (2, 10), -- MAT001-B
(3, 1), (3, 3), (3, 5), (3, 7), (3, 9), -- MAT002-A
(4, 2), (4, 4), (4, 6), (4, 8), (4, 10), -- PHY001-A
(5, 1), (5, 2), (5, 3), (5, 4), (5, 5), (5, 6), -- PRG001-A
(6, 7), (6, 8), (6, 9), (6, 10), -- PRG002-A
(7, 1), (7, 4), (7, 7), (7, 10), -- ENG001-A
(8, 2), (8, 5), (8, 8); -- DBS001-A

INSERT INTO items (code, type, name, available) VALUES
('LAB-001', 'LABORATORY', 'Computer Lab 1', TRUE),
('LAB-002', 'LABORATORY', 'Computer Lab 2', TRUE),
('LAB-003', 'LABORATORY', 'Physics Lab', TRUE),
('LAB-004', 'LABORATORY', 'Chemistry Lab', TRUE),
('LAB-005', 'LABORATORY', 'Networks Lab', TRUE);

INSERT INTO items (code, type, name, available) VALUES
('PER-001', 'PERIPHERAL', 'Multimedia Projector', TRUE),
('PER-002', 'PERIPHERAL', 'Dell i5 Notebook', TRUE),
('PER-003', 'PERIPHERAL', 'Canon Digital Camera', TRUE),
('PER-004', 'PERIPHERAL', 'Wireless Microphone', TRUE),
('PER-005', 'PERIPHERAL', '24 Port Switch', TRUE),
('PER-006', 'PERIPHERAL', 'Wi-Fi Router', TRUE);

INSERT INTO items (code, type, name, available) VALUES
('ROOM-001', 'ROOM', 'Classroom 101', TRUE),
('ROOM-002', 'ROOM', 'Classroom 102', TRUE),
('ROOM-003', 'ROOM', 'Main Auditorium', TRUE),
('ROOM-004', 'ROOM', 'Meeting Room', TRUE);

INSERT INTO reservations (code, user_id, item_id, schedule, reservation_date, status) VALUES
('RES-001', 1, 1, 'MON-14', '2025-11-05', 'ACTIVE'),
('RES-002', 2, 2, 'TUE-10', '2025-11-05', 'ACTIVE'),
('RES-003', 3, 6, 'WED-08', '2025-11-06', 'ACTIVE'),
('RES-004', 4, 7, 'THU-14', '2025-11-06', 'ACTIVE'),
('RES-005', 5, 3, 'FRI-10', '2025-11-07', 'ACTIVE'),
('RES-006', 11, 11, 'MON-08', '2025-11-08', 'ACTIVE'),
('RES-007', 12, 12, 'TUE-14', '2025-11-08', 'ACTIVE');

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
