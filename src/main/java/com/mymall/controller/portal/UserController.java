package com.mymall.controller.portal;

import com.mymall.common.Const;
import com.mymall.common.ResponseCode;
import com.mymall.common.ServerResponse;
import com.mymall.pojo.User;
import com.mymall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录功能
     *
     * @param username 前端传入的用户名
     * @param password 前端传入的密码
     * @param session  session
     * @return ServerResponse<User>
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);

        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }

    /**
     * 登出操作
     *
     * @param session session
     * @return ServerResponse<String>
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession session) {
        session.removeAttribute(Const.CURRENT_USER);
        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     *
     * @param user 用户
     * @return ServerResponse<String>
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return userService.register(user);
    }


    /**
     * 校验账号类型（账号或邮箱）的有效性
     * @param str 传入的内容
     * @param type 类型
     * @return ServerResponse<String>
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return userService.checkValid(str, type);
    }

    /**
     * 获取登录用户信息
     * @param session session
     * @return ServerResponse<User>
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);

        if (user != null){
            return ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录");
    }

    /**
     * 根据用户名搜索重置密码的安全问题
     * @param username 用户名
     * @return ServerResponse<String>
     */
    @RequestMapping(value = "forget_get_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username){
        return userService.selectQuestion(username);
    }

    /**
     * 检索用户名-问题-答案 是否匹配， 匹配则通过并添加token加入cache
     * @param username 用户名
     * @param question 问题
     * @param answer 答案
     * @return ServerResponse<String>
     */
    @RequestMapping(value = "forget_check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer){
        return userService.checkAnswer(username, question, answer);
    }

    /**
     * 未登录状态下忘记密码
     * @param username 用户名
     * @param passwordNew 新密码
     * @param forgetToken 前端传递下来的token
     * @return ServerResponse<String>
     */
    @RequestMapping(value = "forget_reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken){
        return userService.forgetResetPassword(username, passwordNew, forgetToken);
    }

    /**
     * 登录状态下修改密码
     * @param session
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "reset_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return userService.resetPassword(passwordOld, passwordNew, user);
    }

    /**
     * 更改用户信息
     * @param session
     * @param user
     * @return
     */
    @RequestMapping(value = "update_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session, User user){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = userService.updateInformation(user);

        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    @RequestMapping(value = "get_information.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInformation(HttpSession session){
        User currentUser = (User)session.getAttribute(Const.CURRENT_USER);

        if (currentUser == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.NEED_LOGIN.getCode(), "未登录需要强制登录");
        }

        return userService.getInformation(currentUser.getId());
    }
}
