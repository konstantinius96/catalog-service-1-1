--Создание допов
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Создание таблиц

-- Пользователи
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    login VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    registration_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    role VARCHAR(20) NOT NULL
);


-- Автокомпоненты
CREATE TABLE IF NOT EXISTS auto_component (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
	specification TEXT,
    specification_jsonb JSONB
);


-- Изображения компонентов
CREATE TABLE IF NOT EXISTS auto_component_image (
    id BIGSERIAL PRIMARY KEY,
    url TEXT NOT NULL,
    description TEXT,
    auto_component_id UUID NOT NULL
        REFERENCES auto_component(id) ON DELETE CASCADE
);


-- Избранное
CREATE TABLE IF NOT EXISTS favourite (
    user_id UUID NOT NULL
        REFERENCES users(id) ON DELETE CASCADE,
    auto_component_id UUID NOT NULL
        REFERENCES auto_component(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, auto_component_id)
);


-- Отношения компонентов
CREATE TABLE IF NOT EXISTS auto_component_relation (
    from_component UUID NOT NULL
        REFERENCES auto_component(id) ON DELETE CASCADE,
    to_component UUID NOT NULL
        REFERENCES auto_component(id) ON DELETE CASCADE,
    relation_type VARCHAR(50) NOT NULL,
    PRIMARY KEY (from_component, to_component, relation_type)
);


-- Вставка тестовых данных

-- Users
INSERT INTO users (login, password_hash, role)
VALUES ('alice', 'hash1', 'ADMIN');
