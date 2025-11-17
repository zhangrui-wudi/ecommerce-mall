package com.yourname.mall.module.product.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yourname.mall.module.product.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    // 根据分类ID分页查询商品
    IPage<Product> selectByCategoryId(Page<Product> page, @Param("categoryId") Long categoryId);

    // 搜索商品
    IPage<Product> searchProducts(Page<Product> page, @Param("keyword") String keyword);
}