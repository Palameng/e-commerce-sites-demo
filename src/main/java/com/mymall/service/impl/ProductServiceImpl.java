package com.mymall.service.impl;

import com.mymall.common.ResponseCode;
import com.mymall.common.ServerResponse;
import com.mymall.dao.ProductMapper;
import com.mymall.pojo.Product;
import com.mymall.service.ProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse saveOrUpdateProduct(Product product){

        int rowCount = 0;

        if (product != null){
            if (StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if (subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null){
                rowCount = productMapper.updateByPrimaryKey(product);

                if (rowCount > 0){
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                }

                return ServerResponse.createByErrorMessage("更新产品失败");
            }else {
                rowCount = productMapper.insert(product);

                if (rowCount > 0){
                    return ServerResponse.createBySuccessMessage("新增产品成功");
                }

                return ServerResponse.createByErrorMessage("新增产品失败");
            }
        }
        return  ServerResponse.createByErrorMessage("新增或更新产品参数不正确");
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status){
        if (productId == null || status == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);

        int rowCount = productMapper.updateByPrimaryKeySelective(product);

        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("修改商品状态成功");
        }
        return ServerResponse.createByErrorMessage("修改商品状态失败");
    }
}
