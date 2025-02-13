package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Контроллер для управления статистикой.
 */
@RestController
@RequestMapping("/rest/admin-ui/statistics")
@RequiredArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    /**
     * Экспортирует статистику в CSV файл.
     *
     * @return ResponseEntity с FileSystemResource, содержащим CSV файл.
     * @throws IOException если произошла ошибка ввода-вывода.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/export")
    public ResponseEntity<FileSystemResource> exportStatistics() throws IOException {
        String filePath = statisticsService.exportStatisticsToCSV();
        FileSystemResource file = new FileSystemResource(filePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=statistics.csv")
                .body(file);
    }
}
