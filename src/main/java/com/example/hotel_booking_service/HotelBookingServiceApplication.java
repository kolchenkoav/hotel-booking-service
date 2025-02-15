package com.example.hotel_booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class HotelBookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelBookingServiceApplication.class, args);
	}

}
