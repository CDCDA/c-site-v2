package com.pw.common.utils;

import com.pw.domain.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret}")
    private String secretValue;

    @Value("${jwt.expiration}")
    private Long expirationValue;

    private static String secret;
    private static Long expiration;

    @PostConstruct
    public void init() {
        secret = this.secretValue;
        expiration = this.expirationValue;
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public static Long getUserIdFromToken(String token) {
        try {
            // 如果是 uniToken，直接返回用户ID 1
            if ("uniToken".equals(token)) {
                return 1L;
            }
            return Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static User getLoginUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static Long getLoginUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }

            Object principal = authentication.getPrincipal();

            // 如果是 uniToken，直接返回用户ID 1
            if (principal instanceof String && "uniToken".equals(principal)) {
                return 1L;
            }

            if (principal instanceof User) {
                User user = (User) principal;
                return user.getUserId();
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    // 检查token是否过期
    public boolean isTokenExpired(String token) {
        // uniToken 永不过期
        if ("uniToken".equals(token)) {
            return false;
        }
        try {
            Date expirationDate = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
            return expirationDate.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    // 生成token
    public String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateTouristToken() {
        return Jwts.builder()
                .setSubject("tourist")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 验证token
    public boolean validateToken(String token) {
        // uniToken 总是有效的
        if ("uniToken".equals(token)) {
            return true;
        }
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}
