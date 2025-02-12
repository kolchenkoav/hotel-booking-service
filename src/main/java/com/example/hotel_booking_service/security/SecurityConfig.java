package com.example.hotel_booking_service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Конфигурация безопасности для приложения.
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Конструктор для внедрения UserDetailsService.
     *
     * @param userDetailsService сервис для получения данных пользователя.
     */
    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Bean для кодирования паролей.
     *
     * @return экземпляр BCryptPasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Bean для настройки цепочки фильтров безопасности.
     *
     * @param http объект HttpSecurity для настройки безопасности.
     * @return экземпляр SecurityFilterChain.
     * @throws Exception если возникает ошибка при настройке.
     */

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Новый способ отключения CSRF-защиты
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/rest/admin-ui/users/register").permitAll() // Регистрация доступна всем
                        .requestMatchers("/rest/admin-ui/rooms/**", "/rest/admin-ui/bookings/**")
                        .hasRole("ADMIN") // Только админ может управлять комнатами и бронированиями отелей
                        .anyRequest().authenticated()
                )
                .httpBasic(withDefaults()); // Используем Basic Auth

        return http.build();
    }


    /**
     * Bean для конфигурации аутентификации.
     *
     * @return экземпляр AuthenticationConfiguration.
     */
    @Bean
    public AuthenticationConfiguration authenticationConfiguration() {
        return new AuthenticationConfiguration();
    }
}
