-- =============================================================
--  car_manager_db — SQL Server Init Script
--  Tables: users, cars
--  Includes: constraints, indexes, seed data
-- =============================================================

-- Create database if it does not exist
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'car_manager_db')
BEGIN
    CREATE DATABASE car_manager_db;
END
GO

USE car_manager_db;
GO

-- =============================================================
--  DROP (reverse order to respect FK dependencies)
-- =============================================================
IF OBJECT_ID('dbo.cars', 'U') IS NOT NULL DROP TABLE dbo.cars;
IF OBJECT_ID('dbo.users', 'U') IS NOT NULL DROP TABLE dbo.users;
GO

-- =============================================================
--  TABLE: users
-- =============================================================
CREATE TABLE dbo.users (
                           id            BIGINT IDENTITY(1,1)   NOT NULL,
                           email         VARCHAR(100)           NOT NULL,
                           password_hash VARCHAR(255)           NOT NULL,   -- BCrypt hash
                           full_name     VARCHAR(100)           NOT NULL,
                           created_at    DATETIME2              NOT NULL  DEFAULT GETUTCDATE(),
                           updated_at    DATETIME2              NOT NULL  DEFAULT GETUTCDATE()

                           CONSTRAINT PK_users        PRIMARY KEY (id),
                           CONSTRAINT UQ_users_email  UNIQUE      (email),
                           CONSTRAINT CHK_users_email CHECK       (email LIKE '%_@_%._%')
);
GO

-- =============================================================
--  TABLE: cars
-- =============================================================
CREATE TABLE dbo.cars (
                          id         BIGINT IDENTITY(1,1)  NOT NULL,
                          user_id    BIGINT                NOT NULL,
                          brand      VARCHAR(50)           NOT NULL,
                          model      VARCHAR(50)           NOT NULL,
                          year       INT                   NOT NULL,
                          plate      VARCHAR(10)           NOT NULL,
                          color      VARCHAR(30)           NOT NULL,
                          photo_url  VARCHAR(500)          NULL,           -- simulated field, not functional
                          is_active  BIT                   NOT NULL  DEFAULT 1,
                          created_at DATETIME2             NOT NULL  DEFAULT GETUTCDATE(),
                          updated_at DATETIME2             NOT NULL  DEFAULT GETUTCDATE(),

                          CONSTRAINT PK_cars         PRIMARY KEY (id),
                          CONSTRAINT FK_cars_users   FOREIGN KEY (user_id) REFERENCES dbo.users(id)
                              ON DELETE CASCADE
                              ON UPDATE CASCADE,
                          CONSTRAINT UQ_cars_plate   UNIQUE (plate),

    -- Year cannot be in the future (current year + 1 allowed for next-year models)
                          CONSTRAINT CHK_cars_year   CHECK (
                              year >= 1900
                              AND year <= YEAR(GETDATE()) + 1
),

    -- Colombian plate format: 3 letters + hyphen + 3 digits (e.g. ABC-123)
    CONSTRAINT CHK_cars_plate  CHECK (
        plate LIKE '[A-Z][A-Z][A-Z]-[0-9][0-9][0-9]'
    )
);
GO

-- =============================================================
--  INDEXES
-- =============================================================
-- Queries by user (listing a user's own cars)
CREATE NONCLUSTERED INDEX IX_cars_user_id
    ON dbo.cars (user_id)
    INCLUDE (brand, model, year, plate, color, is_active);

-- Search by plate or model
CREATE NONCLUSTERED INDEX IX_cars_plate
    ON dbo.cars (plate);

CREATE NONCLUSTERED INDEX IX_cars_brand_year
    ON dbo.cars (brand, year);

-- Fast login lookup by email
CREATE NONCLUSTERED INDEX IX_users_email
    ON dbo.users (email);
GO

-- =============================================================
--  SEED DATA
-- =============================================================

-- Demo users
-- Plain-text passwords (for reference only):
--   Demo User   → password: Admin123!
--   John Doe    → password: Admin123!
--   Maria Lopez → password: Admin123!
-- Hashes are BCrypt $2b$10$... generated with cost=10

INSERT INTO dbo.users (email, password_hash, full_name)
VALUES
    (
        'demo@unitet.com',
        '$2b$10$db6tUWz97eUET2cY6H8foOszsWQ1yh7yJ3V3yOrity0VUXQekh3ia',
        'Demo User'
    ),
    (
        'john.doe@unitet.com',
        '$2b$10$4sI1LjAqMNnWba9sdAQoPO2AC2ZnX.gqodhh5oTyUfsNfcbdK2DEW',
        'John Doe'
    ),
    (
        'maria.lopez@unitet.com',
        '$2b$10$/vEwnVPdpZJVh9VPf/8x2OSo2dMgMyMq4JMZ80pxwDDWxnDAo0Zs2',
        'Maria Lopez'
    );
GO

-- Cars for Demo User (id=1)
INSERT INTO dbo.cars (user_id, brand, model, year, plate, color, photo_url)
VALUES
    (1, 'Chevrolet', 'Spark',   2020, 'ABC-123', 'Red',   NULL),
    (1, 'Renault',   'Logan',   2019, 'XYZ-789', 'White', NULL),
    (1, 'Mazda',     'CX-5',    2022, 'MZD-456', 'Gray',  NULL);

-- Cars for John Doe (id=2)
INSERT INTO dbo.cars (user_id, brand, model, year, plate, color, photo_url)
VALUES
    (2, 'Toyota', 'Corolla', 2021, 'TYT-321', 'Black', NULL),
    (2, 'Honda',  'CR-V',    2023, 'HND-654', 'Blue',  NULL);

-- Cars for Maria Lopez (id=3)
INSERT INTO dbo.cars (user_id, brand, model, year, plate, color, photo_url)
VALUES
    (3, 'Kia',     'Sportage', 2022, 'KIA-111', 'Silver', NULL),
    (3, 'Hyundai', 'Tucson',   2021, 'HYN-222', 'White',  NULL),
    (3, 'Nissan',  'Kicks',    2020, 'NSN-333', 'Red',    NULL);
GO

-- =============================================================
--  VERIFICATION QUERIES
-- =============================================================

-- Users with total cars count
SELECT
    u.id,
    u.email,
    u.full_name,
    COUNT(c.id) AS total_cars
FROM dbo.users u
         LEFT JOIN dbo.cars c ON c.user_id = u.id
GROUP BY u.id, u.email, u.full_name
ORDER BY u.id;
GO

-- All cars with owner info
SELECT
    c.id          AS car_id,
    c.plate,
    c.brand,
    c.model,
    c.year,
    c.color,
    c.is_active,
    u.id          AS user_id,
    u.full_name   AS owner,
    u.email       AS owner_email
FROM dbo.cars c
         JOIN dbo.users u ON u.id = c.user_id
ORDER BY u.id, c.year DESC;
GO