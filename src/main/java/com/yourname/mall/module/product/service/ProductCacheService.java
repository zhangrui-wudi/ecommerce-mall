package com.yourname.mall.module.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yourname.mall.common.redis.RedisService;
import com.yourname.mall.module.product.entity.Category;
import com.yourname.mall.module.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProductCacheService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    private static final String PRODUCT_KEY_PREFIX = "product:";
    private static final String CATEGORY_KEY_PREFIX = "category:";
    private static final String HOT_PRODUCTS_KEY = "hot:products";
    private static final String ALL_CATEGORIES_KEY = "all:categories";

    private static final long PRODUCT_EXPIRE = 3600; // 1小时
    private static final long CATEGORY_EXPIRE = 7200; // 2小时
    private static final long HOT_PRODUCTS_EXPIRE = 1800; // 30分钟

    /**
     * 获取商品详情（带缓存）- 修复类型安全问题
     */
    public Product getProductWithCache(Long productId) {
        String key = PRODUCT_KEY_PREFIX + productId;

        try {
            // 先从缓存获取
            Object cached = redisService.get(key);
            if (cached != null) {
                // 安全的类型检查
                if (cached instanceof Product) {
                    System.out.println("从缓存获取商品: " + productId);
                    return (Product) cached;
                } else {
                    // 如果是错误类型，清理缓存
                    System.err.println("缓存数据类型错误，期望 Product 但得到: " + cached.getClass().getName());
                    redisService.delete(key);
                }
            }

            // 缓存不存在或类型错误，从数据库查询
            System.out.println("从数据库查询商品: " + productId);
            Product product = productService.getProductDetail(productId);
            if (product != null) {
                // 写入缓存
                redisService.set(key, product, PRODUCT_EXPIRE);
            }

            return product;
        } catch (Exception e) {
            System.err.println("获取商品缓存异常: " + e.getMessage());
            // 发生异常时直接查询数据库
            return productService.getProductDetail(productId);
        }
    }

    /**
     * 获取热门商品（带缓存）- 修复类型安全问题
     */
    public List<Product> getHotProducts() {
        try {
            Object cached = redisService.get(HOT_PRODUCTS_KEY);
            if (cached != null) {
                if (cached instanceof List) {
                    System.out.println("从缓存获取热门商品");
                    return (List<Product>) cached;
                } else {
                    System.err.println("热门商品缓存数据类型错误，清理缓存");
                    redisService.delete(HOT_PRODUCTS_KEY);
                }
            }

            System.out.println("从数据库查询热门商品");
            // 模拟热门商品查询（按销量排序的前10个）
            List<Product> products = productService.lambdaQuery()
                    .eq(Product::getStatus, 1)
                    .eq(Product::getIsDeleted, false)
                    .orderByDesc(Product::getSalesCount)
                    .last("LIMIT 10")
                    .list();

            if (products != null && !products.isEmpty()) {
                redisService.set(HOT_PRODUCTS_KEY, products, HOT_PRODUCTS_EXPIRE);
            }

            return products;
        } catch (Exception e) {
            System.err.println("获取热门商品缓存异常: " + e.getMessage());
            return productService.lambdaQuery()
                    .eq(Product::getStatus, 1)
                    .eq(Product::getIsDeleted, false)
                    .orderByDesc(Product::getSalesCount)
                    .last("LIMIT 10")
                    .list();
        }
    }

    /**
     * 获取所有分类（带缓存）- 修复类型安全问题
     */
    public List<Category> getAllCategoriesWithCache() {
        try {
            Object cached = redisService.get(ALL_CATEGORIES_KEY);
            if (cached != null) {
                if (cached instanceof List) {
                    System.out.println("从缓存获取所有分类");
                    return (List<Category>) cached;
                } else {
                    System.err.println("分类缓存数据类型错误，清理缓存");
                    redisService.delete(ALL_CATEGORIES_KEY);
                }
            }

            System.out.println("从数据库查询所有分类");
            List<Category> categories = categoryService.getVisibleCategories();

            if (categories != null && !categories.isEmpty()) {
                redisService.set(ALL_CATEGORIES_KEY, categories, CATEGORY_EXPIRE);
            }

            return categories;
        } catch (Exception e) {
            System.err.println("获取分类缓存异常: " + e.getMessage());
            return categoryService.getVisibleCategories();
        }
    }

    /**
     * 更新商品缓存
     */
    public void updateProductCache(Long productId) {
        String key = PRODUCT_KEY_PREFIX + productId;
        try {
            Product product = productService.getById(productId);
            if (product != null) {
                redisService.set(key, product, PRODUCT_EXPIRE);
                System.out.println("更新商品缓存: " + productId);
            }
        } catch (Exception e) {
            System.err.println("更新商品缓存异常: " + e.getMessage());
        }
    }

    /**
     * 删除商品缓存
     */
    public void deleteProductCache(Long productId) {
        String key = PRODUCT_KEY_PREFIX + productId;
        try {
            redisService.delete(key);
            System.out.println("删除商品缓存: " + productId);
        } catch (Exception e) {
            System.err.println("删除商品缓存异常: " + e.getMessage());
        }
    }

    /**
     * 清除分类缓存
     */
    public void clearCategoryCache() {
        try {
            redisService.delete(ALL_CATEGORIES_KEY);
            System.out.println("清除分类缓存");
        } catch (Exception e) {
            System.err.println("清除分类缓存异常: " + e.getMessage());
        }
    }

    /**
     * 清理所有商品相关缓存（用于调试）
     */
    public void clearAllProductCache() {
        try {
            // 这里需要 RedisService 支持模式删除，或者逐个删除已知的key
            redisService.delete(HOT_PRODUCTS_KEY);
            System.out.println("清理热门商品缓存");
        } catch (Exception e) {
            System.err.println("清理缓存异常: " + e.getMessage());
        }
    }
}