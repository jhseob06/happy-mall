package com.happynanum.happymall.infra.jwt;

import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.entity.Account;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;
import java.time.LocalDate;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String access = request.getHeader("access");

        if(access == null || !access.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        access = access.split("")[1];

        try {
            jwtUtil.isExpired(access);
        } catch (ExpiredJwtException e) {

            PrintWriter writer = response.getWriter();
            writer.print("expired token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String category = jwtUtil.getCategory(access);
        if(!category.equals("access")) {
            PrintWriter writer = response.getWriter();
            writer.print ("invalid access token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long id = jwtUtil.getId(access);
        String identifier = jwtUtil.getIdentifier(access);
        String role = jwtUtil.getRole(access);
        Account account = Account.builder()
                .id(id)
                .identifier(identifier)
                .role(role)
                .name("none")
                .age(0)
//                .weight(0)
                .birth(LocalDate.now())
                .shoulderLength(0)
                .waistLength(0)
                .armLength(0)
//                .height(0)
                .legLength(0)
                .phoneNumber("01000000000")
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(account);
        Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
