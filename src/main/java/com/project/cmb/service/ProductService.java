package com.project.cmb.service;

import com.project.cmb.entity.Product;
import com.project.cmb.repo.ProductLineRepo;
import com.project.cmb.repo.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductLineRepo productLineRepo;

    public long getTotalProducts() {
        return productRepo.count();
    }

    public long getTotalProductLines() {
        return productLineRepo.count();
    }

    public Map<String, Long> getProductCountPerLine() {
        return productRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        p -> p.getProductLine().getProductLine(),
                        Collectors.counting()
                ));
    }

    public List<Product> getLowStockProducts() {
        return productRepo.findByQuantityInStockLessThan((short) 50);
    }
}