package com.yourname.mall.module.product.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yourname.mall.common.Result;
import com.yourname.mall.module.product.entity.Category;
import com.yourname.mall.module.product.entity.Product;
import com.yourname.mall.module.product.service.ProductCacheService;
import com.yourname.mall.module.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCacheService productCacheService;

    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public Result<Product> getProductDetail(@PathVariable Long id) {
        Product product = productCacheService.getProductWithCache(id);
        if (product == null) {
            return Result.error(404, "商品不存在或已下架");
        }
        return Result.success(product);
    }

    /**
     * 根据分类获取商品列表
     */
    @GetMapping("/category/{categoryId}")
    public Result<IPage<Product>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<Product> products = productService.getProductsByCategory(categoryId, page, size);
        return Result.success(products);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result<IPage<Product>> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        IPage<Product> products = productService.searchProducts(keyword, page, size);
        return Result.success(products);
    }

    /**
     * 获取热门商品
     */
    @GetMapping("/hot")
    public Result<List<Product>> getHotProducts() {
        List<Product> products = productCacheService.getHotProducts();
        return Result.success(products);
    }
}