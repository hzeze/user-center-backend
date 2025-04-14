package com.hz.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hz.usercenterbackend.common.ErrorCode;
import com.hz.usercenterbackend.exception.BusinessException;
import com.hz.usercenterbackend.model.domain.User;
import com.hz.usercenterbackend.service.UserService;
import com.hz.usercenterbackend.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.hz.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author HeZe
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-08-10 13:24:55
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    // 盐值
    private static final String SALT = "hz";


    @Autowired
    UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户密码过短");
        }
        Pattern pattern = Pattern.compile("^\\W+$");
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.matches()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        long count = this.count(wrapper);
        if (count > 0) {
            return 0;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            return 0;
        }

        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {

        //校验账号、密码是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        //校验账号长度是否小于4位
        if (userAccount.length() < 4) {
            return null;
        }

        //校验密码是否小于8位
        if (userPassword.length() < 8) {
            return null;
        }

        //校验账号是否合法
        Pattern pattern = Pattern.compile("^\\W+$");
        Matcher matcher = pattern.matcher(userAccount);
        if (matcher.matches()) {
            return null;
        }

        //查询用户是否存在
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("userAccount", userAccount);
        wrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(wrapper);

        if (user == null) {
            log.info("================User is null=================");
            return null;
        }


        //用户脱敏
        User safetyUser = getSafetyUser(user);

        //记录用户session
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */
    @Override
    public User getSafetyUser(User user){
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setUserAccount(user.getUserAccount());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        return safetyUser;
    }

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }
}




