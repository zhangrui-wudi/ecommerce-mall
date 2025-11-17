package com.yourname.mall.module.product.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.mall.module.product.entity.Product;
import com.yourname.mall.module.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends ServiceImpl<ProductMapper, Product> {

    @Autowired
    private ProductMapper productMapper;

    public IPage<Product> getProductsByCategory(Long categoryId, int page, int size) {
        Page<Product> pageParam = new Page<>(page, size);
        return productMapper.selectByCategoryId(pageParam, categoryId);
    }

    public IPage<Product> searchProducts(String keyword, int page, int size) {
        Page<Product> pageParam = new Page<>(page, size);
        return productMapper.searchProducts(pageParam, keyword);
    }

    public Product getProductDetail(Long id) {
        Product product = getById(id);
        if (product != null && product.getStatus() == 1) {
            // 增加浏览量
            product.setViewCount(product.getViewCount() + 1);
            updateById(product);
            return product;
        }
        return null;
    }
}