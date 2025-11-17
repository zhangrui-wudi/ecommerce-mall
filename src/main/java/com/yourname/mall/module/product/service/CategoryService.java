package com.yourname.mall.module.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.mall.module.product.entity.Category;
import com.yourname.mall.module.product.mapper.CategoryMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    @Cacheable(value = "categories", key = "'all'")
    public List<Category> getVisibleCategories() {
        System.out.println("从数据库查询所有分类");
        return lambdaQuery()
                .eq(Category::getIsVisible, true)
                .orderByAsc(Category::getSortOrder)
                .list();
    }

    @Cacheable(value = "categories", key = "'sub:' + #parentId")
    public List<Category> getSubCategories(Long parentId) {
        System.out.println("从数据库查询子分类: " + parentId);
        return lambdaQuery()
                .eq(Category::getParentId, parentId)
                .eq(Category::getIsVisible, true)
                .orderByAsc(Category::getSortOrder)
                .list();
    }

    @CacheEvict(value = "categories", allEntries = true)
    public void clearCategoryCache() {
        System.out.println("清除所有分类缓存");
    }
}