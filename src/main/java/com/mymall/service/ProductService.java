package com.mymall.service;

import com.mymall.common.ServerResponse;
import com.mymall.pojo.Product;

public interface ProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<Object> manageProductDetail(Integer productId);
}
