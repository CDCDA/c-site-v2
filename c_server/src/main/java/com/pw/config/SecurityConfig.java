package com.pw.config;

import com.pw.common.filter.JwtAuthenticationFilter;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 允许访问的接口路径
                        .requestMatchers(
                                "/prod-api/**",
                                "/auth/**",
                                "/user/**",
                                "/public/**",
                                // Swagger/Knife4j 相关路径
                                "/doc.html",
                                "/webjars/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v2/api-docs",
                                "/favicon.ico",
                                "/error",
                                // 其他静态资源
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/ws/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil,
                                                           UserService userService) {
        return new JwtAuthenticationFilter(jwtTokenUtil, userService);
    }
}
