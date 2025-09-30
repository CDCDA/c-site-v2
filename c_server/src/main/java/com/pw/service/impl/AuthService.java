package com.pw.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pw.common.utils.PassWordUtil;
import com.pw.domain.User;
import com.pw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.pw.common.utils.PassWordUtil.matches;

/***
 * @author cyd
 * @date 2025/9/26 11:40
 * @description <>
 **/
@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public User login(User userDto) {
        String identifier = userDto.getUserName();
        // 判断标识符类型
        IdentifierType type = identifyType(identifier);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();

        User user = new User();
        switch (type) {
            case EMAIL:
                wrapper.eq(User::getEmail, identifier);
                user = userService.list(wrapper).stream().findFirst().orElse(null);
                break;
            case PHONE:
                wrapper.eq(User::getPhone, identifier);
                user = userService.list(wrapper).stream().findFirst().orElse(null);
                break;
            case USERNAME:
                wrapper.eq(User::getUserName, identifier);
                user = userService.list(wrapper).stream().findFirst().orElse(null);
                break;
            default:
                throw new RuntimeException("不支持的登录标识符");
        }

        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 验证密码
        if (!matches(userDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        return user;
    }

    private IdentifierType identifyType(String identifier) {
        // 邮箱正则
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        // 手机号正则（简单中国手机号）
        String phoneRegex = "^1[3-9]\\d{9}$";

        if (identifier.matches(emailRegex)) {
            return IdentifierType.EMAIL;
        } else if (identifier.matches(phoneRegex)) {
            return IdentifierType.PHONE;
        } else {
            return IdentifierType.USERNAME;
        }
    }

    enum IdentifierType {
        EMAIL, PHONE, USERNAME
    }
}
