package com.project.cmb.service;

import com.project.cmb.entity.Customer;
import com.project.cmb.entity.Order;
import com.project.cmb.entity.Payment;
import com.project.cmb.entity.PaymentId;
import com.project.cmb.repo.CustomerRepo;
import com.project.cmb.repo.OrderRepo;
import com.project.cmb.repo.PaymentRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private CustomerRepo customerRepo;
    @Mock private OrderRepo orderRepo;
    @Mock private PaymentRepo paymentRepo;

    @InjectMocks private CustomerService customerService;

    private Customer c1, c2, c3;
    private Order order1, order2;
    private Payment payment1, payment2;

    @BeforeEach
    void setup() {
        c1 = new Customer();
        c1.setCustomerNumber(101);
        c1.setCustomerName("Alpha Corp");
        c1.setCountry("USA");
        c1.setCreditLimit(new BigDecimal("10000.00"));

        c2 = new Customer();
        c2.setCustomerNumber(102);
        c2.setCustomerName("Beta Ltd");
        c2.setCountry("France");
        c2.setCreditLimit(new BigDecimal("20000.00"));

        c3 = new Customer();
        c3.setCustomerNumber(103);
        c3.setCustomerName("Gamma Inc");
        c3.setCountry("USA");
        c3.setCreditLimit(new BigDecimal("30000.00"));

        order1 = new Order();
        order1.setOrderNumber(90001);
        order1.setStatus("Shipped");
        order1.setOrderDate(LocalDate.of(2024, 1, 10));
        order1.setCustomer(c1);

        order2 = new Order();
        order2.setOrderNumber(90002);
        order2.setStatus("In Process");
        order2.setOrderDate(LocalDate.of(2024, 2, 5));
        order2.setCustomer(c1);

        PaymentId pid1 = new PaymentId(101, "CHK001");
        payment1 = new Payment();
        payment1.setId(pid1);
        payment1.setAmount(new BigDecimal("1500.00"));
        payment1.setPaymentDate(LocalDate.of(2024, 1, 15));

        PaymentId pid2 = new PaymentId(101, "CHK002");
        payment2 = new Payment();
        payment2.setId(pid2);
        payment2.setAmount(new BigDecimal("2500.00"));
        payment2.setPaymentDate(LocalDate.of(2024, 2, 20));
    }

    @Test
    void getTotalCustomers_shouldReturnCount() {
        when(customerRepo.count()).thenReturn(3L);
        assertThat(customerService.getTotalCustomers()).isEqualTo(3L);
        verify(customerRepo).count();
    }

    @Test
    void getTotalCountries_shouldReturnDistinctCount() {
        when(customerRepo.findAll()).thenReturn(List.of(c1, c2, c3));
        // c1 and c3 are USA, c2 is France — 2 distinct countries
        assertThat(customerService.getTotalCountries()).isEqualTo(2L);
        verify(customerRepo).findAll();
    }

    @Test
    void getTotalCountries_emptyRepo_shouldReturnZero() {
        when(customerRepo.findAll()).thenReturn(List.of());
        assertThat(customerService.getTotalCountries()).isEqualTo(0L);
    }

    @Test
    void getTotalCountries_allSameCountry_shouldReturnOne() {
        when(customerRepo.findAll()).thenReturn(List.of(c1, c3)); // both USA
        assertThat(customerService.getTotalCountries()).isEqualTo(1L);
    }

    @Test
    void getAvgCreditLimit_shouldReturnCorrectAverage() {
        // (10000 + 20000 + 30000) / 3 = 20000.00
        when(customerRepo.findAll()).thenReturn(List.of(c1, c2, c3));
        assertThat(customerService.getAvgCreditLimit())
                .isEqualByComparingTo("20000.00");
        verify(customerRepo).findAll();
    }

    @Test
    void getAvgCreditLimit_singleCustomer_shouldReturnThatLimit() {
        when(customerRepo.findAll()).thenReturn(List.of(c1));
        assertThat(customerService.getAvgCreditLimit())
                .isEqualByComparingTo("10000.00");
    }

    @Test
    void getAvgCreditLimit_emptyRepo_shouldReturnZero() {
        when(customerRepo.findAll()).thenReturn(List.of());
        assertThat(customerService.getAvgCreditLimit())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getOrdersByCustomer_shouldReturnAllOrders() {
        when(orderRepo.findByCustomer_CustomerNumber(101))
                .thenReturn(List.of(order1, order2));

        List<Order> result = customerService.getOrdersByCustomer(101);

        assertThat(result).hasSize(2);
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90001))).isTrue();
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90002))).isTrue();
        verify(orderRepo).findByCustomer_CustomerNumber(101);
    }

    @Test
    void getOrdersByCustomer_noOrders_shouldReturnEmpty() {
        when(orderRepo.findByCustomer_CustomerNumber(999)).thenReturn(List.of());
        assertThat(customerService.getOrdersByCustomer(999)).isEmpty();
    }

    @Test
    void getPaymentsByCustomer_shouldReturnAllPayments() {
        when(paymentRepo.findById_CustomerNumber(101))
                .thenReturn(List.of(payment1, payment2));

        List<Payment> result = customerService.getPaymentsByCustomer(101);

        assertThat(result).hasSize(2);
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("CHK001"))).isTrue();
        assertThat(result.stream()
                .anyMatch(p -> p.getId().getCheckNumber().equals("CHK002"))).isTrue();
        verify(paymentRepo).findById_CustomerNumber(101);
    }

    @Test
    void getPaymentsByCustomer_noPayments_shouldReturnEmpty() {
        when(paymentRepo.findById_CustomerNumber(999)).thenReturn(List.of());
        assertThat(customerService.getPaymentsByCustomer(999)).isEmpty();
    }
}