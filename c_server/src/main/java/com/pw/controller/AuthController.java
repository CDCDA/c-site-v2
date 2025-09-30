package com.pw.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pw.common.utils.JwtTokenUtil;
import com.pw.common.utils.PassWordUtil;
import com.pw.common.utils.Result;
import com.pw.common.utils.SnowFlake;
import com.pw.domain.EmailMessage;
import com.pw.domain.User;
import com.pw.service.UserService;
import com.pw.service.impl.AuthService;
import com.pw.service.impl.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Random;

@RestController
@Tag(name = "鉴权")
@RequestMapping("/auth")
public class AuthController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public AuthController(JwtTokenUtil jwtTokenUtil, UserService userService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(summary = "登录")
    public Result login(@RequestBody User userDto, HttpServletResponse response) {
        String token = null;
        User user = new User();
        try {
            user = authService.login(userDto);
            if (ObjectUtils.isNotEmpty(user)) {
                token = jwtTokenUtil.generateToken(user.getUserId());
                Cookie cookie = new Cookie("token", token);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        } catch (RuntimeException e) {
            return Result.error(e.toString());
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", user);
        return Result.ok().data(map);
    }

    @PostMapping("/tourist")
    @Operation(summary = "游客登录")
    public Result touristLogin(HttpServletResponse response) {
        String token = jwtTokenUtil.generateTouristToken();
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);
        return Result.ok().data("token", token);
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public Result register(@RequestBody User user) {
        if (ObjectUtils.isEmpty(user.getPassword())) {
            return Result.error("请输入密码");
        }
        if (ObjectUtils.isEmpty(user.getEmail())) {
            return Result.error("请输入邮箱");
        }
        if (ObjectUtils.isEmpty(user.getCode())) {
            return Result.error("请输入验证码");
        }

        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, user.getEmail());
        User existUser = userService.getOne(wrapper);
        if (ObjectUtils.isNotEmpty(existUser)) {
            return Result.error("邮箱已被注册");
        }

        String code = redisTemplate.opsForValue().get(user.getEmail());
        if (user.getCode().equals(code)) {
            user.setUserId(new SnowFlake(1, 0).nextId());
            user.setUserName(user.getEmail());
            user.setPassword(PassWordUtil.encodePassword(user.getPassword()));
            userService.save(user);
        } else {
            return Result.error("验证码错误或者已过期");
        }
        return Result.ok("注册成功");
    }

    @GetMapping("/validate")
    @Operation(summary = "token校验")
    public Result validate(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.error("Token格式错误");
        }

        String token = authHeader.substring(7);
        boolean isValid = jwtTokenUtil.validateToken(token);

        if (isValid) {
            return Result.ok();
        } else {
            return Result.error("Token无效或已过期");
        }
    }

    @PostMapping("/verification-code")
    @Operation(summary = "获取验证码")
    public Result getVerificationCode(@RequestBody HashMap<String, String> request) {
        String email = request.get("email");
        if (ObjectUtils.isNotEmpty(redisTemplate.opsForValue().get(email))) {
            return Result.error(500, "请勿重复获取验证码");
        }
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String code = String.format("%04d", randomNum);
        redisTemplate.opsForValue().set(email, code);

        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setText("您的验证码为：" + code + "\n" + "有效时间为5分钟");
        emailMessage.setTo(email);
        emailMessage.setSubject("验证码");
        emailService.sendEmail(emailMessage);
        return Result.ok();
    }
}
