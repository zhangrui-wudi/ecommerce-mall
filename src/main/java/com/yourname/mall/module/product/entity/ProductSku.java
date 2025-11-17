package com.yourname.mall.module.product.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

@Data
@TableName("product_skus")
public class ProductSku implements Serializable{
    private static final long serialVersionUID = 1L;
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long productId;
    private String skuCode;
    private BigDecimal price;
    private Integer stock;
    private String attributes; // JSON格式存储
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}