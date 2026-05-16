package com.project.cmb.repo;

import com.project.cmb.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(path = "products")
public interface ProductRepo extends JpaRepository<Product, String> {

    Page<Product> findByProductNameContainingIgnoreCase(String productName, Pageable pageable);

    Page<Product> findByProductLine_ProductLine(String productLine, Pageable pageable);

    Page<Product> findByProductVendorContainingIgnoreCase(String vendor, Pageable pageable);

    Page<Product> findByProductCodeContainingIgnoreCase(String productCode, Pageable pageable);

    List<Product> findByQuantityInStockLessThan(Short threshold);
}