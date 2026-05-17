package com.project.cmb.repo;

import com.project.cmb.entity.OrderDetail;
import com.project.cmb.entity.OrderDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "orderdetails")
public interface OrderDetailRepo extends JpaRepository<OrderDetail, OrderDetailId> {

    List<OrderDetail> findById_OrderNumber(Integer orderNumber);

    List<OrderDetail> findById_ProductCode(String productCode);
}