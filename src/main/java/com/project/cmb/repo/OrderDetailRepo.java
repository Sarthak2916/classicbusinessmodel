package com.project.cmb.repo;

import com.project.cmb.entity.OrderDetail;
import com.project.cmb.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "orderdetails")
public interface OrderDetailRepo extends JpaRepository<OrderDetail, OrderDetailId> {

    // All line items for one order — used by order detail screen
    List<OrderDetail> findById_OrderNumber(
            @Param("orderNumber") Integer orderNumber
    );

    // All order lines for a product — useful for product detail screen
    List<OrderDetail> findById_ProductCode(
            @Param("productCode") String productCode
    );
}