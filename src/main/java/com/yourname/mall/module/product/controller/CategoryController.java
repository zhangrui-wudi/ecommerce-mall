package com.yourname.mall.module.product.controller;

import com.yourname.mall.common.Result;
import com.yourname.mall.module.product.entity.Category;
import com.yourname.mall.module.product.service.CategoryService;
import com.yourname.mall.module.product.service.ProductCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductCacheService productCacheService;

    /**
     * 获取所有分类
     */
    @GetMapping
    public Result<List<Category>> getAllCategories() {
        List<Category> categories = productCacheService.getAllCategoriesWithCache();
        return Result.success(categories);
    }

    /**
     * 获取子分类
     */
    @GetMapping("/{parentId}/sub")
    public Result<List<Category>> getSubCategories(@PathVariable Long parentId) {
        List<Category> categories = categoryService.getSubCategories(parentId);
        return Result.success(categories);
    }

    /**
     * 清除分类缓存（测试用）
     */
    @PostMapping("/cache/clear")
    public Result<String> clearCategoryCache() {
        productCacheService.clearCategoryCache();
        return Result.success("分类缓存清除成功");
    }
}