package com.project.cmb.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "productCode", length = 15, nullable = false)
    private String productCode;

    @Column(name = "productName", length = 70, nullable = false)
    private String productName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productLine")
    private ProductLine productLine;

    @Column(name = "productScale", length = 10, nullable = false)
    private String productScale;

    @Column(name = "productVendor", length = 50, nullable = false)
    private String productVendor;

    @Column(name = "productDescription", columnDefinition = "TEXT", nullable = false)
    private String productDescription;

    @Column(name = "quantityInStock", nullable = false)
    private Short quantityInStock;

    @Column(name = "buyPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal buyPrice;

    @Column(name = "MSRP", nullable = false, precision = 10, scale = 2)
    private BigDecimal msrp;

}
