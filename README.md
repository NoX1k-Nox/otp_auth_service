# OTP Auth Service

Проект `otp-auth-service` — это Java-приложение для генерации, валидации и отправки одноразовых паролей (OTP) пользователям через различные каналы (email, Telegram, файл). Также реализована регистрация и аутентификация пользователей.

## Как пользоваться сервисом
Сервис запускается локально на порту 8080. После запуска доступны следующие HTTP-эндпоинты:

### Регистрация пользователя

POST `/register`
```json
{
  "login": "testuser",
  "password": "secret123",
  "role": "USER"
}
```

### Аутентификация (вход)

POST `/login`
```json
{
  "login": "testuser",
  "password": "secret123"
}
```
Ответ при успехе:
```json
{
  "message": "Login successful"
}
```

### Генерация OTP

POST `/otp/generate`
```json
{
  "userId": 1,
  "operationId": "confirm-payment"
}
```

Ответ:
```json
{
  "code": "894712"
}
```

### Валидация OTP
POST `/otp/validate`

```json
{
  "otpCode": "894712"
}
```

## Как протестировать код

1. Запустить PostgreSQL и создать базу данных:
```sql
CREATE DATABASE otpdb;
CREATE USER "user" WITH ENCRYPTED PASSWORD 'password';
GRANT ALL PRIVILEGES ON DATABASE otpdb TO "user";
```

2. Создать таблицы.
3. Сбидить проект: ./gradlew build
4. Запустить приложение.
