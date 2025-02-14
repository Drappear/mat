package com.example.mat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
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
                                .requestMatchers("/member/check-duplicate-userid", "/member/check-duplicate-nickname")
                                .permitAll()
                                .requestMatchers("/member/register").permitAll()
                                .requestMatchers("/board/register").permitAll()
                                .requestMatchers("/diner/**", "/dfup/**", "/review/**").permitAll()
                                .requestMatchers("/recipe/**", "/upload/**").permitAll()
                                .anyRequest().authenticated());

                http.formLogin(login -> login.loginPage("/member/login").permitAll().defaultSuccessUrl("/"));

                http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

                http.logout(logout -> logout
                                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                .logoutSuccessUrl("/diner/list"));

                // CSRF 설정: 게시물 등록 경로에서 비활성화
                http.csrf(csrf -> csrf
                                .ignoringRequestMatchers("/board/register", "/board/modify", "/market/cart"));

                return http.build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
}
