CREATE TABLE drones (
                       serial_number VARCHAR(50) PRIMARY KEY,
                       model VARCHAR(50) NOT NULL,
                       weight_limit INTEGER NOT NULL,
                       battery_capacity INTEGER NOT NULL,
                       state VARCHAR(20) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE medications (
                            code VARCHAR(50) PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            weight INTEGER NOT NULL,
                            image VARCHAR(255) NOT NULL,
                            created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dronesmedications (
                                  serial_number VARCHAR(50) REFERENCES drones(serial_number),
                                  code VARCHAR(50) REFERENCES medications(code),
                                  PRIMARY KEY (serial_number, code),
                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


-- Insertar más drones
INSERT INTO drones (serial_number, model, weight_limit, battery_capacity, state, created_at, updated_at) VALUES
                                                                                    ('4567-8901', 'Modelo 3', 10000, 80, 'IDLE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                    ('5678-9012', 'Modelo 1', 5000, 60, 'LOADED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                                    ('6789-0123', 'Modelo 2', 7500, 50, 'DELIVERING', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertar más medicaciones
INSERT INTO medications (code, name, weight, image, created_at, updated_at) VALUES
                                                       ('JKL-456', 'Medicación 4', 15, 'https://example.com/image4.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('MNO-789', 'Medicación 5', 25, 'https://example.com/image5.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('PQR-012', 'Medicación 6', 35, 'https://example.com/image6.png', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Asignar medicaciones a más drones
INSERT INTO dronesmedications (serial_number, code, created_at, updated_at) VALUES
                                                       ('4567-8901', 'JKL-456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('4567-8901', 'MNO-789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('5678-9012', 'PQR-012', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('6789-0123', 'JKL-456', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('6789-0123', 'MNO-789', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                       ('6789-0123', 'PQR-012', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);