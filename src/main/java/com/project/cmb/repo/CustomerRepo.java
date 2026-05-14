package com.project.cmb.repo;

import com.project.cmb.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Integer> {

    // =====================================================
    // CUSTOMER LIST PAGE METHODS
    // =====================================================

    Page<Customer> findByCustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );

    Page<Customer> findByPhoneContaining(
            String phone,
            Pageable pageable
    );

    Page<Customer> findByCountry(
            String country,
            Pageable pageable
    );

    Page<Customer> findByCity(
            String city,
            Pageable pageable
    );

    Page<Customer> findByPostalCode(
            String postalCode,
            Pageable pageable
    );

    Page<Customer> findByCreditLimitBetween(
            BigDecimal minCredit,
            BigDecimal maxCredit,
            Pageable pageable
    );

    Page<Customer> findByCreditLimitGreaterThanEqual(
            BigDecimal creditLimit,
            Pageable pageable
    );

    Page<Customer> findByCreditLimitLessThanEqual(
            BigDecimal creditLimit,
            Pageable pageable
    );

    Page<Customer> findByCountryAndCreditLimitBetween(
            String country,
            BigDecimal minCredit,
            BigDecimal maxCredit,
            Pageable pageable
    );

    Page<Customer> findByCustomerNameContainingIgnoreCaseAndCountry(
            String customerName,
            String country,
            Pageable pageable
    );

    // =====================================================
    // CUSTOMER DETAIL PAGE METHODS
    // =====================================================

    Optional<Customer> findByCustomerNumber(
            Integer customerNumber
    );

    Optional<Customer> findByPhone(
            String phone
    );

    Optional<Customer> findByCustomerName(
            String customerName
    );

    List<Customer> findBySalesRepEmployeeNumber(
            Integer employeeNumber
    );

    // =====================================================
    // ADD CUSTOMER PAGE METHODS
    // =====================================================

    boolean existsByCustomerName(
            String customerName
    );

    boolean existsByPhone(
            String phone
    );

    Page<Customer> findByContactFirstNameContainingIgnoreCase(
            String firstName,
            Pageable pageable
    );

    Page<Customer> findByContactLastNameContainingIgnoreCase(
            String lastName,
            Pageable pageable
    );

    // =====================================================
    // EDIT CUSTOMER PAGE METHODS
    // =====================================================

    Optional<Customer> findByCustomerNumberAndPhone(
            Integer customerNumber,
            String phone
    );

    Page<Customer> findByCreditLimitGreaterThan(
            BigDecimal creditLimit,
            Pageable pageable
    );

    Page<Customer> findByCreditLimitLessThan(
            BigDecimal creditLimit,
            Pageable pageable
    );

    // =====================================================
    // UPDATE ADDRESS PAGE METHODS
    // =====================================================

    Page<Customer> findByAddressLine1ContainingIgnoreCase(
            String addressLine1,
            Pageable pageable
    );

    Page<Customer> findByCountryContainingIgnoreCase(
            String country,
            Pageable pageable
    );

    // =====================================================
    // ORDERS TAB METHODS
    // =====================================================

    List<Customer> findByCustomerNumberOrderByCustomerNumberAsc(
            Integer customerNumber
    );

    List<Customer> findByCustomerNumberAndCountry(
            Integer customerNumber,
            String country
    );

    // =====================================================
    // PAYMENTS TAB METHODS
    // =====================================================

    List<Customer> findByCustomerNumberOrderByCreditLimitDesc(
            Integer customerNumber
    );

    List<Customer> findByCustomerNumberAndCreditLimitGreaterThan(
            Integer customerNumber,
            BigDecimal creditLimit
    );

    // =====================================================
    // DASHBOARD METHODS
    // =====================================================

    long count();

    long countDistinctByCountryIsNotNull();

    List<Customer> findTop5ByOrderByCreditLimitDesc();

    List<Customer> findTop5ByOrderByCustomerNumberDesc();

    // =====================================================
    // EXTRA FILTER METHODS
    // =====================================================

    Page<Customer> findByCountryContainingIgnoreCaseAndCityContainingIgnoreCase(
            String country,
            String city,
            Pageable pageable
    );

    Page<Customer> findByCustomerNameContainingIgnoreCaseAndCityContainingIgnoreCase(
            String customerName,
            String city,
            Pageable pageable
    );

    Page<Customer> findByCustomerNameContainingIgnoreCaseAndCreditLimitBetween(
            String customerName,
            BigDecimal minCredit,
            BigDecimal maxCredit,
            Pageable pageable
    );

    Page<Customer> findByCountryAndCreditLimitGreaterThanEqual(
            String country,
            BigDecimal creditLimit,
            Pageable pageable
    );

    Page<Customer> findByCountryAndCreditLimitLessThanEqual(
            String country,
            BigDecimal creditLimit,
            Pageable pageable
    );

    // =====================================================
    // SORTING / DISPLAY METHODS
    // =====================================================

    List<Customer> findTop10ByOrderByCustomerNameAsc();

    List<Customer> findTop10ByOrderByCreditLimitAsc();

    List<Customer> findTop10ByOrderByCreditLimitDesc();

    List<Customer> findTop10ByOrderByCountryAsc();

}