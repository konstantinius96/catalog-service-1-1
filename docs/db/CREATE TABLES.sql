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


-- Outbox for transactional events
CREATE TABLE IF NOT EXISTS outbox_event (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(100) NOT NULL,
    aggregate_id VARCHAR(100) NOT NULL,
    topic VARCHAR(255) NOT NULL,
    event_key VARCHAR(255) NOT NULL,
    event_type VARCHAR(255) NOT NULL,
    payload TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    published_at TIMESTAMPTZ,
    retry_count INTEGER NOT NULL DEFAULT 0,
    last_error TEXT
);

CREATE INDEX IF NOT EXISTS idx_outbox_event_unpublished
    ON outbox_event (id)
    WHERE published_at IS NULL;


-- Вставка тестовых данных

-- Users
INSERT INTO users (login, password_hash, role)
VALUES ('alice', 'hash1', 'ADMIN');
