CREATE TABLE pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE tratamiento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    diagnostico VARCHAR(300) NOT NULL,
    descripcion VARCHAR(300) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_termino DATE NOT NULL
);

CREATE TABLE insumo (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(300) NOT NULL,
    stock INT NOT NULL
);

CREATE TABLE cita (
    id INT AUTO_INCREMENT PRIMARY KEY,
    motivo VARCHAR(300) NOT NULL,
    fecha_hora DATETIME NOT NULL,
    estado VARCHAR(20) NOT NULL,
    mascota_id BIGINT NOT NULL
);

CREATE TABLE tratamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cita_id INT NOT NULL,
    tratamiento_id INT NOT NULL,
    FOREIGN KEY (cita_id) REFERENCES cita(id),
    FOREIGN KEY (tratamiento_id) REFERENCES tratamiento(id)
);

CREATE TABLE insumos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tratamiento_id INT NOT NULL,
    insumo_id INT NOT NULL,
    FOREIGN KEY (tratamiento_id) REFERENCES tratamiento(id),
    FOREIGN KEY (insumo_id) REFERENCES insumo(id)
);

CREATE TABLE pagos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cita_id INT NOT NULL,
    pago_id INT NOT NULL,
    FOREIGN KEY (cita_id) REFERENCES cita(id),
    FOREIGN KEY (pago_id) REFERENCES pago(id)
);

CREATE TABLE veterinarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cita_id INT NOT NULL,
    veterinario_id BIGINT NOT NULL,
    FOREIGN KEY (cita_id) REFERENCES cita(id)
);