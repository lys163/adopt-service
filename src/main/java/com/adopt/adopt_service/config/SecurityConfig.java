package com.adopt.adopt_service.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        
                .csrf(csrf -> csrf.disable())
                .cors(c->{
                    CorsConfigurationSource source = request->{
                        CorsConfiguration config = new CorsConfiguration();
                        config.addAllowedOrigin("http://localhost:5173");
                        config.addAllowedOrigin("http://localhost:8080");
                        config.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(List.of("*"));
                        return config;
                    };
                    c.configurationSource(source);
                })

                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers("/api/board/**").permitAll()
                        .requestMatchers("/api/animals/**").permitAll()
                        .requestMatchers("/api/personalities/**").permitAll()
                        .requestMatchers("/api/orders/**").permitAll()
                        .requestMatchers("/v3/api-docs/**","/swagger-ui/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                
                .httpBasic(httpBasic -> httpBasic.disable())

                .formLogin(form->form.disable())

                .logout(logout->logout
                    .logoutUrl("/api/auth/logout")
                    .logoutSuccessHandler((requests,response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                    })
                );
                
        return http.build();
    }
    @Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
}
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
