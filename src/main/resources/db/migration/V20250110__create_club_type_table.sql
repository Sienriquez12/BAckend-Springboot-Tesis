-- Script para crear la tabla club_type
-- Ejecutar antes de insertar los datos

CREATE TABLE club_type (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000),
    icon VARCHAR(100),
    color VARCHAR(20),
    order_index INT,
    record_status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Índices para mejorar el rendimiento
CREATE INDEX idx_club_type_record_status ON club_type(record_status);
CREATE INDEX idx_club_type_order_index ON club_type(order_index);

-- Comentarios en las columnas
ALTER TABLE club_type MODIFY COLUMN id BIGINT AUTO_INCREMENT COMMENT 'Identificador único del tipo de club';
ALTER TABLE club_type MODIFY COLUMN name VARCHAR(255) NOT NULL UNIQUE COMMENT 'Nombre del tipo de club (ej: Deportivo, Académico, Cultural)';
ALTER TABLE club_type MODIFY COLUMN description VARCHAR(1000) COMMENT 'Descripción del tipo de club y su enfoque';
ALTER TABLE club_type MODIFY COLUMN icon VARCHAR(100) COMMENT 'Icono o clase CSS para representar visualmente el tipo';
ALTER TABLE club_type MODIFY COLUMN color VARCHAR(20) COMMENT 'Color hexadecimal asociado al tipo de club para UI';
ALTER TABLE club_type MODIFY COLUMN order_index INT COMMENT 'Orden de visualización en listas';
ALTER TABLE club_type MODIFY COLUMN record_status BOOLEAN DEFAULT TRUE COMMENT 'Estado del registro (activo/inactivo)';
ALTER TABLE club_type MODIFY COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación';
ALTER TABLE club_type MODIFY COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización';
