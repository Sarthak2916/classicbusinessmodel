package com.project.cmb.repo;

import com.project.cmb.entity.Payment;
import com.project.cmb.entity.PaymentId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "payments")
public interface PaymentRepo extends JpaRepository<Payment, PaymentId> {

    // Search by customer number
    List<Payment> findById_CustomerNumber(Integer customerNumber);

    // Search by order number
    List<Payment> findByOrderNumber(Integer orderNumber);

    // Filter by date range
    List<Payment> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    // Search by check number
    List<Payment> findById_CheckNumberContainingIgnoreCase(String checkNumber);
}
