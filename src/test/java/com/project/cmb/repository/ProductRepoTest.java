package com.project.cmb.repository;

import com.project.cmb.entity.Product;
import com.project.cmb.entity.ProductLine;
import com.project.cmb.repo.ProductLineRepo;
import com.project.cmb.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepoTest {

    @Autowired private ProductRepo productRepo;
    @Autowired private ProductLineRepo productLineRepo;

    private ProductLine testLine1;
    private ProductLine testLine2;

    @BeforeEach
    void setup() {
        testLine1 = new ProductLine();
        testLine1.setProductLine("Test Line A");
        testLine1.setTextDescription("Test description A");
        productLineRepo.save(testLine1);

        testLine2 = new ProductLine();
        testLine2.setProductLine("Test Line B");
        testLine2.setTextDescription("Test description B");
        productLineRepo.save(testLine2);

        Product p1 = new Product();
        p1.setProductCode("T_001");
        p1.setProductName("Test Classic Car One");
        p1.setProductLine(testLine1);
        p1.setProductScale("1:10");
        p1.setProductVendor("Test Vendor A");
        p1.setProductDescription("A test product");
        p1.setQuantityInStock((short) 100);
        p1.setBuyPrice(new BigDecimal("50.00"));
        p1.setMsrp(new BigDecimal("99.99"));
        productRepo.save(p1);

        Product p2 = new Product();
        p2.setProductCode("T_002");
        p2.setProductName("Test Vintage Bike Two");
        p2.setProductLine(testLine1);
        p2.setProductScale("1:12");
        p2.setProductVendor("Test Vendor B");
        p2.setProductDescription("Another test product");
        p2.setQuantityInStock((short) 50);
        p2.setBuyPrice(new BigDecimal("30.00"));
        p2.setMsrp(new BigDecimal("59.99"));
        productRepo.save(p2);

        Product p3 = new Product();
        p3.setProductCode("T_003");
        p3.setProductName("Test Plane Three");
        p3.setProductLine(testLine2);
        p3.setProductScale("1:18");
        p3.setProductVendor("Test Vendor C");
        p3.setProductDescription("Third test product");
        p3.setQuantityInStock((short) 200);
        p3.setBuyPrice(new BigDecimal("70.00"));
        p3.setMsrp(new BigDecimal("129.99"));
        productRepo.save(p3);
    }

    @Test
    void repo_findAll_shouldReturnProducts() {
        assertThat(productRepo.findAll()).isNotEmpty();
    }

    @Test
    void repo_findById_shouldReturnProduct() {
        Optional<Product> result = productRepo.findById("T_001");
        assertThat(result).isPresent();
        assertThat(result.get().getProductName()).isEqualTo("Test Classic Car One");
    }

    @Test
    void repo_findById_whenNotExists_shouldReturnEmpty() {
        assertThat(productRepo.findById("ZZZZZZ")).isEmpty();
    }

    @Test
    void repo_save_shouldPersistNewProduct() {
        Product p = new Product();
        p.setProductCode("T_004");
        p.setProductName("Test Ship Four");
        p.setProductLine(testLine2);
        p.setProductScale("1:700");
        p.setProductVendor("Test Vendor D");
        p.setProductDescription("Fourth test product");
        p.setQuantityInStock((short) 10);
        p.setBuyPrice(new BigDecimal("90.00"));
        p.setMsrp(new BigDecimal("179.99"));
        productRepo.save(p);
        assertThat(productRepo.findById("T_004")).isPresent();
    }

    @Test
    void repo_save_shouldIncreaseCount() {
        long countBefore = productRepo.count();
        Product p = new Product();
        p.setProductCode("T_005");
        p.setProductName("Test Truck Five");
        p.setProductLine(testLine1);
        p.setProductScale("1:24");
        p.setProductVendor("Test Vendor E");
        p.setProductDescription("Fifth test product");
        p.setQuantityInStock((short) 75);
        p.setBuyPrice(new BigDecimal("40.00"));
        p.setMsrp(new BigDecimal("79.99"));
        productRepo.save(p);
        assertThat(productRepo.count()).isEqualTo(countBefore + 1);
    }

    @Test
    void repo_update_buyPrice_shouldModify() {
        Product p = productRepo.findById("T_001").orElseThrow();
        p.setBuyPrice(new BigDecimal("55.00"));
        productRepo.save(p);
        assertThat(productRepo.findById("T_001").orElseThrow().getBuyPrice())
                .isEqualByComparingTo("55.00");
    }

    @Test
    void repo_update_msrp_shouldModify() {
        Product p = productRepo.findById("T_001").orElseThrow();
        p.setMsrp(new BigDecimal("109.99"));
        productRepo.save(p);
        assertThat(productRepo.findById("T_001").orElseThrow().getMsrp())
                .isEqualByComparingTo("109.99");
    }

    @Test
    void repo_update_quantity_shouldModify() {
        Product p = productRepo.findById("T_001").orElseThrow();
        p.setQuantityInStock((short) 150);
        productRepo.save(p);
        assertThat(productRepo.findById("T_001").orElseThrow().getQuantityInStock())
                .isEqualTo((short) 150);
    }

    @Test
    void repo_update_shouldNotChangeOtherFields() {
        Product p = productRepo.findById("T_001").orElseThrow();
        p.setBuyPrice(new BigDecimal("60.00"));
        productRepo.save(p);
        Product updated = productRepo.findById("T_001").orElseThrow();
        assertThat(updated.getProductName()).isEqualTo("Test Classic Car One");
        assertThat(updated.getProductVendor()).isEqualTo("Test Vendor A");
    }

    @Test
    void repo_deleteById_shouldRemoveProduct() {
        productRepo.deleteById("T_003");
        assertThat(productRepo.findById("T_003")).isEmpty();
    }

    @Test
    void repo_deleteById_shouldDecreaseCount() {
        long countBefore = productRepo.count();
        productRepo.deleteById("T_003");
        assertThat(productRepo.count()).isEqualTo(countBefore - 1);
    }

    @Test
    void repo_findByName_shouldReturnMatch() {
        Page<Product> result = productRepo
                .findByProductNameContainingIgnoreCase("classic", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_001"))).isTrue();
    }

    @Test
    void repo_findByName_caseInsensitive_shouldWork() {
        Page<Product> result = productRepo
                .findByProductNameContainingIgnoreCase("VINTAGE", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_002"))).isTrue();
    }

    @Test
    void repo_findByName_noMatch_shouldReturnEmpty() {
        Page<Product> result = productRepo
                .findByProductNameContainingIgnoreCase("xyzxyzxyz", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void repo_findByProductLine_shouldReturnCorrectProducts() {
        Page<Product> result = productRepo
                .findByProductLine_ProductLine("Test Line A", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .allMatch(p -> p.getProductLine().getProductLine().equals("Test Line A"))).isTrue();
    }

    @Test
    void repo_findByProductLine_shouldNotReturnOtherLines() {
        Page<Product> result = productRepo
                .findByProductLine_ProductLine("Test Line B", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_003"))).isTrue();
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_001"))).isFalse();
    }

    @Test
    void repo_findByProductLine_noMatch_shouldReturnEmpty() {
        Page<Product> result = productRepo
                .findByProductLine_ProductLine("Nonexistent Line", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void repo_findByVendor_shouldReturnMatch() {
        Page<Product> result = productRepo
                .findByProductVendorContainingIgnoreCase("vendor a", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_001"))).isTrue();
    }

    @Test
    void repo_findByVendor_caseInsensitive_shouldWork() {
        Page<Product> result = productRepo
                .findByProductVendorContainingIgnoreCase("VENDOR B", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_002"))).isTrue();
    }

    @Test
    void repo_findByVendor_noMatch_shouldReturnEmpty() {
        Page<Product> result = productRepo
                .findByProductVendorContainingIgnoreCase("xyzxyzxyz", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void repo_findByProductCode_shouldReturnMatch() {
        Page<Product> result = productRepo
                .findByProductCodeContainingIgnoreCase("T_001", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(p -> p.getProductCode().equals("T_001"))).isTrue();
    }

    @Test
    void repo_findByProductCode_partialMatch_shouldWork() {
        Page<Product> result = productRepo
                .findByProductCodeContainingIgnoreCase("T_", PageRequest.of(0, 10));
        assertThat(result.getContent().size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void repo_findByProductCode_noMatch_shouldReturnEmpty() {
        Page<Product> result = productRepo
                .findByProductCodeContainingIgnoreCase("ZZZZZZ", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void repo_findByLowStock_shouldReturnProductsBelowThreshold() {
        List<Product> result = productRepo.findByQuantityInStockLessThan((short) 60);
        assertThat(result.stream()
                .anyMatch(p -> p.getProductCode().equals("T_002"))).isTrue();
    }

    @Test
    void repo_findByLowStock_shouldNotReturnHighStockProducts() {
        List<Product> result = productRepo.findByQuantityInStockLessThan((short) 60);
        assertThat(result.stream()
                .noneMatch(p -> p.getProductCode().equals("T_003"))).isTrue();
    }

    @Test
    void repo_findByLowStock_noneBelow_shouldReturnEmpty() {
        List<Product> result = productRepo.findByQuantityInStockLessThan((short) 1);
        assertThat(result.stream()
                .noneMatch(p -> p.getProductCode().startsWith("T_"))).isTrue();
    }
}