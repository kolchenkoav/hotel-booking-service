package com.example.hotel_booking_service.service;

import com.example.hotel_booking_service.entity.Statistic;
import com.example.hotel_booking_service.kafka.dto.KafkaBookingEvent;
import com.example.hotel_booking_service.kafka.dto.KafkaUserRegistrationEvent;
import com.example.hotel_booking_service.repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Сервис для обработки статистики регистраций и бронирований.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticRepository statisticRepository;

    /**
     * Сохраняет событие регистрации пользователя.
     *
     * @param event событие регистрации пользователя
     */
    public void saveUserRegistration(KafkaUserRegistrationEvent event) {
        log.info("StatisticsService -> saveUserRegistration(KafkaUserRegistrationEvent event) -> event.getUserId(): {}", event.getUserId());
        Statistic stat = new Statistic();
        stat.setUserId(event.getUserId());
        stat.setEventType("REGISTRATION");
        //statisticRepository.save(stat);
    }

    /**
     * Сохраняет событие бронирования.
     *
     * @param event событие бронирования
     */
    public void saveBooking(KafkaBookingEvent event) {
        log.info("Сохраняем событие бронирования userId: {}, checkIn: {}, checkOut: {}",
                event.getUserId(), event.getCheckIn(), event.getCheckOut());
        Statistic stat = new Statistic();
        stat.setUserId(event.getUserId());
        stat.setCheckIn(event.getCheckIn());
        stat.setCheckOut(event.getCheckOut());
        stat.setEventType("BOOKING");
        //statisticRepository.save(stat);
    }

    /**
     * Экспортирует статистику в CSV файл.
     *
     * @return путь к созданному CSV файлу
     * @throws IOException если произошла ошибка ввода-вывода
     */
    public String exportStatisticsToCSV() throws IOException {
        List<Statistic> stats = statisticRepository.findAll();
        String filePath = "statistics.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("User ID,Check-In,Check-Out,Event Type\n");
            for (Statistic stat : stats) {
                writer.write(stat.getUserId() + "," +
                        (stat.getCheckIn() != null ? stat.getCheckIn() : "") + "," +
                        (stat.getCheckOut() != null ? stat.getCheckOut() : "") + "," +
                        stat.getEventType() + "\n");
            }
        }
        return filePath;
    }
}
