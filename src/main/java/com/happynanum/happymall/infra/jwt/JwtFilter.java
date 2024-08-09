package com.happynanum.happymall.infra.jwt;

import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.entity.Account;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.warn("token null");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.split(" ")[1];

        if (jwtUtil.isExpired(token)) {
            log.warn("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        Long id = jwtUtil.getId(token);
        String identifier = jwtUtil.getIdentifier(token);
        String role = jwtUtil.getRole(token);
        Account account = Account.builder()
                .id(id)
                .identifier(identifier)
                .role(role)
                .name("none")
                .age(0)
                .weight(0)
                .birth(LocalDate.now())
                .shoulderLength(0)
                .wishLength(0)
                .armLength(0)
                .height(0)
                .legLength(0)
                .phoneNumber("01000000000")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
