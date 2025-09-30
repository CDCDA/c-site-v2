package com.pw.common.utils;

import com.pw.domain.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/***
 * @author cyd
 * @date 2023/6/5 17:21
 * @description <>
 **/
public class SecureUtil {

    /**
     * 获取session中存储的当前登录用户信息
     * @param req
     * @return
     */
    public static User getLoginUser(HttpServletRequest req){
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("User");
        return user;
    }
}
