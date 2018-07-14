package com.mymall.service.impl;

import com.mymall.common.ServerResponse;
import com.mymall.dao.CategoryMapper;
import com.mymall.pojo.Category;
import com.mymall.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if (parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加类别错误");
        }

        Category category = new Category();

        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);//这个分类是可用的

        int rowCount = categoryMapper.insert(category);

        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("添加类别成功");
        }

        return ServerResponse.createByErrorMessage("添加类别失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新类别错误");
        }

        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);

        int rowCount = categoryMapper.updateByPrimaryKeySelective(category);

        if (rowCount > 0){
            return ServerResponse.createBySuccessMessage("更新类别名字成功");
        }
        return ServerResponse.createByErrorMessage("更新类别名字失败");
    }
}
