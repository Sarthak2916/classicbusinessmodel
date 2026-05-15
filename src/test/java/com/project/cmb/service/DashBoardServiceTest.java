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

    @Mock
    private EmployeeRepo employeeRepo;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private DashBoardService dashBoardService;

    @Test
    void getEmployeesCount_shouldReturnEmployeeRepositoryCount() {
        when(employeeRepo.count()).thenReturn(5L);

        long result = dashBoardService.getEmployeesCount();

        assertThat(result).isEqualTo(5L);
        verify(employeeRepo).count();
    }

    @Test
    void getCustomersCount_shouldReturnCustomerRepositoryCount() {
        when(customerRepo.count()).thenReturn(7L);

        long result = dashBoardService.getCustomersCount();

        assertThat(result).isEqualTo(7L);
        verify(customerRepo).count();
    }

    @Test
    void getOrdersCount_shouldReturnOrderRepositoryCount() {
        when(orderRepo.count()).thenReturn(12L);

        long result = dashBoardService.getOrdersCount();

        assertThat(result).isEqualTo(12L);
        verify(orderRepo).count();
    }

    @Test
    void getProductsCount_shouldReturnProductRepositoryCount() {
        when(productRepo.count()).thenReturn(8L);

        long result = dashBoardService.getProductsCount();

        assertThat(result).isEqualTo(8L);
        verify(productRepo).count();
    }

    @Test
    void getPaymentsCount_shouldReturnPaymentRepositoryCount() {
        when(paymentRepo.count()).thenReturn(15L);

        long result = dashBoardService.getPaymentsCount();

        assertThat(result).isEqualTo(15L);
        verify(paymentRepo).count();
    }

    @Test
    void getTotalSalesAmount_shouldReturnSumOfPaymentAmounts() {
        Payment firstPayment = buildPayment("CHK001", new BigDecimal("120.50"));
        Payment secondPayment = buildPayment("CHK002", new BigDecimal("79.25"));
        Payment thirdPayment = buildPayment("CHK003", new BigDecimal("300.00"));

        when(paymentRepo.findAll())
                .thenReturn(List.of(firstPayment, secondPayment, thirdPayment));

        BigDecimal result = dashBoardService.getTotalSalesAmount();

        assertThat(result).isEqualByComparingTo(new BigDecimal("499.75"));
        verify(paymentRepo).findAll();
    }

    @Test
    void getTotalSalesAmount_whenNoPayments_shouldReturnZero() {
        when(paymentRepo.findAll()).thenReturn(List.of());

        BigDecimal result = dashBoardService.getTotalSalesAmount();

        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
        verify(paymentRepo).findAll();
    }

    @Test
    void getOrdersPerMonth_shouldGroupOrdersByMonthName() {
        Order januaryOrder = buildOrder(101, LocalDate.of(2024, 1, 15));
        Order anotherJanuaryOrder = buildOrder(102, LocalDate.of(2024, 1, 28));
        Order marchOrder = buildOrder(103, LocalDate.of(2024, 3, 5));

        when(orderRepo.findAll())
                .thenReturn(List.of(januaryOrder, anotherJanuaryOrder, marchOrder));

        Map<String, Long> result = dashBoardService.getOrdersPerMonth();

        assertThat(result)
                .containsEntry("JANUARY", 2L)
                .containsEntry("MARCH", 1L)
                .hasSize(2);
        verify(orderRepo).findAll();
    }

    @Test
    void getOrdersPerMonth_whenNoOrders_shouldReturnEmptyMap() {
        when(orderRepo.findAll()).thenReturn(List.of());

        Map<String, Long> result = dashBoardService.getOrdersPerMonth();

        assertThat(result).isEmpty();
        verify(orderRepo).findAll();
    }

    private Order buildOrder(Integer orderNumber, LocalDate orderDate) {
        return new Order(
                orderNumber,
                orderDate,
                orderDate.plusDays(7),
                null,
                "In Process",
                null,
                100
        );
    }

    private Payment buildPayment(String checkNumber, BigDecimal amount) {
        return new Payment(
                100,
                checkNumber,
                LocalDate.of(2024, 1, 15),
                amount
        );
    }
}
