package com.mymall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mymall.common.Const;
import com.mymall.common.ResponseCode;
import com.mymall.common.ServerResponse;
import com.mymall.dao.CartMapper;
import com.mymall.dao.ProductMapper;
import com.mymall.pojo.Cart;
import com.mymall.pojo.Product;
import com.mymall.service.CartService;
import com.mymall.util.BigDecimalUtil;
import com.mymall.util.PropertiesUtil;
import com.mymall.vo.CartProductVo;
import com.mymall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("cartService")
public class CartServiceImpl implements CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;


    /**
     * 添加购物车的一条记录
     * @param userId 用户ID
     * @param productId 商品Id
     * @param count 添加进来的单个商品的总数
     * @return ServerResponse
     */
    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count){

        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }

        //cart表指出的是用户和商品的关系，一个用户有多个商品，某个商品也可能对应多个用户，这里表现为购物车的方式
        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);

        if (cart == null){
            //产品不在购物车中，添加进来
            Cart cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            cartItem.setProductId(productId);
            cartItem.setUserId(userId);

            cartMapper.insert(cartItem);
        }else {
            //如果产品存在，则数量相加
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }

        CartVo cartVo = this.getCartVoLimit(userId);


        return ServerResponse.createBySuccess(cartVo);
    }

    /**
     * 组装前端需要的购物车数据格式
     * @param userId 用户ID
     * @return CartVo
     */

    private CartVo getCartVoLimit(Integer userId){

        //声明一个提供给前端的购物车商品记录的vo
        CartVo cartVo = new CartVo();

        //根据用户ID查询该用户所有已加入购物车 的 商品记录list（区别于商品list，也就是List<Product> 和 List<Cart> 的区别）
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);

        //CartProductVo结合了产品和购物车的一些属性，产品内容的核心在CartProductVo中体现，而cartVo只是一个记录
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        //用BigDecimal修饰购物车的总价格
        BigDecimal cartTotalPrice = new BigDecimal("0");

        //
        if (CollectionUtils.isNotEmpty(cartList)){

            for (Cart cartItem : cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());

                if (product != null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductSubtitle(product.getSubtitle());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductPrice(product.getPrice());
                    cartProductVo.setProductStock(product.getStock());

                    int buyLimitCount = 0;

                    //如果商品库存 大于 购物车关于该商品记录中添加的数量，则说明库存还足够
                    if (product.getStock() >= cartItem.getQuantity()){
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else {
                        //库存不足时，说明添加购物车时该商品数量达到上限，设置状态标识后，更新记录
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);

                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }

                    cartProductVo.setQuantity(buyLimitCount);

                    //计算总价
                    cartProductVo.setProductTotalPrice(
                            BigDecimalUtil.mul(product.getPrice().doubleValue(), cartProductVo.getQuantity())
                    );

                    cartProductVo.setProductChecked(cartItem.getChecked()); //勾选状态
                }

                if (cartItem.getChecked() == Const.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), cartProductVo.getProductTotalPrice().doubleValue());
                }

                cartProductVoList.add(cartProductVo);
            }
        }

        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));

        return cartVo;
    }

    /**
     * 判断是否全选
     * @param userId 用户ID
     * @return true or false
     */
    private boolean getAllCheckedStatus(Integer userId){
        if (userId == null){
            return false;
        }else {
            return cartMapper.selectCartProductCheckedStatusByUserId(userId) == 0;
        }
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count){
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ILLEGAL_ARGUMENT.getCode(),
                    ResponseCode.ILLEGAL_ARGUMENT.getDesc()
            );
        }

        Cart cart = cartMapper.selectCartByUserIdProductId(userId, productId);

        if (cart != null){
            cart.setQuantity(count);
        }

        cartMapper.updateByPrimaryKeySelective(cart);
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);

    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds){
        //guava的一个工具，把字符串根据符号分割成小部分存放成list
        List<String> productList = Splitter.on(",").splitToList(productIds);

        if (CollectionUtils.isEmpty(productList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId, productList);
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }
}
