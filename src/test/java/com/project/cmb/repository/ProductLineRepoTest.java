package com.project.cmb.repository;

import com.project.cmb.entity.ProductLine;
import com.project.cmb.repo.ProductLineRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductLineRepoTest {

    @Autowired
    private ProductLineRepo productLineRepo;

    private ProductLine productLine;

    @BeforeEach
    void setUp() {

        productLine = new ProductLine();

        productLine.setProductLine("Classic Cars");
        productLine.setTextDescription("Classic Cars Description");
        productLine.setHtmlDescription("<h1>Classic Cars</h1>");

        productLineRepo.save(productLine);
    }

    // =====================================================
    // findAll()
    // =====================================================

    @Test
    @DisplayName("findAll returns paginated product lines")
    void findAll_ReturnsAllProductLinesPaginated() {

        Pageable pageable = PageRequest.of(0, 5);

        Page<ProductLine> productLines =
                productLineRepo.findAll(pageable);

        assertThat(productLines).isNotEmpty();

        assertThat(productLines.getContent().size())
                .isGreaterThan(0);
    }

    // =====================================================
    // findById()
    // =====================================================

    @Test
    @DisplayName("findById when exists returns product line")
    void findById_WhenExists_ReturnsProductLine() {

        Optional<ProductLine> foundProductLine =
                productLineRepo.findById("Classic Cars");

        assertThat(foundProductLine).isPresent();

        assertThat(foundProductLine.get().getTextDescription())
                .isEqualTo("Classic Cars Description");
    }

    @Test
    @DisplayName("findById when not exists returns empty")
    void findById_WhenNotExists_ReturnsEmpty() {

        Optional<ProductLine> foundProductLine =
                productLineRepo.findById("INVALID_PRODUCT_LINE");

        assertThat(foundProductLine).isEmpty();
    }

    // =====================================================
    // save()
    // =====================================================

    @Test
    @DisplayName("save when valid product line saves successfully")
    void save_WhenValidProductLine_SavesSuccessfully() {

        ProductLine newProductLine = new ProductLine();

        newProductLine.setProductLine("Motorcycles");
        newProductLine.setTextDescription("Motorcycles Description");
        newProductLine.setHtmlDescription("<h1>Motorcycles</h1>");

        ProductLine savedProductLine =
                productLineRepo.save(newProductLine);

        assertThat(savedProductLine).isNotNull();

        assertThat(savedProductLine.getProductLine())
                .isEqualTo("Motorcycles");
    }

    // =====================================================
    // existsById()
    // =====================================================

    @Test
    @DisplayName("existsById when exists returns true")
    void existsById_WhenExists_ReturnsTrue() {

        boolean exists =
                productLineRepo.existsById("Classic Cars");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("existsById when not exists returns false")
    void existsById_WhenNotExists_ReturnsFalse() {

        boolean exists =
                productLineRepo.existsById("INVALID_PRODUCT_LINE");

        assertThat(exists).isFalse();
    }
}