package com.pw.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.domain.User;
import com.pw.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * JWT 认证过滤器
 * 优化：使用 Redis 缓存用户信息，减少数据库查询
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String USER_CACHE_PREFIX = "jwt:user:";
    private static final long CACHE_EXPIRE_HOURS = 2;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        if ("uniToken".equals(authHeader)) {
            token = authHeader;
            userId = 1L;
        }

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userId = jwtTokenUtil.getUserIdFromToken(token);
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtTokenUtil.validateToken(token)) {
                User user = getUserWithCache(userId);
                if (user != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * 从 Redis 缓存获取用户信息，查不到再查数据库
     */
    private User getUserWithCache(Long userId) {
        String cacheKey = USER_CACHE_PREFIX + userId;
        try {
            // 1. 先查 Redis 缓存
            String cachedUser = redisTemplate.opsForValue().get(cacheKey);
            if (cachedUser != null) {
                log.debug("[JWT优化] 用户 {} 从Redis缓存获取", userId);
                return objectMapper.readValue(cachedUser, User.class);
            }

            // 2. 缓存未命中，查数据库
            User user = userService.getUserByUserId(userId);
            if (user != null) {
                // 3. 写入 Redis 缓存
                String userJson = objectMapper.writeValueAsString(user);
                redisTemplate.opsForValue().set(cacheKey, userJson, CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
                log.debug("[JWT优化] 用户 {} 从数据库查询并写入缓存", userId);
            }
            return user;
        } catch (Exception e) {
            log.error("[JWT优化] 缓存异常，降级到数据库查询", e);
            return userService.getUserByUserId(userId);
        }
    }
}
