package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.web.dto.RoomDto;
import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.2")
            .withDatabaseName("hotel_booking_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
        postgres.close();
    }

    @BeforeEach
    void setUp() {
        try {
            roomRepository.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(1)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Успешное создание комнаты")
    void testCreateRoom() throws Exception {

        // Создаем отель
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTitle("Best Hotel");
        hotel.setCity("Test City");
        hotel.setDistance(500);
        hotel.setRating(5);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        // Создаем DTO комнаты
        RoomDto roomDto = new RoomDto();
        roomDto.setHotelId(hotel.getId());
        roomDto.setName("Deluxe Room");
        roomDto.setDescription("A beautiful deluxe room");
        roomDto.setRoomNumber("101");
        roomDto.setPrice(new BigDecimal("150.0"));
        roomDto.setMaxPeople(2);

        mockMvc.perform(post("/rest/admin-ui/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "hotelId": %d,
                                    "name": "%s",
                                    "description": "%s",
                                    "roomNumber": "%s",
                                    "price": %.1f,
                                    "maxPeople": %d
                                }
                                """.formatted(
                                hotel.getId(),
                                roomDto.getName(),
                                roomDto.getDescription(),
                                roomDto.getRoomNumber(),
                                roomDto.getPrice(),
                                roomDto.getMaxPeople()
                        )))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(roomDto.getName()))
                .andExpect(jsonPath("$.roomNumber").value(roomDto.getRoomNumber()))
                .andExpect(jsonPath("$.price").value(roomDto.getPrice().toString()))
                .andExpect(jsonPath("$.maxPeople").value(roomDto.getMaxPeople()));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Получение списка комнат")
    void testGetAllRooms() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Order(3)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Получение одной комнаты по ID")
    void testGetRoomById() throws Exception {
        // Создаем отель и комнату
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTitle("Best Hotel");
        hotel.setCity("Test City");
        hotel.setDistance(500);
        hotel.setRating(5);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setName("Deluxe Room");
        room.setDescription("A beautiful deluxe room");
        room.setRoomNumber("101");
        room.setPrice(new BigDecimal("150.0"));
        room.setMaxPeople(2);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        mockMvc.perform(get("/rest/admin-ui/rooms/{id}", room.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(room.getName()))
                .andExpect(jsonPath("$.roomNumber").value(room.getRoomNumber()))
                .andExpect(jsonPath("$.price").value(room.getPrice().toString()))
                .andExpect(jsonPath("$.maxPeople").value(room.getMaxPeople()));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Обновление комнаты")
    void testUpdateRoom() throws Exception {
        // Создаем отель и комнату
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTitle("Best Hotel");
        hotel.setCity("Test City");
        hotel.setDistance(500);
        hotel.setRating(5);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setName("Deluxe Room");
        room.setDescription("A beautiful deluxe room");
        room.setRoomNumber("101");
        room.setPrice(new BigDecimal("150.00"));
        room.setMaxPeople(2);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        // Обновляем данные комнаты
        RoomDto updatedRoomDto = new RoomDto();
        updatedRoomDto.setId(room.getId());
        updatedRoomDto.setName("Updated Deluxe Room");
        updatedRoomDto.setDescription("An updated beautiful deluxe room");
        updatedRoomDto.setRoomNumber("102");
        updatedRoomDto.setPrice(new BigDecimal("200.0"));
        updatedRoomDto.setMaxPeople(3);

        mockMvc.perform(put("/rest/admin-ui/rooms/{id}", room.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "%s",
                                    "description": "%s",
                                    "roomNumber": "%s",
                                    "price": %.1f,
                                    "maxPeople": %d
                                }
                                """.formatted(
                                updatedRoomDto.getName(),
                                updatedRoomDto.getDescription(),
                                updatedRoomDto.getRoomNumber(),
                                updatedRoomDto.getPrice(),
                                updatedRoomDto.getMaxPeople()
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedRoomDto.getName()))
                .andExpect(jsonPath("$.roomNumber").value(updatedRoomDto.getRoomNumber()))
                .andExpect(jsonPath("$.price").value(updatedRoomDto.getPrice().toString()))
                .andExpect(jsonPath("$.maxPeople").value(updatedRoomDto.getMaxPeople()));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Удаление комнаты")
    void testDeleteRoom() throws Exception {
        // Создаем отель и комнату
        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTitle("Best Hotel");
        hotel.setCity("Test City");
        hotel.setDistance(500);
        hotel.setRating(5);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setName("Deluxe Room");
        room.setDescription("A beautiful deluxe room");
        room.setRoomNumber("101");
        room.setPrice(new BigDecimal("150.0"));
        room.setMaxPeople(2);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        mockMvc.perform(delete("/rest/admin-ui/rooms/{id}", room.getId()))
                .andExpect(status().isNoContent());

        // Проверяем, что комната удалена
        mockMvc.perform(get("/rest/admin-ui/rooms/{id}", room.getId()))
                .andExpect(status().isNotFound());
    }
}