package com.project.cmb.repo;

import com.project.cmb.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDate;
import java.util.List;

@RepositoryRestResource(path = "orders")
public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<Order> findByStatus(String status);

    List<Order> findByCustomer_CustomerNumber(Integer customerNumber);

    Page<Order> findByCustomer_CustomerNameContainingIgnoreCase(
            String customerName,
            Pageable pageable
    );

    List<Order> findByOrderDateBetween(
            LocalDate startDate,
            LocalDate endDate
    );

    long countByStatus(String status);

    List<Order> findTop5ByOrderByOrderDateDesc();
}