package com.mymall.service;

import com.mymall.common.ServerResponse;

import java.util.Map;

public interface OrderService {

    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallback(Map<String, String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}
