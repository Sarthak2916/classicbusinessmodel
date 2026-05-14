package com.project.cmb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @Column(name = "customerNumber")
    private Integer customerNumber;

    @NotBlank(message = "Customer name is required")
    @Size(max = 50)
    @Column(name = "customerName")
    private String customerName;

    @NotBlank(message = "Contact last name is required")
    @Size(max = 50)
    @Column(name = "contactLastName")
    private String contactLastName;

    @NotBlank(message = "Contact first name is required")
    @Size(max = 50)
    @Column(name = "contactFirstName")
    private String contactFirstName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 50)
    @Column(name = "phone")
    private String phone;

    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 50)
    @Column(name = "addressLine1")
    private String addressLine1;

    @Size(max = 50)
    @Column(name = "addressLine2")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    @Column(name = "city")
    private String city;

    @Size(max = 50)
    @Column(name = "state")
    private String state;

    @Size(max = 15)
    @Column(name = "postalCode")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 50)
    @Column(name = "country")
    private String country;

    @Column(name = "salesRepEmployeeNumber")
    private Integer salesRepEmployeeNumber;

    @DecimalMin(value = "0.0", message = "Credit limit cannot be negative")
    @Column(name = "creditLimit")
    private BigDecimal creditLimit;
}