package com.example.hotel_booking_service.security;

import com.example.hotel_booking_service.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Реализация интерфейса UserDetails для пользователя.
 */
public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    /**
     * Возвращает коллекцию ролей пользователя.
     *
     * @return коллекция ролей пользователя.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Возвращает пароль пользователя.
     *
     * @return пароль пользователя.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Возвращает имя пользователя.
     *
     * @return имя пользователя.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Проверяет, не истек ли срок действия учетной записи пользователя.
     *
     * @return true, если учетная запись не истекла, иначе false.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирована ли учетная запись пользователя.
     *
     * @return true, если учетная запись не заблокирована, иначе false.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истекли ли учетные данные пользователя.
     *
     * @return true, если учетные данные не истекли, иначе false.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Проверяет, включен ли пользователь.
     *
     * @return true, если пользователь включен, иначе false.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
