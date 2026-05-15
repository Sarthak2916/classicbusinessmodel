
package com.project.cmb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @EmbeddedId
    private PaymentId id;

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



























































































































































