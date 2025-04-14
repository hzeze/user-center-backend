package com.hz.usercenterbackend.service;

import com.hz.usercenterbackend.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author HeZe
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-08-10 13:24:55
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   账号
     * @param userPassword  密码
     * @param checkPassword 校验密码
     * @return 新用户ID
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  账号
     * @param userPassword 密码
     * @param request      请求
     * @return 脱敏后的用户
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    User getSafetyUser(User user);

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    int  userLogout(HttpServletRequest request);
}
