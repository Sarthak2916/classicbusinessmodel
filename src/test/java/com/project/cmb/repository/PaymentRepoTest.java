package com.project.cmb.repository;

import com.project.cmb.entity.Payment;
import com.project.cmb.entity.PaymentId;
import com.project.cmb.repo.PaymentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepoTest {

    @Autowired
    private PaymentRepo paymentRepo;

    // Use existing customerNumbers from classicmodels (103, 112 exist)
    // Use unique check numbers prefixed with TEST_ to avoid collision
    private final PaymentId id1 = new PaymentId(103, "TEST_CHK001");
    private final PaymentId id2 = new PaymentId(103, "TEST_CHK002");
    private final PaymentId id3 = new PaymentId(112, "TEST_CHK003");

    @BeforeEach
    void setup() {
        Payment p1 = new Payment();
        p1.setId(id1);
        p1.setPaymentDate(LocalDate.of(2024, 1, 10));
        p1.setAmount(new BigDecimal("1500.00"));
        p1.setOrderNumber(90001);
        paymentRepo.save(p1);

        Payment p2 = new Payment();
        p2.setId(id2);
        p2.setPaymentDate(LocalDate.of(2024, 2, 15));
        p2.setAmount(new BigDecimal("2500.00"));
        p2.setOrderNumber(90002);
        paymentRepo.save(p2);

        Payment p3 = new Payment();
        p3.setId(id3);
        p3.setPaymentDate(LocalDate.of(2024, 3, 20));
        p3.setAmount(new BigDecimal("3500.00"));
        p3.setOrderNumber(90003);
        paymentRepo.save(p3);
    }

    // --- findAll ---

    @Test
    void repo_findAll_shouldReturnPayments() {
        assertThat(paymentRepo.findAll()).isNotEmpty();
    }

    // --- findById (composite key) ---

    @Test
    void repo_findById_shouldReturnPayment() {
        Optional<Payment> result = paymentRepo.findById(id1);
        assertThat(result).isPresent();
        assertThat(result.get().getAmount()).isEqualByComparingTo("1500.00");
    }

    @Test
    void repo_findById_whenNotExists_shouldReturnEmpty() {
        Optional<Payment> result = paymentRepo.findById(
                new PaymentId(99999, "NONEXISTENT"));
        assertThat(result).isEmpty();
    }

    // --- save ---

    @Test
    void repo_save_shouldPersistNewPayment() {
        PaymentId newId = new PaymentId(112, "TEST_CHK004");
        Payment p = new Payment();
        p.setId(newId);
        p.setPaymentDate(LocalDate.of(2024, 4, 1));
        p.setAmount(new BigDecimal("500.00"));
        p.setOrderNumber(90004);
        paymentRepo.save(p);

        assertThat(paymentRepo.findById(newId)).isPresent();
    }

    @Test
    void repo_save_shouldIncreaseCount() {
        long countBefore = paymentRepo.count();
        PaymentId newId = new PaymentId(103, "TEST_CHK005");
        Payment p = new Payment();
        p.setId(newId);
        p.setPaymentDate(LocalDate.of(2024, 5, 1));
        p.setAmount(new BigDecimal("750.00"));
        p.setOrderNumber(90005);
        paymentRepo.save(p);
        assertThat(paymentRepo.count()).isEqualTo(countBefore + 1);
    }

    // --- update ---

    @Test
    void repo_update_amount_shouldModify() {
        Payment p = paymentRepo.findById(id1).orElseThrow();
        p.setAmount(new BigDecimal("1800.00"));
        paymentRepo.save(p);

        assertThat(paymentRepo.findById(id1).orElseThrow().getAmount())
                .isEqualByComparingTo("1800.00");
    }

    @Test
    void repo_update_shouldNotChangeOtherFields() {
        Payment p = paymentRepo.findById(id1).orElseThrow();
        p.setAmount(new BigDecimal("2000.00"));
        paymentRepo.save(p);

        Payment updated = paymentRepo.findById(id1).orElseThrow();
        assertThat(updated.getId().getCustomerNumber()).isEqualTo(103);
        assertThat(updated.getId().getCheckNumber()).isEqualTo("TEST_CHK001");
        assertThat(updated.getOrderNumber()).isEqualTo(90001);
    }

    // --- delete ---

    @Test
    void repo_deleteById_shouldRemovePayment() {
        paymentRepo.deleteById(id3);
        assertThat(paymentRepo.findById(id3)).isEmpty();
    }

    @Test
    void repo_deleteById_shouldDecreaseCount() {
        long countBefore = paymentRepo.count();
        paymentRepo.deleteById(id3);
        assertThat(paymentRepo.count()).isEqualTo(countBefore - 1);
    }

    // --- findById_CustomerNumber ---

    @Test
    void repo_findByCustomerNumber_shouldReturnAllPayments() {
        List<Payment> result = paymentRepo.findById_CustomerNumber(103);
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("TEST_CHK001"))).isTrue();
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("TEST_CHK002"))).isTrue();
    }

    @Test
    void repo_findByCustomerNumber_shouldNotReturnOtherCustomers() {
        List<Payment> result = paymentRepo.findById_CustomerNumber(103);
        assertThat(result.stream()
                .allMatch(p -> p.getId().getCustomerNumber().equals(103))).isTrue();
    }

    @Test
    void repo_findByCustomerNumber_noMatch_shouldReturnEmpty() {
        List<Payment> result = paymentRepo.findById_CustomerNumber(99999);
        assertThat(result).isEmpty();
    }

    // --- findByOrderNumber ---

    @Test
    void repo_findByOrderNumber_shouldReturnPayment() {
        List<Payment> result = paymentRepo.findByOrderNumber(90001);
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("TEST_CHK001"))).isTrue();
    }

    @Test
    void repo_findByOrderNumber_noMatch_shouldReturnEmpty() {
        List<Payment> result = paymentRepo.findByOrderNumber(99999);
        assertThat(result).isEmpty();
    }

    // --- findByPaymentDateBetween ---

    @Test
    void repo_findByDateBetween_shouldReturnPaymentsInRange() {
        List<Payment> result = paymentRepo.findByPaymentDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 28)
        );
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("TEST_CHK001"))).isTrue();
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("TEST_CHK002"))).isTrue();
    }

    @Test
    void repo_findByDateBetween_shouldNotReturnOutsideRange() {
        List<Payment> result = paymentRepo.findByPaymentDateBetween(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 28)
        );
        assertThat(result.stream()
                .noneMatch(p -> p.getId().getCheckNumber().equals("TEST_CHK003"))).isTrue();
    }

    @Test
    void repo_findByDateBetween_noMatch_shouldReturnEmpty() {
        List<Payment> result = paymentRepo.findByPaymentDateBetween(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2000, 12, 31)
        );
        assertThat(result).isEmpty();
    }
}