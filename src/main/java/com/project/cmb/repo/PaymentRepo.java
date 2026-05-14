package com.project.cmb.repo;

import com.project.cmb.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PaymentRepo
        extends JpaRepository<Payment, String> {

    Page<Payment> findByCustomerNumber(
            Integer customerNumber,
            Pageable pageable
    );

    Page<Payment> findByOrderNumber(
            Integer orderNumber,
            Pageable pageable
    );

    Page<Payment> findByCheckNumberContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    Page<Payment> findByPaymentDateBetween(
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    );
}

