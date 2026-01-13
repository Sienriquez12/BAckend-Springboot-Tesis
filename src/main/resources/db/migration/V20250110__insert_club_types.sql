-- Script para insertar los tipos de clubes universitarios
-- Ejecutar solo en entorno de desarrollo/local

INSERT INTO club_type (id, name, description, icon, color, order_index, record_status, created_at, updated_at) VALUES
(1, 'Deportivo', 'Clubes enfocados en actividad física y competencia. Incluye fútbol, básquet, atletismo, natación, etc.', 'fa-futbol', '#FF6B6B', 1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Académico', 'Clubes que refuerzan conocimientos de la carrera. Incluye programación, robótica, matemáticas, investigación, ciencia.', 'fa-graduation-cap', '#4ECDC4', 2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Cultural y Artístico', 'Clubes que promueven el arte y la expresión. Incluye teatro, música, danza, lectura, fotografía.', 'fa-palette', '#45B7D1', 3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Social y Voluntariado', 'Clubes enfocados en impacto social. Incluye ayuda comunitaria, medio ambiente, derechos humanos.', 'fa-hands-helping', '#96CEB4', 4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Tecnológico', 'Clubes especializados en tecnología avanzada. Incluye IA, ciberseguridad, desarrollo de software, electrónica.', 'fa-microchip', '#FFEAA7', 5, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Emprendimiento y Liderazgo', 'Clubes enfocados en desarrollo empresarial y liderazgo. Incluye negocios, startups, liderazgo estudiantil.', 'fa-rocket', '#DDA0DD', 6, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Recreativo', 'Clubes de entretenimiento y hobbies. Incluye ajedrez, videojuegos, debates, hobbies diversos.', 'fa-gamepad', '#FFB74D', 7, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Fin del script
