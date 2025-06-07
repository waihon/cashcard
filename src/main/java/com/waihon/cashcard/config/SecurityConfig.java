package com.waihon.cashcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Tells Spring to use this class to configure Spring and Spring Boot itself.
@Configuration
class SecurityConfig {

    // Spring Security expects a Bean to configure its Filter Chain. Annotating a method
    // return a SecurityFilterChain with the @Bean satisfies this expectation.
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // All HTTP requests to cashcards/ endpoints are required to be authenticated.
        http.authorizeHttpRequests(request -> request
                .requestMatchers("/cashcards/**")
                .authenticated());
        // Enable HTTP Basic Authentication security (username and password) with default settings.
        http.httpBasic(Customizer.withDefaults());
        // Do not require CSRF security
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
