package com.project.cmb.repository;

import com.project.cmb.entity.Customer;
import com.project.cmb.entity.Order;
import com.project.cmb.repo.CustomerRepo;
import com.project.cmb.repo.OrderRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepoTest {

    @Autowired private OrderRepo orderRepo;
    @Autowired private CustomerRepo customerRepo;

    private Customer customer103;
    private Customer customer112;

    @BeforeEach
    void setup() {
        // Load existing customers from real DB — 103 and 112 exist in classicmodels
        customer103 = customerRepo.findById(103).orElseThrow();
        customer112 = customerRepo.findById(112).orElseThrow();

        Order o1 = new Order();
        o1.setOrderNumber(90001);
        o1.setOrderDate(LocalDate.of(2024, 1, 10));
        o1.setRequiredDate(LocalDate.of(2024, 1, 20));
        o1.setStatus("Shipped");
        o1.setCustomer(customer103);
        orderRepo.save(o1);

        Order o2 = new Order();
        o2.setOrderNumber(90002);
        o2.setOrderDate(LocalDate.of(2024, 2, 5));
        o2.setRequiredDate(LocalDate.of(2024, 2, 15));
        o2.setStatus("In Process");
        o2.setCustomer(customer103);
        orderRepo.save(o2);

        Order o3 = new Order();
        o3.setOrderNumber(90003);
        o3.setOrderDate(LocalDate.of(2024, 3, 1));
        o3.setRequiredDate(LocalDate.of(2024, 3, 10));
        o3.setStatus("Cancelled");
        o3.setCustomer(customer112);
        orderRepo.save(o3);
    }

    // --- findAll ---

    @Test
    void repo_findAll_shouldReturnOrders() {
        assertThat(orderRepo.findAll()).isNotEmpty();
    }

    // --- findById ---

    @Test
    void repo_findById_shouldReturnOrder() {
        Optional<Order> result = orderRepo.findById(90001);
        assertThat(result).isPresent();
        assertThat(result.get().getStatus()).isEqualTo("Shipped");
    }

    @Test
    void repo_findById_whenNotExists_shouldReturnEmpty() {
        assertThat(orderRepo.findById(99999)).isEmpty();
    }

    // --- save ---

    @Test
    void repo_save_shouldPersistNewOrder() {
        Order o = new Order();
        o.setOrderNumber(90004);
        o.setOrderDate(LocalDate.of(2024, 4, 1));
        o.setRequiredDate(LocalDate.of(2024, 4, 10));
        o.setStatus("In Process");
        o.setCustomer(customer103);
        orderRepo.save(o);
        assertThat(orderRepo.findById(90004)).isPresent();
    }

    @Test
    void repo_save_shouldIncreaseCount() {
        long countBefore = orderRepo.count();
        Order o = new Order();
        o.setOrderNumber(90005);
        o.setOrderDate(LocalDate.of(2024, 5, 1));
        o.setRequiredDate(LocalDate.of(2024, 5, 10));
        o.setStatus("In Process");
        o.setCustomer(customer112);
        orderRepo.save(o);
        assertThat(orderRepo.count()).isEqualTo(countBefore + 1);
    }

    // --- update ---

    @Test
    void repo_update_status_shouldModify() {
        Order o = orderRepo.findById(90002).orElseThrow();
        o.setStatus("Shipped");
        orderRepo.save(o);
        assertThat(orderRepo.findById(90002).orElseThrow().getStatus()).isEqualTo("Shipped");
    }

    @Test
    void repo_update_shippedDate_shouldModify() {
        Order o = orderRepo.findById(90001).orElseThrow();
        o.setShippedDate(LocalDate.of(2024, 1, 15));
        orderRepo.save(o);
        assertThat(orderRepo.findById(90001).orElseThrow().getShippedDate())
                .isEqualTo(LocalDate.of(2024, 1, 15));
    }

    @Test
    void repo_update_shouldNotChangeOtherFields() {
        Order o = orderRepo.findById(90001).orElseThrow();
        o.setStatus("Resolved");
        orderRepo.save(o);
        Order updated = orderRepo.findById(90001).orElseThrow();
        assertThat(updated.getCustomer().getCustomerNumber()).isEqualTo(103);
        assertThat(updated.getOrderDate()).isEqualTo(LocalDate.of(2024, 1, 10));
    }

    // --- delete ---

    @Test
    void repo_deleteById_shouldRemoveOrder() {
        orderRepo.deleteById(90003);
        assertThat(orderRepo.findById(90003)).isEmpty();
    }

    @Test
    void repo_deleteById_shouldDecreaseCount() {
        long countBefore = orderRepo.count();
        orderRepo.deleteById(90003);
        assertThat(orderRepo.count()).isEqualTo(countBefore - 1);
    }

    // --- findByStatus ---

    @Test
    void repo_findByStatus_shouldReturnMatchingOrders() {
        List<Order> result = orderRepo.findByStatus("Shipped");
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90001))).isTrue();
    }

    @Test
    void repo_findByStatus_shouldNotReturnOtherStatuses() {
        List<Order> result = orderRepo.findByStatus("Shipped");
        assertThat(result.stream()
                .allMatch(o -> o.getStatus().equals("Shipped"))).isTrue();
    }

    @Test
    void repo_findByStatus_noMatch_shouldReturnEmpty() {
        List<Order> result = orderRepo.findByStatus("NonExistentStatus");
        assertThat(result).isEmpty();
    }

    // --- findByCustomer_CustomerNumber ---

    @Test
    void repo_findByCustomerNumber_shouldReturnCustomerOrders() {
        List<Order> result = orderRepo.findByCustomer_CustomerNumber(103);
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90001))).isTrue();
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90002))).isTrue();
    }

    @Test
    void repo_findByCustomerNumber_shouldNotReturnOtherCustomers() {
        List<Order> result = orderRepo.findByCustomer_CustomerNumber(103);
        assertThat(result.stream()
                .allMatch(o -> o.getCustomer().getCustomerNumber().equals(103))).isTrue();
    }

    @Test
    void repo_findByCustomerNumber_noMatch_shouldReturnEmpty() {
        List<Order> result = orderRepo.findByCustomer_CustomerNumber(99999);
        assertThat(result).isEmpty();
    }

    // --- findByCustomer_CustomerNameContainingIgnoreCase ---

    @Test
    void repo_findByCustomerName_shouldReturnMatch() {
        // customer103's name exists in real DB
        String name = customer103.getCustomerName();
        Page<Order> result = orderRepo.findByCustomer_CustomerNameContainingIgnoreCase(
                name.substring(0, 4), PageRequest.of(0, 10));
        assertThat(result.getContent().stream()
                .anyMatch(o -> o.getOrderNumber().equals(90001))).isTrue();
    }

    @Test
    void repo_findByCustomerName_noMatch_shouldReturnEmpty() {
        Page<Order> result = orderRepo.findByCustomer_CustomerNameContainingIgnoreCase(
                "xyzxyzxyz", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    // --- findByOrderDateBetween ---

    @Test
    void repo_findByDateBetween_shouldReturnOrdersInRange() {
        List<Order> result = orderRepo.findByOrderDateBetween(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 28));
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90001))).isTrue();
        assertThat(result.stream()
                .anyMatch(o -> o.getOrderNumber().equals(90002))).isTrue();
    }

    @Test
    void repo_findByDateBetween_shouldNotReturnOutsideRange() {
        List<Order> result = orderRepo.findByOrderDateBetween(
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 2, 28));
        assertThat(result.stream()
                .noneMatch(o -> o.getOrderNumber().equals(90003))).isTrue();
    }

    @Test
    void repo_findByDateBetween_noMatch_shouldReturnEmpty() {
        List<Order> result = orderRepo.findByOrderDateBetween(
                LocalDate.of(2000, 1, 1), LocalDate.of(2000, 12, 31));
        assertThat(result).isEmpty();
    }

    // --- countByStatus ---

    @Test
    void repo_countByStatus_shouldReturnCorrectCount() {
        long count = orderRepo.countByStatus("In Process");
        assertThat(count).isGreaterThanOrEqualTo(1);
    }

    @Test
    void repo_countByStatus_noMatch_shouldReturnZero() {
        long count = orderRepo.countByStatus("NonExistentStatus");
        assertThat(count).isEqualTo(0);
    }

    // --- findTop5ByOrderByOrderDateDesc ---

    @Test
    void repo_findTop5RecentOrders_shouldReturnAtMost5() {
        List<Order> result = orderRepo.findTop5ByOrderByOrderDateDesc();
        assertThat(result.size()).isLessThanOrEqualTo(5);
    }

    @Test
    void repo_findTop5RecentOrders_shouldBeDescendingOrder() {
        List<Order> result = orderRepo.findTop5ByOrderByOrderDateDesc();
        for (int i = 0; i < result.size() - 1; i++) {
            assertThat(result.get(i).getOrderDate()
                    .isAfter(result.get(i + 1).getOrderDate()) ||
                    result.get(i).getOrderDate()
                            .isEqual(result.get(i + 1).getOrderDate())).isTrue();
        }
    }
}