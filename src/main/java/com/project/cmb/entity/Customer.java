package com.project.cmb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customerNumber")
    private Integer customerNumber;

    @NotBlank(message = "Customer name is required")
    @Size(max = 50)
    @Column(name = "customerName", nullable = false)
    private String customerName;

    @NotBlank(message = "Contact last name is required")
    @Size(max = 50)
    @Column(name = "contactLastName", nullable = false)
    private String contactLastName;

    @NotBlank(message = "Contact first name is required")
    @Size(max = 50)
    @Column(name = "contactFirstName", nullable = false)
    private String contactFirstName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 50)
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 50)
    @Column(name = "addressLine1", nullable = false)
    private String addressLine1;

    @Size(max = 50)
    @Column(name = "addressLine2")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 50)
    @Column(name = "state")
    private String state;

    @Size(max = 15)
    @Column(name = "postalCode")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 50)
    @Column(name = "country", nullable = false)
    private String country;

    @DecimalMin(value = "0.0", message = "Credit limit cannot be negative")
    @Column(name = "creditLimit")
    private BigDecimal creditLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesRepEmployeeNumber")
    private Employee salesRepEmployee;
}