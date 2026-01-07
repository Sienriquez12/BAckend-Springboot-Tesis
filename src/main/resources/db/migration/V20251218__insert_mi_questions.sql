-- Script de ejemplo para insertar tipos de inteligencias múltiples y preguntas
-- Ejecutar solo en entorno de desarrollo/local si usas migraciones manuales

INSERT INTO intelligence_type (id, code, name, description, record_status, created_at) VALUES
(1, 'LINGUISTIC', 'Lingüística', 'Facilidad con las palabras y el lenguaje', true, CURRENT_TIMESTAMP),
(2, 'LOGICAL', 'Lógico-matemática', 'Habilidad para el razonamiento lógico y los números', true, CURRENT_TIMESTAMP),
(3, 'VISUAL', 'Viso-espacial', 'Pensar en imágenes y espacios', true, CURRENT_TIMESTAMP),
(4, 'MUSICAL', 'Musical', 'Sensibilidad al ritmo y al sonido', true, CURRENT_TIMESTAMP),
(5, 'BODILY', 'Corporal-cinestésica', 'Habilidad con el cuerpo y el movimiento', true, CURRENT_TIMESTAMP),
(6, 'INTERPERSONAL', 'Interpersonal', 'Capacidad de entender y relacionarse con otros', true, CURRENT_TIMESTAMP),
(7, 'INTRAPERSONAL', 'Intrapersonal', 'Capacidad de autoconocimiento y gestión emocional', true, CURRENT_TIMESTAMP),
(8, 'NATURALIST', 'Naturalista', 'Comprensión del entorno natural', true, CURRENT_TIMESTAMP);

-- Ejemplos de preguntas para cada tipo (solo 2 por tipo como ejemplo)
INSERT INTO mi_question (id, intelligence_type_id, code, text, order_index, record_status, created_at) VALUES
(1, 1, 'LING_1', 'Me siento cómodo expresando ideas por escrito o hablando en público.', 1, true, CURRENT_TIMESTAMP),
(2, 1, 'LING_2', 'Disfruto leer y escribir historias o ensayos.', 2, true, CURRENT_TIMESTAMP),
(3, 2, 'LOG_1', 'Me gusta resolver rompecabezas y problemas de lógica.', 1, true, CURRENT_TIMESTAMP),
(4, 2, 'LOG_2', 'Me resulta sencillo comprender conceptos matemáticos.', 2, true, CURRENT_TIMESTAMP),
(5, 3, 'VIS_1', 'Puedo imaginar cómo se verá un objeto desde diferentes ángulos.', 1, true, CURRENT_TIMESTAMP),
(6, 3, 'VIS_2', 'Disfruto dibujar, diseñar o trabajar con imágenes.', 2, true, CURRENT_TIMESTAMP),
(7, 4, 'MUS_1', 'Me acuerdo fácilmente de melodías y ritmos.', 1, true, CURRENT_TIMESTAMP),
(8, 4, 'MUS_2', 'Me gusta tocar instrumentos o cantar.', 2, true, CURRENT_TIMESTAMP),
(9, 5, 'BOD_1', 'Aprendo mejor cuando hago actividades prácticas y en movimiento.', 1, true, CURRENT_TIMESTAMP),
(10,5, 'BOD_2', 'Me siento cómodo usando mi cuerpo para expresarme (baile, deporte).', 2, true, CURRENT_TIMESTAMP),
(11,6, 'INTP_1', 'Me resulta fácil entender las emociones y necesidades de otras personas.', 1, true, CURRENT_TIMESTAMP),
(12,6, 'INTP_2', 'Disfruto trabajar en equipo y liderar actividades sociales.', 2, true, CURRENT_TIMESTAMP),
(13,7, 'INTR_1', 'Puedo identificar mis emociones y cómo me afectan.', 1, true, CURRENT_TIMESTAMP),
(14,7, 'INTR_2', 'Me gusta reflexionar sobre mis decisiones y metas personales.', 2, true, CURRENT_TIMESTAMP),
(15,8, 'NAT_1', 'Disfruto observar y clasificar plantas, animales o el entorno natural.', 1, true, CURRENT_TIMESTAMP),
(16,8, 'NAT_2', 'Me interesa aprender sobre ecología y conservación.', 2, true, CURRENT_TIMESTAMP);

-- Fin del script

