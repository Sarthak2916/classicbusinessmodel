package com.project.cmb.repo;

import com.project.cmb.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "customers")
public interface  CustomerRepo extends JpaRepository<Customer, Integer> {

    Page<Customer> findByCustomerNameContainingIgnoreCase(String customerName, Pageable pageable);
    Page<Customer> findByPhoneContaining(String phone, Pageable pageable);

    Page<Customer> findByCountry(String country, Pageable pageable);

    Page<Customer> findByCreditLimitBetween(BigDecimal min, BigDecimal max, Pageable pageable);

    List<Customer> findBySalesRepEmployee_EmployeeNumber(Integer employeeNumber);

    boolean existsByCustomerName(String customerName);
    boolean existsByPhone(String phone);
}