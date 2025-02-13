package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.web.dto.HotelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("hotel_booking_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
        System.setProperty("spring.datasource.url", postgres.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgres.getUsername());
        System.setProperty("spring.datasource.password", postgres.getPassword());
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        postgres.close();
    }

    @BeforeEach
    void setUp() {
        try {
            hotelRepository.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Успешное создание отеля")
    void testCreateHotel() throws Exception {
        HotelDto hotelDto = new HotelDto(null, "Hotel Name", "Hotel Title", "City", "", 10, 4, 100, 0);
        mockMvc.perform(post("/rest/admin-ui/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hotelDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Hotel Name"))
                .andExpect(jsonPath("$.title").value("Hotel Title"))
                .andExpect(jsonPath("$.city").value("City"))
                .andExpect(jsonPath("$.distance").value(10))
                .andExpect(jsonPath("$.rating").value(4))
                .andExpect(jsonPath("$.numberofratings").value(100));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Тест: Успешное обновление рейтинга отеля")
    void testUpdateHotelRating() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Name");
        hotel.setTitle("Hotel Title");
        hotel.setCity("City");
        hotel.setDistance(10);
        hotel.setRating(4);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        mockMvc.perform(put("/rest/admin-ui/hotels/{id}/rating", hotel.getId())
                        .param("newMark", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4)) // Ожидаем, что рейтинг изменится
                .andExpect(jsonPath("$.numberofratings").value(101)); // Ожидаем, что количество оценок увеличится
    }

    @Test
    @Order(3)
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Тест: Успешное обновление отеля")
    void testUpdateHotel() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Name");
        hotel.setTitle("Hotel Title");
        hotel.setCity("City");
        hotel.setDistance(10);
        hotel.setRating(4);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        HotelDto updatedHotelDto = new HotelDto(1L, "Updated Hotel Name", "Updated Hotel Title", "Updated City", "",20, 5, 200, 0);
        mockMvc.perform(put("/rest/admin-ui/hotels/{id}", hotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedHotelDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Hotel Name"))
                .andExpect(jsonPath("$.title").value("Updated Hotel Title"))
                .andExpect(jsonPath("$.city").value("Updated City"))
                .andExpect(jsonPath("$.distance").value(20))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.numberofratings").value(200));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Тест: Успешное частичное обновление отеля")
    void testPatchHotel() throws Exception {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Name");
        hotel.setTitle("Hotel Title");
        hotel.setCity("City");
        hotel.setDistance(10);
        hotel.setRating(4);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        String patchJson = "{\"name\": \"Patched Hotel Name\"}";
        mockMvc.perform(patch("/rest/admin-ui/hotels/{id}", hotel.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Patched Hotel Name"));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Тест: Успешное частичное обновление нескольких отелей")
    void testPatchManyHotels() throws Exception {
        Hotel hotel1 = new Hotel();
        hotel1.setName("Hotel Name 1");
        hotel1.setTitle("Hotel Title 1");
        hotel1.setCity("City 1");
        hotel1.setDistance(10);
        hotel1.setRating(4);
        hotel1.setNumberofratings(100);
        hotel1 = hotelRepository.save(hotel1);

        Hotel hotel2 = new Hotel();
        hotel2.setName("Hotel Name 2");
        hotel2.setTitle("Hotel Title 2");
        hotel2.setCity("City 2");
        hotel2.setDistance(20);
        hotel2.setRating(5);
        hotel2.setNumberofratings(200);
        hotel2 = hotelRepository.save(hotel2);

        String patchJson = "[{\"name\": \"Patched Hotel Name 1\"}, {\"name\": \"Patched Hotel Name 2\"}]";
        mockMvc.perform(patch("/rest/admin-ui/hotels/by-ids")
                        .param("ids", hotel1.getId().toString(), hotel2.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value(hotel1.getId()))
                .andExpect(jsonPath("$[1]").value(hotel2.getId()));
    }
}