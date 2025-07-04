package com.waihon.cashcard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                // Enable Role-Based Access Control (RBAC) to restrict access to only users with the
                // CARD-OWNER role.
                .hasRole("CARD-OWNER"));
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

    // Spring's IoC container will find the UserDetailsService bean and Spring Data
    // will use it when needed.
    @Bean
    UserDetailsService testOnlyUsers(PasswordEncoder passwordEncoder) {
        User.UserBuilder user = User.builder();
        UserDetails sarah = user
                .username("sarah1")
                .password(passwordEncoder.encode("abc123"))
                .roles("CARD-OWNER") // new role
                .build();
        UserDetails hankOwnsNoCards = user
                .username("hank-owns-no-cards")
                .password(passwordEncoder.encode("qrs456"))
                .roles("NON-OWNER") // new role
                .build();
        UserDetails kumar = user
                .username("kumar2")
                .password(passwordEncoder.encode("xyz789"))
                .roles("CARD-OWNER") // new role
                .build();
        return new InMemoryUserDetailsManager(sarah, hankOwnsNoCards, kumar);
    }
}
