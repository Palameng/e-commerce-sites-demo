package com.mymall.service;

import com.mymall.common.ServerResponse;

public interface OrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);
}
