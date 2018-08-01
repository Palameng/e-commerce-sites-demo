package com.mymall.controller.portal;

import com.mymall.common.Const;
import com.mymall.common.ResponseCode;
import com.mymall.common.ServerResponse;
import com.mymall.pojo.User;
import com.mymall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;


    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse add(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.add(user.getId(), productId, count);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse update(HttpSession session, Integer count, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete_product.do")
    @ResponseBody
    public ServerResponse deleteProduct(HttpSession session, String productIds){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public ServerResponse selectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnselect(user.getId(),null, Const.Cart.CHECKED);
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ServerResponse unSelectAll(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnselect(user.getId(), null, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ServerResponse unSelect(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnselect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse select(HttpSession session, Integer productId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return cartService.selectOrUnselect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping("get_cart_product_count.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpSession session){
        User user = (User)session.getAttribute(Const.CURRENT_USER);

        if (user == null){
            return ServerResponse.createBySuccess(0);
        }

        return cartService.getCartProductCount(user.getId());
    }


}
