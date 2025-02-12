package com.example.hotel_booking_service.web.controller;

import com.example.hotel_booking_service.entity.Hotel;
import com.example.hotel_booking_service.entity.RoleType;
import com.example.hotel_booking_service.entity.Room;
import com.example.hotel_booking_service.entity.User;
import com.example.hotel_booking_service.repository.BookingRepository;
import com.example.hotel_booking_service.repository.HotelRepository;
import com.example.hotel_booking_service.repository.RoomRepository;
import com.example.hotel_booking_service.repository.UserRepository;
import com.example.hotel_booking_service.web.dto.BookingRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HotelRepository hotelRepository;

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

    @BeforeEach
    void setUp() {
        bookingRepository.deleteAll();
        roomRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Успешное бронирование комнаты")
    void testSuccessfulBooking() throws Exception {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("password123");
        user.setEmail("test_user@example.com");
        user.setRole(RoleType.ROLE_USER);
        user = userRepository.save(user);

        User admin = new User();
        admin.setUsername("test_admin");
        admin.setPassword("password123");
        admin.setEmail("test_admin@example.com");
        admin.setRole(RoleType.ROLE_ADMIN);
        admin = userRepository.save(admin);

        Hotel hotel = new Hotel();
        hotel.setName("Test Hotel");
        hotel.setTitle("Best Hotel");
        hotel.setCity("Test City");
        hotel.setDistance(500);
        hotel.setRating(5);
        hotel.setNumberofratings(100);
        hotel = hotelRepository.save(hotel);

        Room room = new Room();
        room.setRoomNumber("101");
        room.setName("Deluxe Room");
        room.setDescription("A beautiful deluxe room");
        room.setPrice(new BigDecimal("150.00"));
        room.setMaxPeople(2);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        BookingRequestDto request = new BookingRequestDto();
        request.setUserId(user.getId());
        request.setRoomId(room.getId());
        request.setCheckIn(LocalDate.now().plusDays(1));
        request.setCheckOut(LocalDate.now().plusDays(3));

        mockMvc.perform(post("/rest/admin-ui/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                            "userId": %d,
                            "roomId": %d,
                            "checkIn": "%s",
                            "checkOut": "%s"
                        }
                    """.formatted(user.getId(), room.getId(),
                                request.getCheckIn(), request.getCheckOut())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookingId").exists())
                .andExpect(jsonPath("$.userId").value(user.getId()))
                .andExpect(jsonPath("$.roomId").value(room.getId()))
                .andExpect(jsonPath("$.checkIn").value(request.getCheckIn().toString()))
                .andExpect(jsonPath("$.checkOut").value(request.getCheckOut().toString()));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "user", roles = "ADMIN")
    @DisplayName("Тест: Получение списка бронирований")
    void testGetAllBookings() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/bookings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
