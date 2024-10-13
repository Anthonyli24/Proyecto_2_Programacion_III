CREATE DATABASE Pos;

use Pos;

create table Categoria (
    id  varchar(10)  not null,
    nombre varchar(30) not null,
    Primary Key (id)
    );

create table Clientes(
    id        varchar(10) not null,
    nombre    varchar(50) not null,
    telefono  varchar(15),
    email     varchar(50),
    descuento double,
    Primary Key (id)
);

create table Cajeros(
    id     varchar(10) not null,
    nombre varchar(50) not null,
    Primary Key (id)
);

create table Producto (
       Codigo  varchar(10)  not null,
       descripcion varchar(30) not null,
	   unidadMedida  varchar(20),
	   precioUnitario  double,
	   categoria varchar(10),
       existencias double,
       Primary Key (Codigo)
     );

create table Factura (
    codigoFactura varchar(255),
    Fecha varchar(255),
    nombreCli varchar(255),
    nombreCaje varchar(255),
    importeTotal double,
    Primary Key(codigoFactura)
);

create table Linea (
    numeroFactura varchar(10) NOT NULL,
    numero varchar(10) NOT NULL,
    Producto_codigo varchar(255),
    Cantidad int,
    Descuento double,
    Importe double,
    primary key (numero)
);

ALTER TABLE Linea ADD  Foreign Key (Producto_codigo) REFERENCES Producto(Codigo);
ALTER TABLE Producto ADD Foreign Key (categoria) REFERENCES Categoria(id);

insert into Categoria (id, nombre) values ('CAT-001', 'Frutas y Verduras');
insert into Categoria (id, nombre) values ('CAT-002', 'Carnes y Pescados');
insert into Categoria (id, nombre) values ('CAT-003', 'Lacteos y Huevos');
insert into Categoria (id, nombre) values ('CAT-004', 'Panaderia');
insert into Categoria (id, nombre) values ('CAT-005', 'Bebidas');
insert into Categoria (id, nombre) values ('CAT-006', 'Congelados');
insert into Categoria (id, nombre) values ('CAT-007', 'Productos de Limpieza');
insert into Categoria (id, nombre) values ('CAT-008', 'Cuidado Personal');
insert into Categoria (id, nombre) values ('CAT-009', 'Alimentos en Conserva');
insert into Categoria (id, nombre) values ('CAT-010', 'Cereales y Granos');

INSERT INTO Clientes (id, nombre, telefono, email, descuento) VALUES
('801050876', 'Luis Martinez', '22224444', 'luis.martinez@example.com', 15.0),
('304567890', 'Roberto Fernandez', '22226666', 'roberto.fernandez@example.com', 20.0),
('102341231', 'Carla Rodriguez', '22225555', 'carla.rodriguez@example.com', 10.0),
('405678901', 'Laura Jimenez', '22227777', 'laura.jimenez@example.com', 2.0),
('506789012', 'Pablo Lopez', '22228888', 'pablo.lopez@example.com', 15.0),
('607890123', 'Gabriela Vargas', '22229999', 'gabriela.vargas@example.com', 8.0),
('708901234', 'Manuel Castillo', '22221000', 'manuel.castillo@example.com', 12.0),
('809012345', 'Valeria Morales', '22221100', 'valeria.morales@example.com', 7.0),
('910123456', 'Javier Fernandez', '22221200', 'javier.fernandez@example.com', 13.0),
('112233445', 'Silvia Mora', '22221300', 'silvia.mora@example.com', 0.0),
('223344556', 'Francisco Cordero', '22221400', 'francisco.cordero@example.com', 9.0),
('334455667', 'Isabel Ramirez', '22221500', 'isabel.ramirez@example.com', 14.0),
('445566778', 'Eduardo Alvarez', '22221600', 'eduardo.alvarez@example.com', 11.0),
('556677889', 'Patricia Gonzalez', '22221700', 'patricia.gonzalez@example.com', 6.0),
('667788990', 'Andres Perez', '22221800', 'andres.perez@example.com', 16.0),
('778899001', 'Beatriz Vargas', '22221900', 'beatriz.vargas@example.com', 12.0),
('889900112', 'Diego Moreno', '22222000', 'diego.moreno@example.com', 10.0),
('990011223', 'Maria Sanchez', '22222100', 'maria.sanchez@example.com', 4.0),
('101122334', 'Fernando Romero', '22222200', 'fernando.romero@example.com', 8.0),
('12345678', 'Estimado Cliente', '11112222', 'estimado.cliente@mail.com', 0.0);

INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-001', 'Juan Perez');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-002', 'Maria Gonzalez');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-003', 'Carlos Rodriguez');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-004', 'Ana Lopez');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-005', 'Jorge Fernandez');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-006', 'Sofia Vargas');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-007', 'Miguel Castro');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-008', 'Claudia Moreno');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-009', 'David Rojas');
INSERT INTO Cajeros (id, nombre) VALUES ('CAJ-010', 'Isabella Soto');

INSERT INTO Producto (Codigo, descripcion, unidadMedida, precioUnitario, categoria, existencias) VALUES
('PROD-006', 'Helado de vainilla', 'litro', 3000.0, 'CAT-006', 60),
('PROD-007', 'Detergente en polvo', 'kg', 2800.0, 'CAT-007', 100),
('PROD-008', 'Shampoo anticaspa', 'botella', 2500.0, 'CAT-008', 75),
('PROD-009', 'Atun en lata', 'lata', 1200.0, 'CAT-009', 120),
('PROD-010', 'Arroz blanco', 'kg', 800.0, 'CAT-010', 300),
('PROD-011', 'Zanahorias', 'kg', 700.0, 'CAT-001', 150),
('PROD-013', 'Queso mozzarella', 'kg', 5000.0, 'CAT-003', 60),
('PROD-015', 'Jugo de manzana', 'litro', 1100.0, 'CAT-005', 140),
('PROD-016', 'Pizza congelada', 'unidad', 4500.0, 'CAT-006', 35),
('PROD-017', 'Desinfectante multiusos', 'litro', 2200.0, 'CAT-007', 110),
('PROD-018', 'Crema para el cuerpo', 'botella', 3000.0, 'CAT-008', 55),
('PROD-019', 'Frijoles en lata', 'lata', 900.0, 'CAT-009', 130),
('PROD-020', 'Avena', 'kg', 1200.0, 'CAT-010', 180),
('PROD-021', 'Pina', 'unidad', 1800.0, 'CAT-001', 75),
('PROD-022', 'Chuletas de cerdo', 'kg', 4000.0, 'CAT-002', 65),
('PROD-023', 'Huevos', 'docena', 2000.0, 'CAT-003', 110),
('PROD-024', 'Galletas integrales', 'paquete', 1300.0, 'CAT-004', 90),
('PROD-004', 'Pan integral', 'unidad', 1200.0, 'CAT-004', 80),
('PROD-003', 'Leche entera', 'litro', 900.0, 'CAT-003', 200),
('PROD-002', 'Pollo entero', 'kg', 3500.0, 'CAT-002', 50),
('PROD-005', 'Refresco de naranja', 'litro', 1000.0, 'CAT-005', 149),
('PROD-014', 'Pan dulce', 'unidad', 300.0, 'CAT-004', 90),
('PROD-025', 'Agua embotellada', 'litro', 600.0, 'CAT-005', 199),
('PROD-012', 'Filete de salmon', 'kg', 6500.0, 'CAT-002', 40),
('PROD-001', 'Manzanas rojas', 'kg', 1500.0, 'CAT-001', 100);