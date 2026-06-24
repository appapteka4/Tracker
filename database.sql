-- ═══════════════════════════════════════════════════════
-- TaskTracker — скрипт создания БД в Supabase
-- Выполнить в: Supabase Dashboard → SQL Editor → New query
-- ═══════════════════════════════════════════════════════

-- 1. Таблица задач
CREATE TABLE IF NOT EXISTS tasks (
    id          SERIAL PRIMARY KEY,
    title       TEXT        NOT NULL,
    description TEXT        DEFAULT '',
    status      TEXT        NOT NULL DEFAULT 'new'
                            CHECK (status IN ('new', 'in_progress', 'done')),
    priority    TEXT        NOT NULL DEFAULT 'medium'
                            CHECK (priority IN ('low', 'medium', 'high')),
    assignee    TEXT        NOT NULL DEFAULT '',
    created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 2. Включить Row Level Security
ALTER TABLE tasks ENABLE ROW LEVEL SECURITY;

-- 3. Политика: аутентифицированные пользователи видят все задачи
CREATE POLICY "auth_select" ON tasks
    FOR SELECT TO authenticated USING (true);

-- 4. Политика: аутентифицированные пользователи могут создавать задачи
CREATE POLICY "auth_insert" ON tasks
    FOR INSERT TO authenticated WITH CHECK (true);

-- 5. Политика: аутентифицированные пользователи могут обновлять задачи
CREATE POLICY "auth_update" ON tasks
    FOR UPDATE TO authenticated USING (true);

-- 6. Политика: аутентифицированные пользователи могут удалять задачи
CREATE POLICY "auth_delete" ON tasks
    FOR DELETE TO authenticated USING (true);

-- 7. Несколько тестовых задач
INSERT INTO tasks (title, description, status, priority, assignee) VALUES
    ('Настроить сервер разработки',
     'Развернуть Docker-окружение на dev-сервере',
     'done', 'high', 'Иванов А.'),
    ('Разработать модуль авторизации',
     'JWT-аутентификация с refresh-токенами',
     'in_progress', 'high', 'Петров С.'),
    ('Написать документацию к API',
     'Swagger/OpenAPI спецификация для всех эндпоинтов',
     'new', 'medium', 'Сидорова М.'),
    ('Покрыть тестами сервисный слой',
     'Unit-тесты для TaskService и AuthService',
     'new', 'medium', 'Тарасов И.К.'),
    ('Оптимизировать SQL-запросы',
     'Добавить индексы на поля status и assignee',
     'new', 'low', 'Козлов Д.');
