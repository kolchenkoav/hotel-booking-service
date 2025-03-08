**artifact: hotel-booking-service-README**  
**type: text/markdown**  
**title: Документация для сервиса бронирования отелей**


# Сервис бронирования отелей 🏨

Микросервис для управления бронированиями отелей с использованием современных технологий: 
Spring Boot, PostgreSQL, MongoDB, Apache Kafka и Docker. Проект демонстрирует реализацию 
распределенной системы с обработкой событий, безопасностью и аналитикой.

---

## 📋 Требования проекта
- **Docker** и **Docker Compose** для запуска инфраструктуры.
- **Java 17+** и **Gradle** для сборки проекта.
- Доступ к портам: `5432` (PostgreSQL), `5050` (pgAdmin), `27017` (MongoDB), `9092` (Kafka).

---

## 🛠 Зависимости
- **PostgreSQL**: Хранение данных о бронированиях, отелях и пользователях.
- **MongoDB**: Сбор статистики (регистрации, бронирования).
- **Apache Kafka**: Обработка событий в реальном времени.
- **Zookeeper**: Координация для Kafka.
- **PgAdmin**: Управление PostgreSQL через веб-интерфейс.

---

## 🚀 Запуск приложения

### 1. Запустите инфраструктуру через Docker:
```bash
docker-compose -f docker/docker-compose.yaml up -d
```
Проверьте статус сервисов:
```bash
docker ps --format "table {{.Names}}\t{{.Status}}"
```

### 2. Соберите и запустите приложение:
```bash
./gradlew bootRun
```

---

## 🔐 Безопасность
- **Роли**: `ROLE_ADMIN` (управление отелями/бронированиями), `ROLE_USER` (просмотр).
- **Пример запроса с аутентификацией**:
```bash
curl -u admin:password http://localhost:8080/api/v1/hotels
```

---

## 📡 Примеры API

### Создание бронирования (POST `/api/v1/bookings`)
```http
POST /api/v1/bookings HTTP/1.1
Content-Type: application/json

{
  "userId": 1,
  "roomId": 1,
  "checkIn": "2024-10-01",
  "checkOut": "2024-10-05"
}
```

### Получение списка отелей (GET `/api/v1/hotels`)
```java
// Пример фильтрации через спецификации
HotelFilter filter = new HotelFilter(
  null, "Grand", null, "Paris", 
  null, 5, List.of(4, 5), null
);
Page<Hotel> hotels = hotelRepository.findAll(filter.toSpecification(), pageable);
```

---

## 📊 Kafka в действии
События автоматически генерируются при:
- Регистрации пользователя (топик `user_registration`).
- Создании бронирования (топик `room_booking`).

**Пример потребителя Kafka**:
```java
@KafkaListener(topics = "user_registration", groupId = "hotel_service")
public void handleRegistration(KafkaUserRegistrationEvent event) {
  log.info("Новый пользователь: {}", event.getUserId());
  statisticsService.saveUserRegistration(event);
}
```

---

## 🧪 Тестирование
Интеграционные тесты используют Testcontainers для поднятия PostgreSQL.  
Пример теста контроллера:
```java
@Test
@WithMockUser(roles = "ADMIN")
void testCreateHotel() {
  HotelDto dto = new HotelDto("Plaza", "Лучший отель", "Москва", 2);
  mockMvc.perform(post("/api/v1/hotels").content(toJson(dto)))
    .andExpect(status().isCreated());
}
```

---

## 🎯 Итог
Проект реализует:
- Микросервисную архитектуру с Docker.
- Распределенную обработку событий через Kafka.
- Безопасность на основе ролей.
- Гибкую фильтрацию данных через JPA Specifications.

Для предложений или вопросов создавайте Issues в репозитории.  
**Happy coding!** 👨💻👩💻
```