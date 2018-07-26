package com.mymall.service;

import com.mymall.common.ServerResponse;
import com.mymall.vo.CartVo;

public interface CartService {
    ServerResponse add(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Integer userId, String productIds);
}
