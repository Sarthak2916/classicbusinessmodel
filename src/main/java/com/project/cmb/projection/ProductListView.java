package com.project.cmb.projection;

import org.springframework.data.rest.core.config.Projection;
import com.project.cmb.entity.Product;

import java.math.BigDecimal;

@Projection(name = "productList", types = { Product.class })
public interface ProductListView {

    String getProductCode();
    String getProductName();
    Short getQuantityInStock();
    BigDecimal getBuyPrice();
    BigDecimal getMsrp();

    // Product line name only
    ProductLineInfo getProductLine();
    interface ProductLineInfo {
        String getProductLine();
    }
}