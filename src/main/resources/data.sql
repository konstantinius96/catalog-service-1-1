insert into auto_component (id, name, description, specification, specification_jsonb)
values
    ('11111111-1111-1111-1111-111111111111', 'Двигатель', 'Основной силовой агрегат автомобиля', 'Смесь топлива и воздуха сгорает внутри цилиндров', '{"type":"ice"}'::jsonb),
    ('22222222-2222-2222-2222-222222222222', 'Подвеска', 'Система амортизации и удержания колес', 'Обеспечивает контакт колёс с дорогой', '{"type":"multi-link"}'::jsonb);


CREATE OR REPLACE VIEW component_relations_full AS
SELECT
    r.relation_type,
    from_c.id AS from_id,
    from_c.name AS from_name,
    to_c.id AS to_id,
    to_c.name AS to_name
FROM auto_component_relation r
JOIN auto_component from_c ON from_c.id = r.from_component
JOIN auto_component to_c ON to_c.id = r.to_component;
