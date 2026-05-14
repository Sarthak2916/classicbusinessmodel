package com.project.cmb.repository;

import com.project.cmb.entity.Customer;
import com.project.cmb.repo.CustomerRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepoTest {

    @Autowired
    private CustomerRepo customerRepository;

    // =====================================================
    // CUSTOMER LIST PAGE TESTS
    // =====================================================

    @Test
    @DisplayName("Test Find By Customer Name Containing Ignore Case")
    void testFindByCustomerNameContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository.findByCustomerNameContainingIgnoreCase(
                        "atelier",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Phone Containing")
    void testFindByPhoneContaining() {

        Page<Customer> customers =
                customerRepository.findByPhoneContaining(
                        "40",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Country")
    void testFindByCountry() {

        Page<Customer> customers =
                customerRepository.findByCountry(
                        "France",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By City")
    void testFindByCity() {

        Page<Customer> customers =
                customerRepository.findByCity(
                        "Nantes",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Postal Code")
    void testFindByPostalCode() {

        Page<Customer> customers =
                customerRepository.findByPostalCode(
                        "44000",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Credit Limit Between")
    void testFindByCreditLimitBetween() {

        Page<Customer> customers =
                customerRepository.findByCreditLimitBetween(
                        BigDecimal.valueOf(10000),
                        BigDecimal.valueOf(50000),
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Credit Limit Greater Than Equal")
    void testFindByCreditLimitGreaterThanEqual() {

        Page<Customer> customers =
                customerRepository.findByCreditLimitGreaterThanEqual(
                        BigDecimal.valueOf(20000),
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Credit Limit Less Than Equal")
    void testFindByCreditLimitLessThanEqual() {

        Page<Customer> customers =
                customerRepository.findByCreditLimitLessThanEqual(
                        BigDecimal.valueOf(50000),
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Country And Credit Limit Between")
    void testFindByCountryAndCreditLimitBetween() {

        Page<Customer> customers =
                customerRepository.findByCountryAndCreditLimitBetween(
                        "France",
                        BigDecimal.valueOf(10000),
                        BigDecimal.valueOf(50000),
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Customer Name And Country")
    void testFindByCustomerNameContainingIgnoreCaseAndCountry() {

        Page<Customer> customers =
                customerRepository
                        .findByCustomerNameContainingIgnoreCaseAndCountry(
                                "atelier",
                                "France",
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    // =====================================================
    // CUSTOMER DETAIL TESTS
    // =====================================================

    @Test
    @DisplayName("Test Find By Customer Number")
    void testFindByCustomerNumber() {

        Optional<Customer> customer =
                customerRepository.findByCustomerNumber(103);

        assertTrue(customer.isPresent());
    }

    @Test
    @DisplayName("Test Find By Phone")
    void testFindByPhone() {

        Optional<Customer> customer =
                customerRepository.findByPhone("40.32.2555");

        assertNotNull(customer);
    }

    @Test
    @DisplayName("Test Find By Customer Name")
    void testFindByCustomerName() {

        Optional<Customer> customer =
                customerRepository.findByCustomerName("Atelier Graphique");

        assertNotNull(customer);
    }

    @Test
    @DisplayName("Test Find By Sales Rep Employee Number")
    void testFindBySalesRepEmployeeNumber() {

        List<Customer> customers =
                customerRepository.findBySalesRepEmployeeNumber(1370);

        assertNotNull(customers);
    }

    // =====================================================
    // ADD CUSTOMER TESTS
    // =====================================================

    @Test
    @DisplayName("Test Exists By Customer Name")
    void testExistsByCustomerName() {

        boolean exists =
                customerRepository.existsByCustomerName("Atelier Graphique");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Test Exists By Phone")
    void testExistsByPhone() {

        boolean exists =
                customerRepository.existsByPhone("40.32.2555");

        assertTrue(exists);
    }

    @Test
    @DisplayName("Test Find By Contact First Name")
    void testFindByContactFirstNameContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository
                        .findByContactFirstNameContainingIgnoreCase(
                                "car",
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Contact Last Name")
    void testFindByContactLastNameContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository
                        .findByContactLastNameContainingIgnoreCase(
                                "sch",
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    // =====================================================
    // EDIT CUSTOMER TESTS
    // =====================================================

    @Test
    @DisplayName("Test Find By Customer Number And Phone")
    void testFindByCustomerNumberAndPhone() {

        Optional<Customer> customer =
                customerRepository.findByCustomerNumberAndPhone(
                        103,
                        "40.32.2555"
                );

        assertTrue(customer.isPresent());
    }

    @Test
    @DisplayName("Test Find By Credit Limit Greater Than")
    void testFindByCreditLimitGreaterThan() {

        Page<Customer> customers =
                customerRepository.findByCreditLimitGreaterThan(
                        BigDecimal.valueOf(10000),
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Credit Limit Less Than")
    void testFindByCreditLimitLessThan() {

        Page<Customer> customers =
                customerRepository.findByCreditLimitLessThan(
                        BigDecimal.valueOf(100000),
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    // =====================================================
    // ADDRESS UPDATE TESTS
    // =====================================================

    @Test
    @DisplayName("Test Find By Address Line1")
    void testFindByAddressLine1ContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository.findByAddressLine1ContainingIgnoreCase(
                        "royale",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Country Containing Ignore Case")
    void testFindByCountryContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository.findByCountryContainingIgnoreCase(
                        "fra",
                        PageRequest.of(0, 5)
                );

        assertNotNull(customers);
    }

    // =====================================================
    // DASHBOARD TESTS
    // =====================================================

    @Test
    @DisplayName("Test Count Customers")
    void testCount() {

        long count = customerRepository.count();

        assertTrue(count > 0);
    }

    @Test
    @DisplayName("Test Count Distinct Countries")
    void testCountDistinctByCountryIsNotNull() {

        long count =
                customerRepository.countDistinctByCountryIsNotNull();

        assertTrue(count > 0);
    }

    @Test
    @DisplayName("Test Find Top 5 By Credit Limit Desc")
    void testFindTop5ByOrderByCreditLimitDesc() {

        List<Customer> customers =
                customerRepository.findTop5ByOrderByCreditLimitDesc();

        assertEquals(5, customers.size());
    }

    @Test
    @DisplayName("Test Find Top 5 By Customer Number Desc")
    void testFindTop5ByOrderByCustomerNumberDesc() {

        List<Customer> customers =
                customerRepository.findTop5ByOrderByCustomerNumberDesc();

        assertNotNull(customers);
    }

    // =====================================================
    // EXTRA FILTER TESTS
    // =====================================================

    @Test
    @DisplayName("Test Find By Country And City")
    void testFindByCountryContainingIgnoreCaseAndCityContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository
                        .findByCountryContainingIgnoreCaseAndCityContainingIgnoreCase(
                                "fra",
                                "nan",
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Customer Name And City")
    void testFindByCustomerNameContainingIgnoreCaseAndCityContainingIgnoreCase() {

        Page<Customer> customers =
                customerRepository
                        .findByCustomerNameContainingIgnoreCaseAndCityContainingIgnoreCase(
                                "atelier",
                                "nan",
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Customer Name And Credit Range")
    void testFindByCustomerNameContainingIgnoreCaseAndCreditLimitBetween() {

        Page<Customer> customers =
                customerRepository
                        .findByCustomerNameContainingIgnoreCaseAndCreditLimitBetween(
                                "atelier",
                                BigDecimal.valueOf(10000),
                                BigDecimal.valueOf(50000),
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Country And Credit Limit Greater Than Equal")
    void testFindByCountryAndCreditLimitGreaterThanEqual() {

        Page<Customer> customers =
                customerRepository
                        .findByCountryAndCreditLimitGreaterThanEqual(
                                "France",
                                BigDecimal.valueOf(10000),
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find By Country And Credit Limit Less Than Equal")
    void testFindByCountryAndCreditLimitLessThanEqual() {

        Page<Customer> customers =
                customerRepository
                        .findByCountryAndCreditLimitLessThanEqual(
                                "France",
                                BigDecimal.valueOf(50000),
                                PageRequest.of(0, 5)
                        );

        assertNotNull(customers);
    }

    // =====================================================
    // SORTING TESTS
    // =====================================================

    @Test
    @DisplayName("Test Find Top 10 By Customer Name Asc")
    void testFindTop10ByOrderByCustomerNameAsc() {

        List<Customer> customers =
                customerRepository.findTop10ByOrderByCustomerNameAsc();

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find Top 10 By Credit Limit Asc")
    void testFindTop10ByOrderByCreditLimitAsc() {

        List<Customer> customers =
                customerRepository.findTop10ByOrderByCreditLimitAsc();

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find Top 10 By Credit Limit Desc")
    void testFindTop10ByOrderByCreditLimitDesc() {

        List<Customer> customers =
                customerRepository.findTop10ByOrderByCreditLimitDesc();

        assertNotNull(customers);
    }

    @Test
    @DisplayName("Test Find Top 10 By Country Asc")
    void testFindTop10ByOrderByCountryAsc() {

        List<Customer> customers =
                customerRepository.findTop10ByOrderByCountryAsc();

        assertNotNull(customers);
    }
}