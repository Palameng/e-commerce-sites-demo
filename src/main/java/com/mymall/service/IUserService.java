package com.mymall.service;

import com.mymall.common.ServerResponse;
import com.mymall.pojo.User;

public interface IUserService {
    //登录
    ServerResponse<User> login(String username, String password);

    //注册
    ServerResponse register(User user);

    //校验账号类型（账号或邮箱）的有效性
    ServerResponse<String> checkValid(String str, String type);

    //通过用户名查找到相应用户设置的提示问题
    ServerResponse selectQuestion(String username);

    //校验问题答案是否正确，并生成token
    ServerResponse<String> checkAnswer(String username, String question, String answer);

    //未登录情况下的忘记密码并修改密码功能
    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    //登录后修改密码
    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);
}
