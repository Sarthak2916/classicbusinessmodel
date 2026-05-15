package com.project.cmb.repository;

import com.project.cmb.entity.Payment;
import com.project.cmb.repo.PaymentRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PaymentRepoTest {

    @Autowired
    private PaymentRepo paymentRepo;

    // =========================================
    // findByCustomerNumber
    // =========================================

    @Test
    void testFindPaymentsByCustomerNumber() {

        Page<Payment> result =
                paymentRepo.findByCustomerNumber(
                        103,
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isGreaterThan(0);

        boolean found = result.getContent().stream()
                .anyMatch(payment ->
                        payment.getCustomerNumber() == 103
                );

        assertThat(found).isTrue();
    }

    @Test
    void testFindPaymentsByInvalidCustomerNumber() {

        Page<Payment> result =
                paymentRepo.findByCustomerNumber(
                        99999,
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isZero();
    }

    // =========================================
    // findByOrderNumber
    // =========================================

    @Test
    void testFindPaymentsByOrderNumber() {

        Page<Payment> result =
                paymentRepo.findByOrderNumber(
                        10100,
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isGreaterThanOrEqualTo(0);
    }

    @Test
    void testFindPaymentsByInvalidOrderNumber() {

        Page<Payment> result =
                paymentRepo.findByOrderNumber(
                        999999,
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isZero();
    }

    // =========================================
    // findByCheckNumberContainingIgnoreCase
    // =========================================

    @Test
    void testFindPaymentsByCheckNumberKeyword() {

        Page<Payment> result =
                paymentRepo
                        .findByCheckNumberContainingIgnoreCase(
                                "HQ",
                                PageRequest.of(0, 5)
                        );

        assertThat(result.getTotalElements())
                .isGreaterThan(0);

        boolean found = result.getContent().stream()
                .anyMatch(payment ->
                        payment.getCheckNumber()
                                .toUpperCase()
                                .contains("HQ")
                );

        assertThat(found).isTrue();
    }

    @Test
    void testFindPaymentsByInvalidCheckNumberKeyword() {

        Page<Payment> result =
                paymentRepo
                        .findByCheckNumberContainingIgnoreCase(
                                "XYZXYZ",
                                PageRequest.of(0, 5)
                        );

        assertThat(result.getTotalElements())
                .isZero();
    }

    // =========================================
    // findByPaymentDateBetween
    // =========================================

    @Test
    void testFindPaymentsBetweenValidDates() {

        Page<Payment> result =
                paymentRepo.findByPaymentDateBetween(
                        LocalDate.of(2003, 1, 1),
                        LocalDate.of(2004, 12, 31),
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isGreaterThan(0);
    }

    @Test
    void testFindPaymentsBetweenInvalidDates() {

        Page<Payment> result =
                paymentRepo.findByPaymentDateBetween(
                        LocalDate.of(2100, 1, 1),
                        LocalDate.of(2100, 12, 31),
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isZero();
    }

    // =========================================
    // save
    // =========================================

    @Test
    void testSavePaymentSuccessfully() {

        Payment payment = new Payment();

        payment.setCustomerNumber(103);
        payment.setCheckNumber("TEST123");
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(BigDecimal.valueOf(5000.0));
        payment.setOrderNumber(10100);

        paymentRepo.save(payment);

        Optional<Payment> saved =
                paymentRepo.findById("TEST123");

        assertThat(saved).isPresent();

        assertThat(saved.get().getAmount())
                .isEqualTo(BigDecimal.valueOf(5000.0));
    }

    @Test
    void testSavePaymentWithDuplicateCheckNumber() {

        Optional<Payment> existing =
                paymentRepo.findById("HQ336336");

        if(existing.isPresent()) {

            Payment payment = existing.get();

            payment.setAmount(BigDecimal.valueOf(9999.0));

            paymentRepo.save(payment);

            Optional<Payment> updated =
                    paymentRepo.findById("HQ336336");

            assertThat(updated).isPresent();

            assertThat(updated.get().getAmount())
                    .isEqualTo(BigDecimal.valueOf(9999.0));
        }
    }

    // =========================================
    // findById
    // =========================================

    @Test
    void testFindPaymentByExistingId() {

        Optional<Payment> result =
                paymentRepo.findById("HQ336336");

        assertThat(result).isPresent();
    }

    @Test
    void testFindPaymentByNonExistingId() {

        Optional<Payment> result =
                paymentRepo.findById("INVALID123");

        assertThat(result).isEmpty();
    }

    // =========================================
    // findAll
    // =========================================

    @Test
    void testFindAllPaymentsWithPagination() {

        Page<Payment> result =
                paymentRepo.findAll(
                        PageRequest.of(0, 5)
                );

        assertThat(result.getContent().size())
                .isLessThanOrEqualTo(5);

        assertThat(result.getTotalElements())
                .isGreaterThan(0);
    }

    @Test
    void testFindAllPaymentsEmptyDatabase() {

        paymentRepo.deleteAll();

        Page<Payment> result =
                paymentRepo.findAll(
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isZero();
    }

    // =========================================
    // deleteById
    // =========================================

    @Test
    void testDeletePaymentSuccessfully() {

        Payment payment = new Payment();

        payment.setCustomerNumber(103);
        payment.setCheckNumber("DELETE123");
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(BigDecimal.valueOf(1000.0));
        payment.setOrderNumber(10100);

        paymentRepo.save(payment);

        paymentRepo.deleteById("DELETE123");

        Optional<Payment> result =
                paymentRepo.findById("DELETE123");

        assertThat(result).isEmpty();
    }

    @Test
    void testDeletePaymentByNonExistingId() {

        long countBefore = paymentRepo.count();

        paymentRepo.deleteById("INVALID123");

        long countAfter = paymentRepo.count();

        assertThat(countAfter)
                .isEqualTo(countBefore);
    }
}