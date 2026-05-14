package com.project.cmb.repository;

import com.project.cmb.entity.Product;
import com.project.cmb.entity.ProductLine;
import com.project.cmb.repo.ProductLineRepo;
import com.project.cmb.repo.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductLineRepo productLineRepo;

    private Product product;
    private ProductLine productLine;

    @BeforeEach
    void setUp() {

        productLine = new ProductLine();
        productLine.setProductLine("Classic Cars");
        productLine.setTextDescription("Classic Cars Description");

        productLineRepo.save(productLine);

        product = new Product();

        product.setProductCode("S10_1678");
        product.setProductName("1969 Harley Davidson Ultimate Chopper");
        product.setProductLine(productLine);
        product.setProductScale("1:10");
        product.setProductVendor("Min Lin Diecast");
        product.setProductDescription("Test Product Description");
        product.setQuantityInStock((short) 7933);

        product.setBuyPrice(BigDecimal.valueOf(48.81));
        product.setMsrp(BigDecimal.valueOf(95.70));

        productRepo.save(product);
    }

    // =====================================================
    // BUILT IN METHODS
    // =====================================================

    @Test
    @DisplayName("findAll returns paginated products")
    void findAll_ReturnsAllProductsPaginated() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findAll(pageable);

        assertThat(products).isNotEmpty();

        assertThat(products.getContent().size())
                .isGreaterThan(0);
    }

    @Test
    @DisplayName("findById when exists returns product")
    void findById_WhenExists_ReturnsProduct() {

        Optional<Product> foundProduct =
                productRepo.findById("S10_1678");

        assertThat(foundProduct).isPresent();

        assertThat(foundProduct.get().getProductName())
                .isEqualTo("1969 Harley Davidson Ultimate Chopper");
    }

    @Test
    @DisplayName("findById when not exists returns empty")
    void findById_WhenNotExists_ReturnsEmpty() {

        Optional<Product> foundProduct =
                productRepo.findById("INVALID_CODE");

        assertThat(foundProduct).isEmpty();
    }

    @Test
    @DisplayName("save when valid product saves successfully")
    void save_WhenValidProduct_SavesSuccessfully() {

        Product newProduct = new Product();

        newProduct.setProductCode("S12_9999");
        newProduct.setProductName("Ferrari Test Car");
        newProduct.setProductLine(productLine);
        newProduct.setProductScale("1:18");
        newProduct.setProductVendor("AutoArt");
        newProduct.setProductDescription("Ferrari Description");

        newProduct.setQuantityInStock((short) 150);

        newProduct.setBuyPrice(BigDecimal.valueOf(120.50));
        newProduct.setMsrp(BigDecimal.valueOf(200.00));

        Product savedProduct =
                productRepo.save(newProduct);

        assertThat(savedProduct).isNotNull();

        assertThat(savedProduct.getProductCode())
                .isEqualTo("S12_9999");
    }

    @Test
    @DisplayName("existsById when exists returns true")
    void existsById_WhenExists_ReturnsTrue() {

        boolean exists =
                productRepo.existsById("S10_1678");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById when not exists returns false")
    void existsById_WhenNotExists_ReturnsFalse() {

        boolean exists =
                productRepo.existsById("INVALID_CODE");

        assertThat(exists).isFalse();
    }

    // =====================================================
    // CUSTOM METHODS
    // =====================================================

    @Test
    void findByProductName_WhenExists_ReturnsProduct() {

        Optional<Product> foundProduct =
                productRepo.findByProductName(
                        "1969 Harley Davidson Ultimate Chopper"
                );

        assertThat(foundProduct).isPresent();

        assertThat(foundProduct.get().getProductCode())
                .isEqualTo("S10_1678");
    }

    @Test
    void findByProductName_WhenNotExists_ReturnsEmpty() {

        Optional<Product> foundProduct =
                productRepo.findByProductName(
                        "XYZ_NOT_EXISTING_123"
                );

        assertThat(foundProduct).isEmpty();
    }

    // =====================================================
    // findByProductScale()
    // =====================================================

    @Test
    void findByProductScale_WhenExists_ReturnsProducts() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductScale(
                        "1:10",
                        pageable
                );

        assertThat(products).isNotEmpty();

        assertThat(products.getContent())
                .extracting(Product::getProductCode)
                .contains("S10_1678");
    }

    @Test
    void findByProductScale_WhenNotExists_ReturnsEmptyPage() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductScale(
                        "XYZ_SCALE_123",
                        pageable
                );

        assertThat(products).isEmpty();
    }

    // =====================================================
    // findByProductVendor()
    // =====================================================

    @Test
    void findByProductVendor_WhenExists_ReturnsProducts() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductVendor(
                        "Min Lin Diecast",
                        pageable
                );

        assertThat(products).isNotEmpty();

        assertThat(products.getContent())
                .extracting(Product::getProductVendor)
                .contains("Min Lin Diecast");
    }

    @Test
    void findByProductVendor_WhenNotExists_ReturnsEmptyPage() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductVendor(
                        "XYZ_VENDOR_123",
                        pageable
                );

        assertThat(products).isEmpty();
    }

    // =====================================================
    // findByProductNameContainingIgnoreCase()
    // =====================================================

    @Test
    void findByProductNameContainingIgnoreCase_WhenExists_ReturnsProducts() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductNameContainingIgnoreCase(
                        "Harley",
                        pageable
                );

        assertThat(products).isNotEmpty();

        assertThat(products.getContent())
                .extracting(Product::getProductCode)
                .contains("S10_1678");
    }

    @Test
    void findByProductNameContainingIgnoreCase_WhenUpperCase_ReturnsProducts() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductNameContainingIgnoreCase(
                        "HARLEY",
                        pageable
                );

        assertThat(products).isNotEmpty();

        assertThat(products.getContent())
                .extracting(Product::getProductCode)
                .contains("S10_1678");
    }

    @Test
    void findByProductNameContainingIgnoreCase_WhenNoMatch_ReturnsEmptyPage() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findByProductNameContainingIgnoreCase(
                        "XYZ_NOT_EXISTING_123",
                        pageable
                );

        assertThat(products).isEmpty();
    }

    // =====================================================
    // findAllByProductLine()
    // =====================================================

    @Test
    void findAllByProductLine_WhenExists_ReturnsProducts() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findAllByProductLine(
                        productLine,
                        pageable
                );

        assertThat(products).isNotEmpty();

        assertThat(products.getContent())
                .extracting(Product::getProductCode)
                .contains("S10_1678");
    }

    @Test
    void findAllByProductLine_WhenPaginationApplied_ReturnsCorrectPage() {

        Pageable pageable = PageRequest.of(0, 1);

        Page<Product> products =
                productRepo.findAllByProductLine(
                        productLine,
                        pageable
                );

        assertThat(products.getSize())
                .isEqualTo(1);
    }

    @Test
    void findAllByProductLine_WhenNoProducts_ReturnsEmptyPage() {

        ProductLine newLine = new ProductLine();

        newLine.setProductLine("XYZ_LINE_123");
        newLine.setTextDescription("Test Description");

        productLineRepo.save(newLine);

        Pageable pageable = PageRequest.of(0, 5);

        Page<Product> products =
                productRepo.findAllByProductLine(
                        newLine,
                        pageable
                );

        assertThat(products).isEmpty();
    }
}