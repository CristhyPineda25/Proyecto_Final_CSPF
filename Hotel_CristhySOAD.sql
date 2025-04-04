-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS hotel_CristhySOAD;
USE hotel_CristhySOAD;

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL,
    descripcion VARCHAR(255)
);

-- Tabla de empleados
CREATE TABLE IF NOT EXISTS empleados (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    fecha_contratacion DATE NOT NULL,
    salario DECIMAL(10, 2) NOT NULL,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    id_empleado INT,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    salt VARCHAR(255),
    id_rol INT NOT NULL,
    FOREIGN KEY (id_empleado) REFERENCES empleados(id_empleado) ON DELETE SET NULL,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

-- Tabla de huéspedes
CREATE TABLE IF NOT EXISTS huespedes (
    id_huesped INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(255)
);

-- Tabla de habitaciones
CREATE TABLE IF NOT EXISTS habitaciones (
    id_habitacion INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(50) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    capacidad INT NOT NULL,
    disponible BOOLEAN NOT NULL DEFAULT TRUE
);

-- Tabla de reservas
CREATE TABLE IF NOT EXISTS reservas (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_huesped INT NOT NULL,
    id_habitacion INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente',
    FOREIGN KEY (id_huesped) REFERENCES huespedes(id_huesped),
    FOREIGN KEY (id_habitacion) REFERENCES habitaciones(id_habitacion)
);

-- Tabla de pagos
CREATE TABLE IF NOT EXISTS pagos (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_reserva INT NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    fecha_pago DATE NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_reserva) REFERENCES reservas(id_reserva)
);

-- Insertar datos de prueba

-- Roles
INSERT INTO roles (nombre_rol, descripcion) VALUES 
('Administrador', 'Acceso completo al sistema'),
('Recepcionista', 'Gestión de huéspedes, reservas y pagos'),
('Limpieza', 'Gestión de habitaciones');

-- Empleados
INSERT INTO empleados (nombre, apellido, cargo, fecha_contratacion, salario, telefono, email) VALUES 
('Juan', 'Pérez', 'Gerente', '2020-01-15', 3500.00, '555-1234', 'juan.perez@hotel.com'),
('María', 'López', 'Recepcionista', '2020-03-20', 1800.00, '555-5678', 'maria.lopez@hotel.com'),
('Carlos', 'Gómez', 'Limpieza', '2020-05-10', 1200.00, '555-9012', 'carlos.gomez@hotel.com');

-- Usuarios (contraseña: admin123)
INSERT INTO usuarios (id_empleado, usuario, contrasena, salt, id_rol) VALUES 
(1, 'admin', 'admin123', NULL, 1),
(2, 'recepcion', 'recepcion123', NULL, 2),
(3, 'limpieza', 'limpieza123', NULL, 3);

-- Huéspedes
INSERT INTO huespedes (nombre, apellido, email, telefono, direccion) VALUES 
('Pedro', 'Martínez', 'pedro.martinez@email.com', '555-1111', 'Calle Principal 123'),
('Ana', 'Rodríguez', 'ana.rodriguez@email.com', '555-2222', 'Avenida Central 456'),
('Luis', 'Sánchez', 'luis.sanchez@email.com', '555-3333', 'Plaza Mayor 789');

-- Habitaciones
INSERT INTO habitaciones (tipo, precio, capacidad, disponible) VALUES 
('Individual', 80.00, 1, TRUE),
('Doble', 120.00, 2, TRUE),
('Suite', 200.00, 4, TRUE),
('Familiar', 150.00, 3, TRUE),
('Deluxe', 250.00, 2, TRUE);

-- Reservas
INSERT INTO reservas (id_huesped, id_habitacion, fecha_inicio, fecha_fin, estado) VALUES 
(1, 2, '2023-06-10', '2023-06-15', 'Confirmada'),
(2, 3, '2023-06-20', '2023-06-25', 'Pendiente'),
(3, 1, '2023-07-05', '2023-07-10', 'Confirmada');

-- Pagos
INSERT INTO pagos (id_reserva, monto, fecha_pago, metodo_pago) VALUES 
(1, 600.00, '2023-06-01', 'Tarjeta'),
(3, 400.00, '2023-06-25', 'Efectivo');