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

@RepositoryRestResource(path = "payment")
public interface PaymentRepo extends JpaRepository<Payment, PaymentId> {

    List<Payment> findById_CustomerNumber(
            @Param("customerNumber") Integer customerNumber
    );


    List<Payment> findByOrderNumber(
            @Param("orderNumber") Integer orderNumber
    );

    List<Payment> findByPaymentDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
