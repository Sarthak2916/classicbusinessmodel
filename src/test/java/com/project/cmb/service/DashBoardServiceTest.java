package com.project.cmb.service;

import com.project.cmb.entity.Order;
import com.project.cmb.entity.Payment;
import com.project.cmb.repo.CustomerRepo;
import com.project.cmb.repo.EmployeeRepo;
import com.project.cmb.repo.OrderRepo;
import com.project.cmb.repo.PaymentRepo;
import com.project.cmb.repo.ProductRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashBoardServiceTest {

    @Mock private EmployeeRepo employeeRepo;
    @Mock private CustomerRepo customerRepo;
    @Mock private OrderRepo orderRepo;
    @Mock private ProductRepo productRepo;
    @Mock private PaymentRepo paymentRepo;

    @InjectMocks
    private DashBoardService dashBoardService;

    // --- getEmployeesCount ---

    @Test
    void getEmployeesCount_shouldReturnCount() {
        when(employeeRepo.count()).thenReturn(5L);
        assertThat(dashBoardService.getEmployeesCount()).isEqualTo(5L);
        verify(employeeRepo).count();
    }

    // --- getCustomersCount ---

    @Test
    void getCustomersCount_shouldReturnCount() {
        when(customerRepo.count()).thenReturn(10L);
        assertThat(dashBoardService.getCustomersCount()).isEqualTo(10L);
        verify(customerRepo).count();
    }

    // --- getOrdersCount ---

    @Test
    void getOrdersCount_shouldReturnCount() {
        when(orderRepo.count()).thenReturn(12L);
        assertThat(dashBoardService.getOrdersCount()).isEqualTo(12L);
        verify(orderRepo).count();
    }

    // --- getProductsCount ---

    @Test
    void getProductsCount_shouldReturnCount() {
        when(productRepo.count()).thenReturn(8L);
        assertThat(dashBoardService.getProductsCount()).isEqualTo(8L);
        verify(productRepo).count();
    }

    // --- getPaymentsCount ---

    @Test
    void getPaymentsCount_shouldReturnCount() {
        when(paymentRepo.count()).thenReturn(20L);
        assertThat(dashBoardService.getPaymentsCount()).isEqualTo(20L);
        verify(paymentRepo).count();
    }

    // --- getTotalSalesAmount ---

    @Test
    void getTotalSalesAmount_shouldReturnSumOfAllPayments() {
        Payment p1 = new Payment();
        p1.setAmount(new BigDecimal("1500.00"));

        Payment p2 = new Payment();
        p2.setAmount(new BigDecimal("2500.00"));

        when(paymentRepo.findAll()).thenReturn(List.of(p1, p2));

        assertThat(dashBoardService.getTotalSalesAmount())
                .isEqualByComparingTo("4000.00");
        verify(paymentRepo).findAll();
    }

    @Test
    void getTotalSalesAmount_noPayments_shouldReturnZero() {
        when(paymentRepo.findAll()).thenReturn(List.of());
        assertThat(dashBoardService.getTotalSalesAmount())
                .isEqualByComparingTo(BigDecimal.ZERO);
    }

    // --- getRecentOrders ---

    @Test
    void getRecentOrders_shouldReturnTop5() {
        Order o1 = buildOrder(90001, LocalDate.of(2024, 3, 1));
        Order o2 = buildOrder(90002, LocalDate.of(2024, 2, 1));

        when(orderRepo.findTop5ByOrderByOrderDateDesc()).thenReturn(List.of(o1, o2));

        List<Order> result = dashBoardService.getRecentOrders();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderNumber()).isEqualTo(90001);
        verify(orderRepo).findTop5ByOrderByOrderDateDesc();
    }

    // --- getPendingOrdersCount ---

    @Test
    void getPendingOrdersCount_shouldReturnCountByStatus() {
        when(orderRepo.countByStatus("In Process")).thenReturn(3L);
        assertThat(dashBoardService.getPendingOrdersCount()).isEqualTo(3L);
        verify(orderRepo).countByStatus("In Process");
    }

    // --- getOrdersPerMonth ---

    @Test
    void getOrdersPerMonth_shouldGroupByMonth() {
        Order o1 = buildOrder(101, LocalDate.of(2024, 1, 15));
        Order o2 = buildOrder(102, LocalDate.of(2024, 1, 28));
        Order o3 = buildOrder(103, LocalDate.of(2024, 3, 5));

        when(orderRepo.findAll()).thenReturn(List.of(o1, o2, o3));

        Map<String, Long> result = dashBoardService.getOrdersPerMonth();

        assertThat(result)
                .containsEntry("JANUARY", 2L)
                .containsEntry("MARCH", 1L)
                .hasSize(2);
        verify(orderRepo).findAll();
    }

    @Test
    void getOrdersPerMonth_noOrders_shouldReturnEmptyMap() {
        when(orderRepo.findAll()).thenReturn(List.of());
        assertThat(dashBoardService.getOrdersPerMonth()).isEmpty();
        verify(orderRepo).findAll();
    }

    // --- helper ---

    private Order buildOrder(Integer orderNumber, LocalDate orderDate) {
        Order o = new Order();
        o.setOrderNumber(orderNumber);
        o.setOrderDate(orderDate);
        o.setRequiredDate(orderDate.plusDays(7));
        o.setStatus("In Process");
        // customer left null — dashboard tests don't need it
        return o;
    }
}