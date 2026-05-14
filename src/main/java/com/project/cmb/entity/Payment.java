package com.project.cmb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @NotNull(message = "Customer number cannot be null")
    @Column(name = "customerNumber")
    private Integer customerNumber;

    @Id
    @NotBlank(message = "Check number cannot be empty")
    @Column(name = "checkNumber")
    private String checkNumber;

    @NotNull(message = "Payment date cannot be null")
    @Column(name = "paymentDate")
    private LocalDate paymentDate;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull(message = "Order number cannot be null")
    @Column(name = "orderNumber")
    private Integer orderNumber;
}