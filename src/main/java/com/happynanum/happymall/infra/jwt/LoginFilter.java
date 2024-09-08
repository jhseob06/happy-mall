package com.happynanum.happymall.infra.jwt;

import com.happynanum.happymall.domain.dto.CustomUserDetails;
import com.happynanum.happymall.domain.entity.Refresh;
import com.happynanum.happymall.domain.repository.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String identifier = request.getParameter("identifier");
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(identifier, password, null);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

        Long id = customUserDetails.getId();
        String identifier = customUserDetails.getIdentifier();
        String role = authResult.getAuthorities().iterator().next().getAuthority();

        String access = jwtUtil.createJwt("access", id, identifier, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", id, identifier, role, 86400000L);

        Refresh refreshEntity = Refresh.builder()
                .identifier(identifier)
                .refresh(refresh)
                .expiration(new Date(System.currentTimeMillis() + 86400000L))
                .build();
        refreshRepository.save(refreshEntity);

        response.addHeader("access", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
        log.info("사용자 로그인 성공 = {}", identifier);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        log.info("사용자 로그인 실패 = {}", request.getParameter("identifier"));
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }
}
