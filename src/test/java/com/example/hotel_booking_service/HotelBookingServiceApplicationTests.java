package com.example.hotel_booking_service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelBookingServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
	void contextLoads() {
	}

    @Test
    public void getAllHotels() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/hotels")
                        .param("nameStarts", "м")
                        .param("titleStarts", "t")
                        .param("cityStarts", "м")
                        .param("distanceLte", "0")
                        .param("ratingIn", "5")
                        .param("pageNumber", "0")
                        .param("pageSize", "20")
                        .param("sort", ""))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("getOne")
    public void getOne() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/hotels/{0}", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void getMany() throws Exception {
        mockMvc.perform(get("/rest/admin-ui/hotels/by-ids")
                        .param("ids", ""))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void create() throws Exception {
        String dto = """
                {
                    "name": "",
                    "title": "",
                    "city": "",
                    "distance": 0,
                    "rating": 0,
                    "numberofratings": 0
                }""";

        mockMvc.perform(post("/rest/admin-ui/hotels")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void update() throws Exception {
        String dto = """
                {
                    "name": "",
                    "title": "",
                    "city": "",
                    "distance": 0,
                    "rating": 0,
                    "numberofratings": 0
                }""";

        mockMvc.perform(put("/rest/admin-ui/hotels/{0}", "0")
                        .content(dto)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void patch() throws Exception {
        String patchNode = "[]";

        mockMvc.perform(MockMvcRequestBuilders.patch("/rest/admin-ui/hotels/{0}", "0")
                        .content(patchNode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void patchMany() throws Exception {
        String patchNode = "[]";

        mockMvc.perform(MockMvcRequestBuilders.patch("/rest/admin-ui/hotels")
                        .param("ids", "")
                        .content(patchNode)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/admin-ui/hotels/{0}", "0"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void deleteMany() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/admin-ui/hotels")
                        .param("ids", ""))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
