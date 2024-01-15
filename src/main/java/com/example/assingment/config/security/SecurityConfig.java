package com.example.assingment.config.security;

import com.example.assingment.config.env.SecurityEnvConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final SecurityEnvConfig securityEnvConfig;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth
                        .requestMatchers("api/v1/transactions/**").hasRole("ADMIN")
                        .requestMatchers("api/v1/customers/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        var adminProperties = securityEnvConfig.getUsers().get("admin");
        UserDetails admin = User.builder()
                .username(adminProperties.getUsername())
                .password(passwordEncoder().encode(adminProperties.getPassword()))
                .roles(adminProperties.getRole())
                .build();

        var userProperties = securityEnvConfig.getUsers().get("user");
        UserDetails user = User.builder()
                .username(userProperties.getUsername())
                .password(passwordEncoder().encode(userProperties.getPassword()))
                .roles(userProperties.getRole())
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
