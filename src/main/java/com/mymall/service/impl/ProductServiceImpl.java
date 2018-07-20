package com.mymall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mymall.common.ResponseCode;
import com.mymall.common.ServerResponse;
import com.mymall.dao.CategoryMapper;
import com.mymall.dao.ProductMapper;
import com.mymall.pojo.Category;
import com.mymall.pojo.Product;
import com.mymall.service.ProductService;
import com.mymall.util.DateTimeUtil;
import com.mymall.util.PropertiesUtil;
import com.mymall.vo.ProductDetailVo;
import com.mymall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

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


    /**
     * 后台商品详情管理
     * @param productId 商品id
     * @return ServerResponse 结果响应对象
     */
    @Override
    public ServerResponse<Object> manageProductDetail(Integer productId){
        if (productId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);

        if (product == null){
            return ServerResponse.createByErrorMessage("产品已下架或者删除");
        }

        ProductDetailVo productDetailVo = assembleProductDetailVo(product);

        return ServerResponse.createBySuccess(productDetailVo);
    }

    private ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();

        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());

        //imageHost
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        //parentCategoryId 查找父类别
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());

        if (category == null){
            productDetailVo.setParentCategoryId(0); //默认根节点
        }else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        //createTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));

        //updateTime
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVo;
    }


    /**
     * 获取商品列表并分页处理：
     *     1 startPage--start
     *     2 填充自己的sql查询逻辑
     *     3 Pagehelper收尾
     * @param pageNum 第几页开始
     * @param pageSize 一页大小
     * @return ServerResponse响应
     */
    @Override
    public ServerResponse<PageInfo> getproductList(int pageNum, int pageSize){
        //1
        PageHelper.startPage(pageNum, pageSize);

        //2
        List<Product> productList = productMapper.selectList(); //获取所有的商品
        List<ProductListVo> productListVoList = Lists.newArrayList(); //声明一个前端需要的数据vo

        //遍历productList组装成vo赋值给上述的list
        for (Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }

        //3
        PageInfo pageResult = new PageInfo(productList);    //将sql的list作为参数传递给pageinfo构造函数
        pageResult.setList(productListVoList);  //将volist作为参数传入pageinfo的setlist函数完成设置



        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        productListVo.setMainImage(product.getMainImage());
        productListVo.setPrice(product.getPrice());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        return productListVo;
    }

    /**
     * 商品搜索，同时完成分页
     * @param productName 商品名称
     * @param productId 商品ID
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return ServerResponse
     */
    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }

        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVo> productListVoList = Lists.newArrayList(); //声明一个前端需要的数据vo

        PageInfo pageResult = new PageInfo(productList);    //将sql的list作为参数传递给pageinfo构造函数
        pageResult.setList(productListVoList);  //将volist作为参数传入pageinfo的setlist函数完成设置

        return ServerResponse.createBySuccess(pageResult);
    }
}
