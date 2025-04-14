package com.hz.usercenterbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hz.usercenterbackend.common.BaseResponse;
import com.hz.usercenterbackend.common.ErrorCode;
import com.hz.usercenterbackend.common.ResultUtils;
import com.hz.usercenterbackend.exception.BusinessException;
import com.hz.usercenterbackend.model.domain.User;
import com.hz.usercenterbackend.model.domain.request.UserLoginRequest;
import com.hz.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.hz.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.hz.usercenterbackend.constant.UserConstant.ADMIN_ROLE;
import static com.hz.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;
/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
//            return ResultUtils.error(ErrorCode.NULL_ERROR);
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        long result = userService.userRegister(userAccount, userPassword, checkPassword);

        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> login(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return null;
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        User user = userService.userLogin(userAccount, userPassword, request);

        return ResultUtils.success(user);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> logout( HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        Integer result =  userService.userLogout( request);
        return ResultUtils.success(result);
    }

    /**
     * 用户管理-查询接口 必须鉴权
     *
     * @param username
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (isAdmin(request)){
            throw new BusinessException(ErrorCode.NO_AUTH,"不是管理员");
        }
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            wrapper.like("username", username);
        }
        List<User> userList = userService.list(wrapper);
        List<User> result = userList.stream().map(user -> userService.getSafetyUser(user)).toList();
        return ResultUtils.success(result);

    }

    /**
     * 用户管理-删除接口 必须鉴权
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody long id,HttpServletRequest request) {
        if (isAdmin(request)){
            return null;
        }
        if (id <= 0) {
            return null;
        }
        Boolean result = userService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 是否为管理员
     * @param request
     * @return
     */
    private boolean isAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

}
