package com.project.cmb.repo;

import com.project.cmb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    Page<Product> findByProductNameContainingIgnoreCase(
            @Param("productName") String productName,
            Pageable pageable
    );

    Page<Product> findByProductLine_ProductLine(
            @Param("productLine") String productLine,
            Pageable pageable
    );
}