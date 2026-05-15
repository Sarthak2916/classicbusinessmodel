package com.project.cmb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "orderdetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId id;

    @NotNull(message = "Quantity ordered cannot be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(name = "quantityOrdered", nullable = false)
    private Integer quantityOrdered;

    @NotNull(message = "Price each cannot be null")
    @Column(name = "priceEach", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceEach;

    @NotNull(message = "Order line number cannot be null")
    @Column(name = "orderLineNumber", nullable = false)
    private Short orderLineNumber;
}