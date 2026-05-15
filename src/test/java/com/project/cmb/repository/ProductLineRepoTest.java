package com.project.cmb.repository;

import com.project.cmb.entity.ProductLine;
import com.project.cmb.repo.ProductLineRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductLineRepoTest {

    @Autowired
    private ProductLineRepo productLineRepo;

    @BeforeEach
    void setup() {
        ProductLine pl1 = new ProductLine();
        pl1.setProductLine("Test Motorcycles");
        pl1.setTextDescription("Test motorcycle product line");
        productLineRepo.save(pl1);

        ProductLine pl2 = new ProductLine();
        pl2.setProductLine("Test Planes");
        pl2.setTextDescription("Test planes product line");
        productLineRepo.save(pl2);
    }

    @Test
    void repo_findAll_shouldReturnProductLines() {
        List<ProductLine> result = productLineRepo.findAll();
        assertThat(result).isNotEmpty();
    }

    @Test
    void repo_findById_shouldReturnProductLine() {
        Optional<ProductLine> result = productLineRepo.findById("Test Motorcycles");
        assertThat(result).isPresent();
        assertThat(result.get().getTextDescription())
                .isEqualTo("Test motorcycle product line");
    }

    @Test
    void repo_findById_whenNotExists_shouldReturnEmpty() {
        Optional<ProductLine> result = productLineRepo.findById("Nonexistent Line");
        assertThat(result).isEmpty();
    }

    @Test
    void repo_save_shouldPersistNewProductLine() {
        ProductLine pl = new ProductLine();
        pl.setProductLine("Test Ships");
        pl.setTextDescription("Test ships product line");
        productLineRepo.save(pl);

        assertThat(productLineRepo.findById("Test Ships")).isPresent();
    }

    @Test
    void repo_save_shouldIncreaseCount() {
        long countBefore = productLineRepo.count();
        ProductLine pl = new ProductLine();
        pl.setProductLine("Test Trains");
        pl.setTextDescription("Test trains product line");
        productLineRepo.save(pl);
        assertThat(productLineRepo.count()).isEqualTo(countBefore + 1);
    }

    @Test
    void repo_update_description_shouldModify() {
        ProductLine pl = productLineRepo.findById("Test Motorcycles").orElseThrow();
        pl.setTextDescription("Updated motorcycle description");
        productLineRepo.save(pl);

        assertThat(productLineRepo.findById("Test Motorcycles")
                .orElseThrow().getTextDescription())
                .isEqualTo("Updated motorcycle description");
    }

    @Test
    void repo_update_shouldNotChangeOtherFields() {
        ProductLine pl = productLineRepo.findById("Test Motorcycles").orElseThrow();
        pl.setTextDescription("Changed description");
        productLineRepo.save(pl);

        assertThat(productLineRepo.findById("Test Motorcycles")
                .orElseThrow().getProductLine())
                .isEqualTo("Test Motorcycles");
    }

    @Test
    void repo_deleteById_shouldRemoveProductLine() {
        productLineRepo.deleteById("Test Planes");
        assertThat(productLineRepo.findById("Test Planes")).isEmpty();
    }

    @Test
    void repo_deleteById_shouldDecreaseCount() {
        long countBefore = productLineRepo.count();
        productLineRepo.deleteById("Test Planes");
        assertThat(productLineRepo.count()).isEqualTo(countBefore - 1);
    }
}