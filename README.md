# TaskTracker — Трекер задач для команды

JavaFX desktop-приложение с Supabase (PostgreSQL) в качестве бэкенда.

## Технологический стек

| Компонент | Технология |
|-----------|-----------|
| UI        | JavaFX 21 + FXML |
| HTTP      | OkHttp 4.12 |
| JSON      | Gson 2.10 |
| БД/Auth   | Supabase REST API + Supabase Auth |
| Сборка    | Maven |

## Структура проекта

```
TaskTracker/
├── pom.xml
├── database.sql                      ← SQL для Supabase
└── src/main/
    ├── java/com/tasktracker/
    │   ├── Launcher.java             ← Точка входа
    │   ├── MainApp.java              ← JavaFX Application
    │   ├── model/
    │   │   └── Task.java             ← Модель задачи
    │   ├── service/
    │   │   ├── AuthService.java      ← Supabase Auth
    │   │   └── TaskService.java      ← CRUD задач
    │   ├── controller/
    │   │   ├── LoginController.java  ← Экран входа
    │   │   ├── MainController.java   ← Главный экран
    │   │   └── TaskDialogController.java ← Диалог задачи
    │   └── util/
    │       ├── SupabaseConfig.java   ← Настройки API
    │       └── HttpClient.java       ← HTTP-обёртка
    └── resources/com/tasktracker/
        ├── view/
        │   ├── LoginView.fxml
        │   ├── MainView.fxml
        │   └── TaskDialog.fxml
        └── css/
            └── style.css
```

## Настройка Supabase

1. Создайте проект на https://supabase.com
2. В **SQL Editor** выполните файл `database.sql`
3. Перейдите в **Settings → API** и скопируйте:
   - Project URL
   - anon public key
   - service_role key
4. Вставьте значения в `SupabaseConfig.java`

## Настройка приложения

Откройте файл `src/main/java/com/tasktracker/util/SupabaseConfig.java`:

```java
public static final String SUPABASE_URL = "https://XXXXXXXX.supabase.co";
public static final String ANON_KEY     = "eyJ...";
public static final String SERVICE_KEY  = "eyJ...";
```

## Запуск

```bash
# С плагином JavaFX Maven Plugin
mvn javafx:run

# Или собрать jar и запустить
mvn package
java -jar target/TaskTracker-1.0-SNAPSHOT.jar
```

## Функциональность

- **Вход / Регистрация** через Supabase Auth
- **Просмотр задач** в таблице с фильтрацией по статусу
- **Создание задачи** — название, описание, приоритет, статус, исполнитель
- **Редактирование задачи** — обновление всех полей
- **Удаление задачи** с подтверждением
- **Обновление списка** по кнопке «Обновить»
- **Выход** из системы

## Поля задачи

| Поле        | Значения                           |
|-------------|-------------------------------------|
| Название    | Произвольный текст                  |
| Описание    | Произвольный текст                  |
| Приоритет   | Низкий / Средний / Высокий          |
| Статус      | Новая / В работе / Готово           |
| Исполнитель | Имя члена команды                   |
