package com.project.cmb.repo;

import com.project.cmb.entity.Product;
import com.project.cmb.entity.ProductLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepo extends JpaRepository<Product, String> {

    Optional<Product> findByProductName(String productName);

    Page<Product> findByProductScale(String productScale, Pageable  pageable);

    Page<Product> findByProductVendor(String productVendor, Pageable pageable);

    Page<Product> findByProductNameContainingIgnoreCase(String keyword, Pageable pageable);

    Page<Product> findAllByProductLine(ProductLine productLine, Pageable pageable);
}