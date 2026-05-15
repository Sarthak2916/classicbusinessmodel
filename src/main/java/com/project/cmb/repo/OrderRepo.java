

package com.project.cmb.repo;

import com.project.cmb.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "orders")
public interface OrderRepo extends JpaRepository<Order, Integer> {

    // Filter by status — used by GET /orders/status/{status}
    List<Order> findByStatus(
            @Param("status") String status
    );

    // All orders for a customer — used by customer detail screen
    List<Order> findByCustomerNumber(
            @Param("customerNumber") Integer customerNumber
    );

    List<Order> findTop5ByOrderByOrderDateDesc();

    long countByStatus(String inProcess);
}