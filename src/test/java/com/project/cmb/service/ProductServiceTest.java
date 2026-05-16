package com.project.cmb.service;

import com.project.cmb.entity.Product;
import com.project.cmb.entity.ProductLine;
import com.project.cmb.repo.ProductLineRepo;
import com.project.cmb.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepo productRepo;
    @Mock private ProductLineRepo productLineRepo;

    @InjectMocks private ProductService productService;

    private Product p1, p2, p3;
    private ProductLine line1, line2;

    @BeforeEach
    void setup() {
        line1 = new ProductLine();
        line1.setProductLine("Classic Cars");

        line2 = new ProductLine();
        line2.setProductLine("Motorcycles");

        p1 = new Product();
        p1.setProductCode("S10_1678");
        p1.setProductName("1969 Harley Davidson");
        p1.setProductLine(line1);
        p1.setProductVendor("Min Lin Diecast");
        p1.setQuantityInStock((short) 7933);
        p1.setBuyPrice(new BigDecimal("48.81"));
        p1.setMsrp(new BigDecimal("95.70"));

        p2 = new Product();
        p2.setProductCode("S10_1949");
        p2.setProductName("1952 Alpine Renault");
        p2.setProductLine(line1);
        p2.setProductVendor("Classic Metal Creations");
        p2.setQuantityInStock((short) 7305);
        p2.setBuyPrice(new BigDecimal("98.58"));
        p2.setMsrp(new BigDecimal("214.30"));

        p3 = new Product();
        p3.setProductCode("S12_1099");
        p3.setProductName("1968 Ford Mustang");
        p3.setProductLine(line2);
        p3.setProductVendor("Autoart Studio Design");
        p3.setQuantityInStock((short) 30);  // low stock
        p3.setBuyPrice(new BigDecimal("68.99"));
        p3.setMsrp(new BigDecimal("145.00"));
    }

    @Test
    void getTotalProducts_shouldReturnCount() {
        when(productRepo.count()).thenReturn(3L);
        assertThat(productService.getTotalProducts()).isEqualTo(3L);
        verify(productRepo, times(1)).count();
    }

    @Test
    void getTotalProductLines_shouldReturnCount() {
        when(productLineRepo.count()).thenReturn(2L);
        assertThat(productService.getTotalProductLines()).isEqualTo(2L);
        verify(productLineRepo, times(1)).count();
    }

    @Test
    void getProductCountPerLine_shouldReturnCorrectCounts() {
        when(productRepo.findAll()).thenReturn(List.of(p1, p2, p3));

        Map<String, Long> result = productService.getProductCountPerLine();

        assertThat(result).containsKey("Classic Cars");
        assertThat(result).containsKey("Motorcycles");
        assertThat(result.get("Classic Cars")).isEqualTo(2L);
        assertThat(result.get("Motorcycles")).isEqualTo(1L);
        verify(productRepo, times(1)).findAll();
    }

    @Test
    void getProductCountPerLine_emptyRepo_shouldReturnEmptyMap() {
        when(productRepo.findAll()).thenReturn(List.of());
        assertThat(productService.getProductCountPerLine()).isEmpty();
    }

    @Test
    void getLowStockProducts_shouldReturnProductsBelowThreshold() {
        when(productRepo.findByQuantityInStockLessThan((short) 50))
                .thenReturn(List.of(p3));

        List<Product> result = productService.getLowStockProducts();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProductCode()).isEqualTo("S12_1099");
        verify(productRepo, times(1)).findByQuantityInStockLessThan((short) 50);
    }

    @Test
    void getLowStockProducts_noneBelow_shouldReturnEmpty() {
        when(productRepo.findByQuantityInStockLessThan((short) 50))
                .thenReturn(List.of());

        assertThat(productService.getLowStockProducts()).isEmpty();
    }
}