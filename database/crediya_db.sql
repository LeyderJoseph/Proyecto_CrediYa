CREATE DATABASE IF NOT EXISTS crediya_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE crediya_db;

CREATE TABLE IF NOT EXISTS persona (
    id_persona INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS empleado (
    id_persona INT UNSIGNED PRIMARY KEY,
    rol ENUM('ASESOR', 'SUPERVISOR', 'GERENTE') NOT NULL,
    salario DECIMAL(15,2) NOT NULL,
    CONSTRAINT chk_empleado_salario CHECK (salario > 0),
    CONSTRAINT fk_empleado_persona
        FOREIGN KEY (id_persona) REFERENCES persona(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS cliente (
    id_persona INT UNSIGNED PRIMARY KEY,
    telefono VARCHAR(20) NOT NULL,
    CONSTRAINT fk_cliente_persona
        FOREIGN KEY (id_persona) REFERENCES persona(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS politica_credito (
    id_politica INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    tipo_interes ENUM('COMPUESTO') NOT NULL DEFAULT 'COMPUESTO',
    tasa_interes_mensual DECIMAL(5,2) NOT NULL,
    monto_minimo DECIMAL(15,2) NOT NULL,
    monto_maximo DECIMAL(15,2) NOT NULL,
    plazo_minimo_meses TINYINT UNSIGNED NOT NULL,
    plazo_maximo_meses TINYINT UNSIGNED NOT NULL,
    tope_asesor DECIMAL(15,2) NOT NULL,
    tope_supervisor DECIMAL(15,2) NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_politica_tasa CHECK (tasa_interes_mensual > 0),
    CONSTRAINT chk_politica_montos CHECK (monto_minimo > 0 AND monto_maximo >= monto_minimo),
    CONSTRAINT chk_politica_plazos CHECK (plazo_minimo_meses > 0 AND plazo_maximo_meses >= plazo_minimo_meses),
    CONSTRAINT chk_politica_topes CHECK (tope_asesor >= monto_minimo AND tope_supervisor > tope_asesor AND monto_maximo > tope_supervisor)
);

CREATE TABLE IF NOT EXISTS prestamo (
    id_prestamo INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT UNSIGNED NOT NULL,
    id_empleado_radicador INT UNSIGNED NOT NULL,
    id_empleado_aprobador INT UNSIGNED NULL,
    fecha_solicitud DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_aprobacion DATETIME NULL,
    capital DECIMAL(15,2) NOT NULL,
    tipo_interes ENUM('COMPUESTO') NOT NULL,
    tasa_interes_mensual DECIMAL(5,2) NOT NULL,
    plazo_meses TINYINT UNSIGNED NOT NULL,
    interes_total DECIMAL(15,2) NOT NULL,
    monto_total DECIMAL(15,2) NOT NULL,
    valor_cuota DECIMAL(15,2) NOT NULL,
    saldo_pendiente DECIMAL(15,2) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    estado ENUM('PENDIENTE', 'ACTIVO', 'PAGADO', 'RECHAZADO', 'VENCIDO') NOT NULL DEFAULT 'PENDIENTE',
    observacion VARCHAR(255) NULL,
    INDEX idx_prestamo_cliente (id_cliente),
    INDEX idx_prestamo_estado (estado),
    INDEX idx_prestamo_fecha_solicitud (fecha_solicitud),
    CONSTRAINT chk_prestamo_capital CHECK (capital > 0),
    CONSTRAINT chk_prestamo_tasa CHECK (tasa_interes_mensual > 0),
    CONSTRAINT chk_prestamo_plazo CHECK (plazo_meses > 0),
    CONSTRAINT chk_prestamo_valores CHECK (interes_total >= 0 AND monto_total >= capital AND valor_cuota > 0 AND saldo_pendiente >= 0 AND saldo_pendiente <= monto_total),
    CONSTRAINT fk_prestamo_cliente
        FOREIGN KEY (id_cliente) REFERENCES cliente(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_prestamo_radicador
        FOREIGN KEY (id_empleado_radicador) REFERENCES empleado(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_prestamo_aprobador
        FOREIGN KEY (id_empleado_aprobador) REFERENCES empleado(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS historial_aprobacion (
    id_historial INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_prestamo INT UNSIGNED NOT NULL,
    id_empleado INT UNSIGNED NOT NULL,
    rol_empleado ENUM('ASESOR', 'SUPERVISOR', 'GERENTE') NOT NULL,
    accion ENUM('RADICADO', 'ESCALADO', 'APROBADO', 'RECHAZADO') NOT NULL,
    fecha_accion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacion VARCHAR(255) NULL,
    CONSTRAINT fk_historial_prestamo
        FOREIGN KEY (id_prestamo) REFERENCES prestamo(id_prestamo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_historial_empleado
        FOREIGN KEY (id_empleado) REFERENCES empleado(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS pago (
    id_pago INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_prestamo INT UNSIGNED NOT NULL,
    id_empleado_registrador INT UNSIGNED NOT NULL,
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    valor_total DECIMAL(15,2) NOT NULL,
    observacion VARCHAR(255) NULL,
    INDEX idx_pago_prestamo (id_prestamo),
    INDEX idx_pago_fecha (fecha_pago),
    CONSTRAINT chk_pago_valor CHECK (valor_total > 0),
    CONSTRAINT fk_pago_prestamo
        FOREIGN KEY (id_prestamo) REFERENCES prestamo(id_prestamo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_pago_empleado
        FOREIGN KEY (id_empleado_registrador) REFERENCES empleado(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS detalle_pago (
    id_detalle_pago INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_pago INT UNSIGNED NOT NULL,
    metodo_pago ENUM('EFECTIVO', 'TRANSFERENCIA') NOT NULL,
    tipo_transferencia ENUM('PSE', 'NEQUI', 'BANCOLOMBIA', 'DAVIVIENDA') NULL,
    valor DECIMAL(15,2) NOT NULL,
    referencia VARCHAR(80) NULL,
    INDEX idx_detalle_pago_metodo (metodo_pago, tipo_transferencia),
    CONSTRAINT chk_detalle_pago_valor CHECK (valor > 0),
    CONSTRAINT chk_detalle_pago_metodo CHECK (
        (metodo_pago = 'EFECTIVO' AND tipo_transferencia IS NULL)
        OR
        (metodo_pago = 'TRANSFERENCIA' AND tipo_transferencia IS NOT NULL AND referencia IS NOT NULL)
    ),
    CONSTRAINT fk_detalle_pago_pago
        FOREIGN KEY (id_pago) REFERENCES pago(id_pago)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

INSERT INTO politica_credito (
    id_politica,
    tipo_interes,
    tasa_interes_mensual,
    monto_minimo,
    monto_maximo,
    plazo_minimo_meses,
    plazo_maximo_meses,
    tope_asesor,
    tope_supervisor,
    activo
) VALUES (
    1,
    'COMPUESTO',
    1.50,
    500000.00,
    100000000.00,
    1,
    60,
    3000000.00,
    10000000.00,
    TRUE
)
ON DUPLICATE KEY UPDATE
    tipo_interes = VALUES(tipo_interes),
    tasa_interes_mensual = VALUES(tasa_interes_mensual),
    monto_minimo = VALUES(monto_minimo),
    monto_maximo = VALUES(monto_maximo),
    plazo_minimo_meses = VALUES(plazo_minimo_meses),
    plazo_maximo_meses = VALUES(plazo_maximo_meses),
    tope_asesor = VALUES(tope_asesor),
    tope_supervisor = VALUES(tope_supervisor),
    activo = VALUES(activo);

INSERT INTO persona (
    nombre,
    documento,
    correo,
    clave,
    activo
) VALUES (
    'Leyder Joseph Martínez López',
    '1005182668',
    'leyder-joseph.13@hotmail.es',
    '1234',
    TRUE
)
ON DUPLICATE KEY UPDATE
    nombre = VALUES(nombre),
    correo = VALUES(correo),
    clave = VALUES(clave),
    activo = VALUES(activo);

INSERT INTO empleado (
    id_persona,
    rol,
    salario
)
SELECT
    id_persona,
    'GERENTE',
    5000000.00
FROM persona
WHERE documento = '1005182668'
ON DUPLICATE KEY UPDATE
    rol = VALUES(rol),
    salario = VALUES(salario);
