package com.pw.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pw.domain.User;
import org.springframework.stereotype.Service;

import java.util.Date;

/***
 * @author cyd
 * @date 2023/6/9 16:25
 * @description <>
 **/
@Service
public class TokenService {

    public String getToken(User user) {
        Date start = new Date();
        Long currentTime = System.currentTimeMillis() + 3 * 60 * 60 * 1000;//3小时有效时间
        Date end = new Date(currentTime);
        String token = "";
        token = JWT.create().withAudience(String.valueOf(user.getUserId())).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    public String getTouristToken() {
        Date start = new Date();
        Long currentTime = System.currentTimeMillis() + 4 * 60 * 60 * 1000;//4小时有效时间
        Date end = new Date(currentTime);
        String token = "";
        token = JWT.create().withAudience(String.valueOf("2")).withIssuedAt(start).withExpiresAt(end)
                .sign(Algorithm.HMAC256("123456"));
        return token;
    }
}
