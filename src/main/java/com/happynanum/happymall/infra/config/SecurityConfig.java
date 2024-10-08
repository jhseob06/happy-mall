package com.happynanum.happymall.infra.config;

import com.happynanum.happymall.application.service.LogoutService;
import com.happynanum.happymall.infra.jwt.CustomLogoutFilter;
import com.happynanum.happymall.infra.jwt.JwtFilter;
import com.happynanum.happymall.infra.jwt.JwtUtil;
import com.happynanum.happymall.infra.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final LogoutService logoutService;
    private final RedisTemplate<String, String> redisTemplate;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/join", "/", "/login", "/orders/success", "orders/cancel", "/reissue").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/products*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/products*").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/brands*").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/brands*").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/categories*").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories*").hasAnyRole("ADMIN")
                        .requestMatchers("/product-categories*").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterBefore(new CustomLogoutFilter(logoutService), LogoutFilter.class)
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
