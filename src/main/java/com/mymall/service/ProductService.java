package com.mymall.service;

import com.github.pagehelper.PageInfo;
import com.mymall.common.ServerResponse;
import com.mymall.pojo.Product;
import com.mymall.vo.ProductDetailVo;

public interface ProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<Object> manageProductDetail(Integer productId);

    ServerResponse<PageInfo> getproductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,
                                                         Integer categoryId,
                                                         int pageNum,
                                                         int pageSize,
                                                         String orderBy);
}
