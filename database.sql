CREATE TABLE drone (
   serial_number VARCHAR(50) PRIMARY KEY,
   model VARCHAR(50) NOT NULL,
   weight_limit INTEGER NOT NULL,
   battery_capacity INTEGER NOT NULL,
   state VARCHAR(20) NOT NULL
);

CREATE TABLE medication (
    code VARCHAR(50) PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    weight INTEGER NOT NULL,
    image VARCHAR(255) NOT NULL
);

CREATE TABLE drone_medication (
    serial_number VARCHAR(50) REFERENCES drone(serial_number),
    code VARCHAR(50) REFERENCES medication(code),
    PRIMARY KEY (serial_number, code)
);


-- Insertar más drones
INSERT INTO drone (serial_number, model, weight_limit, battery_capacity, state) VALUES
   ('4567-8901', 'Modelo 3', 10000, 80, 'IDLE'),
   ('5678-9012', 'Modelo 1', 5000, 60, 'LOADED'),
   ('6789-0123', 'Modelo 2', 7500, 50, 'DELIVERING');

-- Insertar más medicaciones
INSERT INTO medication (code, name, weight, image) VALUES
   ('JKL-456', 'Medicación 4', 15, 'https://example.com/image4.png'),
   ('MNO-789', 'Medicación 5', 25, 'https://example.com/image5.png'),
   ('PQR-012', 'Medicación 6', 35, 'https://example.com/image6.png');

-- Asignar medicaciones a más drones
INSERT INTO drone_medication (serial_number, code) VALUES
   ('4567-8901', 'JKL-456'),
   ('4567-8901', 'MNO-789'),
   ('5678-9012', 'PQR-012'),
   ('6789-0123', 'JKL-456'),
   ('6789-0123', 'MNO-789'),
   ('6789-0123', 'PQR-012');