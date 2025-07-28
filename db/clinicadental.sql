CREATE DATABASE clinicadentaldb;

USE clinicadentaldb;

CREATE TABLE pacientes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    identidad VARCHAR(20) NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    telefono VARCHAR(20) NOT NULL
);

CREATE TABLE citas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_paciente INT NOT NULL,
    fecha VARCHAR(20) NOT NULL,
    hora VARCHAR(20) NOT NULL,
    procedimiento VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_paciente) REFERENCES pacientes(id)
);

-- Tablas para el sistema de inventario

CREATE TABLE categorias (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    descripcion VARCHAR(200)
);

CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    email VARCHAR(100)
);

CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categorias(id)
);

CREATE TABLE productos_proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_proveedor INT NOT NULL,
    precio_proveedor DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES productos(id),
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id)
);

-- Insertar datos de ejemplo para categorías
INSERT INTO categorias (nombre, descripcion) VALUES 
('Anestésicos', 'Anestésicos locales y medicamentos para el dolor'),
('Instrumentos Quirúrgicos', 'Instrumentos para cirugía dental y extracciones'),
('Materiales Restaurativos', 'Materiales para restauraciones y empastes'),
('Equipos de Diagnóstico', 'Equipos para diagnóstico y exploración dental'),
('Productos de Higiene', 'Productos para higiene bucal del paciente'),
('Materiales de Laboratorio', 'Materiales para laboratorio dental'),
('Equipos de Tratamiento', 'Equipos para tratamientos dentales'),
('Consumibles', 'Productos desechables y consumibles'),
('Ortodoncia', 'Productos y materiales para ortodoncia'),
('Endodoncia', 'Productos y materiales para endodoncia'),
('Prótesis', 'Materiales y productos para prótesis dentales'),
('Radiología', 'Equipos y materiales para radiología dental');

-- Insertar datos de ejemplo para proveedores
INSERT INTO proveedores (nombre, telefono, direccion, email) VALUES 
('Farmacia Central', '555-0101', 'Calle Principal 123', 'info@farmaciacentral.com'),
('Dental Supplies Co.', '555-0202', 'Avenida Comercial 456', 'ventas@dentalsupplies.com'),
('Medical Equipment Pro', '555-0303', 'Zona Industrial 789', 'contacto@medicalequipment.com');

-- Insertar datos de ejemplo para productos
INSERT INTO productos (nombre, descripcion, precio, stock, id_categoria) VALUES 
-- Anestésicos (id_categoria = 1)
('Lidocaína 2%', 'Anestésico local para procedimientos dentales', 8.50, 50, 1),
('Articaína 4%', 'Anestésico local con epinefrina', 12.75, 40, 1),
('Mepivacaína 3%', 'Anestésico local sin vasoconstrictor', 9.25, 35, 1),

-- Instrumentos Quirúrgicos (id_categoria = 2)
('Fórceps Universales', 'Fórceps para extracción dental', 45.00, 15, 2),
('Elevador Dental', 'Elevador para extracción de raíces', 35.00, 20, 2),
('Bisturí Dental', 'Bisturí para cirugía dental', 25.00, 30, 2),
('Tijeras Quirúrgicas', 'Tijeras para cirugía dental', 28.00, 25, 2),

-- Materiales Restaurativos (id_categoria = 3)
('Amalgama Dental', 'Material de restauración tradicional', 25.00, 30, 3),
('Resina Compuesta', 'Material estético para restauraciones', 35.00, 40, 3),
('Ionómero de Vidrio', 'Material restaurador adhesivo', 30.00, 25, 3),
('Cemento de Fosfato', 'Cemento para restauraciones temporales', 15.00, 50, 3),

-- Equipos de Diagnóstico (id_categoria = 4)
('Espejo Dental', 'Espejo para exploración bucal', 12.50, 50, 4),
('Sonda Periodontal', 'Sonda para medición de bolsas', 8.00, 60, 4),
('Lámpara LED Dental', 'Iluminación para procedimientos', 150.00, 10, 4),
('Lupa Dental', 'Lupa para procedimientos detallados', 85.00, 15, 4),

-- Productos de Higiene (id_categoria = 5)
('Cepillo Dental', 'Cepillo de dientes profesional', 3.50, 200, 5),
('Pasta Dental', 'Pasta dental con flúor', 4.25, 150, 5),
('Hilo Dental', 'Hilo dental encerado', 2.75, 300, 5),
('Enjuague Bucal', 'Enjuague antiséptico', 6.50, 100, 5),

-- Materiales de Laboratorio (id_categoria = 6)
('Yeso Dental', 'Yeso para modelos dentales', 18.00, 40, 6),
('Cera de Modelado', 'Cera para modelado dental', 22.00, 35, 6),
('Silicona de Impresión', 'Silicona para impresiones', 45.00, 25, 6),
('Alginato', 'Material para impresiones', 12.00, 60, 6),

-- Equipos de Tratamiento (id_categoria = 7)
('Compresor Dental', 'Compresor para suministro de aire', 500.00, 5, 7),
('Turbina Dental', 'Turbina para preparación dental', 180.00, 12, 7),
('Contra-ángulo', 'Contra-ángulo para preparación', 220.00, 10, 7),
('Jeringa de Aire/Agua', 'Jeringa para limpieza', 35.00, 20, 7),

-- Consumibles (id_categoria = 8)
('Guantes Látex', 'Guantes desechables talla M', 15.00, 500, 8),
('Mascarillas', 'Mascarillas quirúrgicas', 8.00, 400, 8),
('Gorros Quirúrgicos', 'Gorros desechables', 5.00, 300, 8),
('Campos Estériles', 'Campos quirúrgicos', 12.00, 200, 8),

-- Ortodoncia (id_categoria = 9)
('Brackets Metálicos', 'Brackets de ortodoncia', 8.50, 100, 9),
('Arcos Ortodóncicos', 'Arcos de alambre', 12.00, 80, 9),
('Ligaduras Elásticas', 'Ligaduras para brackets', 3.00, 200, 9),
('Cera Ortodóncica', 'Cera para protección', 4.50, 150, 9),

-- Endodoncia (id_categoria = 10)
('Limas Endodóncicas', 'Limas para conductos', 25.00, 60, 10),
('Gutapercha', 'Material de obturación', 18.00, 45, 10),
('Cemento Endodóncico', 'Cemento para conductos', 22.00, 40, 10),
('Irrigante Endodóncico', 'Irrigante para conductos', 15.00, 50, 10),

-- Prótesis (id_categoria = 11)
('Dientes Acrílicos', 'Dientes para prótesis', 35.00, 30, 11),
('Base Acrílica', 'Base para prótesis', 45.00, 25, 11),
('Ganchos Metálicos', 'Ganchos para prótesis', 28.00, 35, 11),
('Cera de Registro', 'Cera para registros', 12.00, 40, 11),

-- Radiología (id_categoria = 12)
('Películas Radiográficas', 'Películas para radiografías', 0.50, 1000, 12),
('Revelador Radiográfico', 'Revelador para películas', 25.00, 20, 12),
('Fijador Radiográfico', 'Fijador para películas', 20.00, 20, 12),
('Protectores de Plomo', 'Protectores radiológicos', 85.00, 15, 12);
