package com.mymall.service;

import com.mymall.common.ServerResponse;

public interface CategoryService {

    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);
}
