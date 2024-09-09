package com.happynanum.happymall.application.service;

import com.happynanum.happymall.infra.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReissueService {

    private static final Logger log = LoggerFactory.getLogger(ReissueService.class);
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void reissue(HttpServletRequest request, HttpServletResponse response) {

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
                break;
            }
        }

        if (refresh == null) {
            throw  new IllegalArgumentException("refresh token not found");
        }

        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("refresh token expired");
        }

        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("invalid refresh token");
        }

        boolean isExist = redisTemplate.hasKey(refresh);
        if (!isExist) {
            throw new IllegalArgumentException("invalid refresh token");
        }

        Long id = jwtUtil.getId(refresh);
        String identifier = jwtUtil.getIdentifier(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", id, identifier, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", id, identifier, role, 86400000L);

        redisTemplate.delete(refresh);
        redisTemplate.opsForValue().set(newRefresh, "true", 86400000L, TimeUnit.MILLISECONDS);

        response.addHeader("access", "Bearer " + newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        log.info("사용자 토큰 재발급 완료 = {}", identifier);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }


}
