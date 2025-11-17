package com.yourname.mall.module.product.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yourname.mall.module.product.entity.ProductSku;
import com.yourname.mall.module.product.mapper.ProductSkuMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSkuService extends ServiceImpl<ProductSkuMapper, ProductSku> {

    public List<ProductSku> getSkusByProductId(Long productId) {
        return lambdaQuery()
                .eq(ProductSku::getProductId, productId)
                .list();
    }
}