package com.project.cmb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @Column(name = "customer_number")
    private Integer customerNumber;

    @NotBlank(message = "Customer name is required")
    @Size(max = 50)
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank(message = "Contact last name is required")
    @Size(max = 50)
    @Column(name = "contact_last_name", nullable = false)
    private String contactLastName;

    @NotBlank(message = "Contact first name is required")
    @Size(max = 50)
    @Column(name = "contact_first_name", nullable = false)
    private String contactFirstName;

    @NotBlank(message = "Phone number is required")
    @Size(max = 50)
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 100)
    @Column(name = "address_line1", nullable = false)
    private String addressLine1;

    @Size(max = 100)
    @Column(name = "address_line2")
    private String addressLine2;

    @NotBlank(message = "City is required")
    @Size(max = 50)
    @Column(name = "city", nullable = false)
    private String city;

    @Size(max = 50)
    @Column(name = "state")
    private String state;

    @Size(max = 15)
    @Column(name = "postal_code")
    private String postalCode;

    @NotBlank(message = "Country is required")
    @Size(max = 50)
    @Column(name = "country", nullable = false)
    private String country;

    @DecimalMin(value = "0.0", message = "Credit limit cannot be negative")
    @Column(name = "credit_limit")
    private BigDecimal creditLimit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_rep_employee_number")
    private Employee salesRepEmployee;

    public Customer() {
    }


    public Integer getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(Integer customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Employee getSalesRepEmployee() {
        return salesRepEmployee;
    }

    public void setSalesRepEmployee(Employee salesRepEmployee) {
        this.salesRepEmployee = salesRepEmployee;
    }
}