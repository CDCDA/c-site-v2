package com.pw.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pw.domain.User;

import java.util.List;


/***
 * @author cyd
 * @date 2023/5/18 17:39
 * @description <用户接口>
 **/

public interface UserService extends IService<User> {

    public Boolean checkPassword(User user);

    public User getUserByUserId(Long userId);
}
