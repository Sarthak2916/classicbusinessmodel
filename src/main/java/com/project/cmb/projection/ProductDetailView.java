package com.project.cmb.projection;

import org.springframework.data.rest.core.config.Projection;
import com.project.cmb.entity.Product;

import java.math.BigDecimal;

@Projection(name = "productDetail", types = { Product.class })
public interface ProductDetailView {

    String getProductCode();
    String getProductName();
    String getProductScale();
    String getProductVendor();
    String getProductDescription();
    Short getQuantityInStock();
    BigDecimal getBuyPrice();
    BigDecimal getMsrp();

    ProductLineInfo getProductLine();
    interface ProductLineInfo {
        String getProductLine();
        String getTextDescription();
    }
}