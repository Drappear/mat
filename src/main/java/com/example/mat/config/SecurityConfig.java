package com.example.mat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/assets/**", "/css/**", "/js/**", "/upload/**").permitAll()
                .requestMatchers("/cart").permitAll() // 인증 필요
                .anyRequest().permitAll());
        
                http.formLogin(Customizer.withDefaults());

        http.sessionManagement(session ->
        session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
        // http.logout(logout -> logout.logoutRequestMatcher(new
        // AntPathRequestMatcher("/member/logout"))
        // .logoutSuccessUrl("/"));
        http.csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserDetailsService users() {
        UserDetails user = User.builder()
                .username("user1")
                .password("{bcrypt}$2a$10$KyDcOFD/NBfGPi/NC2/xGeFvUHcO/YzJVatLumNMpFK29fod1HZgO")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }
}