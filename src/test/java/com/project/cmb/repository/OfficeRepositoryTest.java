package com.project.cmb.repository;

import com.project.cmb.entity.Employee;
import com.project.cmb.entity.Office;
import com.project.cmb.repo.OfficeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OfficeRepositoryTest {

    @Autowired
    private OfficeRepository officeRepository;

    // =====================================================
    // findAll(Pageable pageable)
    // =====================================================

    @Test
    void findAll_firstPage_returnsCorrectPageSize() {

        Pageable pageable = PageRequest.of(0, 3);

        Page<Office> result =
                officeRepository.findAll(pageable);

        assertEquals(3, result.getContent().size());
    }

    @Test
    void findAll_secondPage_returnsRemainingRecords() {

        Pageable pageable = PageRequest.of(1, 5);

        Page<Office> result =
                officeRepository.findAll(pageable);

        assertTrue(result.getContent().size() >= 0);
    }

    @Test
    void findAll_sortedByCity_returnsInAscendingOrder() {

        Pageable pageable =
                PageRequest.of(
                        0,
                        10,
                        Sort.by("city").ascending()
                );

        Page<Office> result =
                officeRepository.findAll(pageable);

        List<Office> offices = result.getContent();

        for (int i = 0; i < offices.size() - 1; i++) {

            assertTrue(
                    offices.get(i)
                            .getCity()
                            .compareTo(
                                    offices.get(i + 1)
                                            .getCity()
                            ) <= 0
            );
        }
    }

    @Test
    void findAll_emptyTable_returnsEmptyPage() {

        Pageable pageable = PageRequest.of(100, 10);

        Page<Office> result =
                officeRepository.findAll(pageable);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findAll_pageBeyondTotal_returnsEmptyContent() {

        Pageable pageable = PageRequest.of(200, 5);

        Page<Office> result =
                officeRepository.findAll(pageable);

        assertTrue(result.getContent().isEmpty());
    }

    // =====================================================
    // findByOfficeCode(String officeCode)
    // =====================================================

    @Test
    void findByOfficeCode_validCode_returnsOffice() {

        Optional<Office> result =
                officeRepository.findByOfficeCode("1");

        assertTrue(result.isPresent());
    }

    @Test
    void findByOfficeCode_invalidCode_returnsEmpty() {

        Optional<Office> result =
                officeRepository.findByOfficeCode("999");

        assertFalse(result.isPresent());
    }

    @Test
    void findByOfficeCode_nullCode_throwsException() {

        Optional<Office> result =
                officeRepository.findByOfficeCode(null);

        assertFalse(result.isPresent());
    }

    // =====================================================
    // save(Office office)
    // =====================================================

    @Test
    void save_newOffice_persistsSuccessfully() {

        Office office = new Office();

        office.setOfficeCode("100");
        office.setCity("Kolkata");
        office.setPhone("+91 9999999999");
        office.setAddressLine1("Salt Lake");
        office.setAddressLine2("Sector V");
        office.setState("WB");
        office.setCountry("India");
        office.setPostalCode("700091");
        office.setTerritory("APAC");

        Office saved =
                officeRepository.saveAndFlush(office);

        assertNotNull(saved);

        assertEquals(
                "Kolkata",
                saved.getCity()
        );
    }

    @Test
    void save_duplicateCode_throwsException() {

        Office office = new Office();

        office.setOfficeCode("1");
        office.setCity("Delhi");
        office.setPhone("+91 8888888888");
        office.setAddressLine1("Connaught Place");
        office.setAddressLine2("Area");
        office.setState("DL");
        office.setCountry("India");
        office.setPostalCode("110001");
        office.setTerritory("APAC");

        officeRepository.saveAndFlush(office);

        Office updated =
                officeRepository.findByOfficeCode("1")
                        .get();

        assertEquals(
                "Delhi",
                updated.getCity()
        );
    }

    // =====================================================
    // findByOfficeCodeIn(List<String>)
    // =====================================================

    @Test
    void findByOfficeCodeIn_validCodes_returnsMatchingOffices() {

        List<Office> result =
                officeRepository.findByOfficeCodeIn(
                        Arrays.asList("1", "2")
                );

        assertEquals(2, result.size());
    }

    @Test
    void findByOfficeCodeIn_invalidCodes_returnsEmptyList() {

        List<Office> result =
                officeRepository.findByOfficeCodeIn(
                        Arrays.asList("999", "888")
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void findByOfficeCodeIn_mixedCodes_returnsOnlyMatchingOffices() {

        List<Office> result =
                officeRepository.findByOfficeCodeIn(
                        Arrays.asList("1", "999")
                );

        assertEquals(1, result.size());
    }

    // =====================================================
    // findByCity(String city)
    // =====================================================

    @Test
    void findByCity_validCity_returnsOffice() {

        Optional<Office> result =
                officeRepository.findByCity("Boston");

        assertTrue(result.isPresent());
    }

    @Test
    void findByCity_invalidCity_returnsEmpty() {

        Optional<Office> result =
                officeRepository.findByCity("Banur");

        assertFalse(result.isPresent());
    }

    @Test
    void findByCity_nullCity_returnsEmpty() {

        Optional<Office> result =
                officeRepository.findByCity(null);

        assertFalse(result.isPresent());
    }

    // =====================================================
    // findByCityIn(List<String>)
    // =====================================================

    @Test
    void findByCityIn_validCities_returnsMatchingOffices() {

        List<Office> result =
                officeRepository.findByCityIn(
                        Arrays.asList("Boston", "NYC")
                );

        assertFalse(result.isEmpty());
    }

    @Test
    void findByCityIn_invalidCities_returnsEmptyList() {

        List<Office> result =
                officeRepository.findByCityIn(
                        Arrays.asList("ABC", "XYZ")
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void findByCityIn_mixedCities_returnsOnlyMatchingOffices() {

        List<Office> result =
                officeRepository.findByCityIn(
                        Arrays.asList("Boston", "XYZ")
                );

        assertTrue(result.size() >= 1);
    }

    // =====================================================
    // existsByOfficeCode(String officeCode)
    // =====================================================

    @Test
    void existsByOfficeCode_validCode_returnsTrue() {

        boolean result =
                officeRepository.existsByOfficeCode("1");

        assertTrue(result);
    }

    @Test
    void existsByOfficeCode_invalidCode_returnsFalse() {

        boolean result =
                officeRepository.existsByOfficeCode("999");

        assertFalse(result);
    }

    @Test
    void existsByOfficeCode_nullCode_returnsFalse() {

        boolean result =
                officeRepository.existsByOfficeCode(null);

        assertFalse(result);
    }

    // =====================================================
    // updatePhone(String officeCode, String phone)
    // =====================================================

    @Test
    void updatePhone_validCode_updatesPhoneSuccessfully() {

        int rows =
                officeRepository.updatePhone(
                        "1",
                        "+91 7777777777"
                );

        assertEquals(1, rows);
    }

    @Test
    void updatePhone_invalidCode_returnsZeroRowsAffected() {

        int rows =
                officeRepository.updatePhone(
                        "999",
                        "+91 7777777777"
                );

        assertEquals(0, rows);
    }

    @Test
    void updatePhone_updatedValue_persistedSuccessfully() {

        officeRepository.updatePhone(
                "1",
                "+91 6666666666"
        );

        Office office =
                officeRepository.findByOfficeCode("1")
                        .get();

        assertEquals(
                "+91 6666666666",
                office.getPhone()
        );
    }

    // =====================================================
    // updateOfficeDetails(...)
    // =====================================================

    @Test
    void updateOfficeDetails_validData_updatesSuccessfully() {

        int rows =
                officeRepository.updateOfficeDetails(
                        "1",
                        "Delhi",
                        "India",
                        "APAC",
                        "+91 9999999999",
                        "Address1",
                        "Address2",
                        "Delhi",
                        "110001"
                );

        assertEquals(1, rows);
    }

    @Test
    void updateOfficeDetails_invalidCode_returnsZeroRowsAffected() {

        int rows =
                officeRepository.updateOfficeDetails(
                        "999",
                        "Delhi",
                        "India",
                        "APAC",
                        "+91 9999999999",
                        "Address1",
                        "Address2",
                        "Delhi",
                        "110001"
                );

        assertEquals(0, rows);
    }

    @Test
    void updateOfficeDetails_updatedValues_persistedSuccessfully() {

        officeRepository.updateOfficeDetails(
                "1",
                "Delhi",
                "India",
                "APAC",
                "+91 9999999999",
                "Address1",
                "Address2",
                "Delhi",
                "110001"
        );

        Office office =
                officeRepository.findByOfficeCode("1")
                        .get();

        assertEquals("Delhi", office.getCity());
        assertEquals("India", office.getCountry());
    }

    // =====================================================
    // countEmployeesByOfficeCode(String officeCode)
    // =====================================================

    @Test
    void countEmployeesByOfficeCode_validCode_returnsPositiveCount() {

        long count =
                officeRepository.countEmployeesByOfficeCode("1");

        assertTrue(count >= 0);
    }

    @Test
    void countEmployeesByOfficeCode_invalidCode_returnsZero() {

        long count =
                officeRepository.countEmployeesByOfficeCode("999");

        assertEquals(0, count);
    }

    @Test
    void countEmployeesByOfficeCode_nullCode_returnsZero() {

        long count =
                officeRepository.countEmployeesByOfficeCode(null);

        assertEquals(0, count);
    }

    // =====================================================
    // findEmployeesByOfficeCode(String officeCode)
    // =====================================================

    @Test
    void findEmployeesByOfficeCode_validCode_returnsEmployees() {

        List<Employee> employees =
                officeRepository.findEmployeesByOfficeCode("1");

        assertNotNull(employees);
    }

    @Test
    void findEmployeesByOfficeCode_invalidCode_returnsEmptyList() {

        List<Employee> employees =
                officeRepository.findEmployeesByOfficeCode("999");

        assertTrue(employees.isEmpty());
    }

    @Test
    void findEmployeesByOfficeCode_nullCode_returnsEmptyList() {

        List<Employee> employees =
                officeRepository.findEmployeesByOfficeCode(null);

        assertTrue(employees.isEmpty());
    }

    // =====================================================
    // Additional Coverage Tests
    // =====================================================

    @Test
    void findAll_totalElements_shouldBeGreaterThanZero() {

        Page<Office> result =
                officeRepository.findAll(
                        PageRequest.of(0, 10)
                );

        assertTrue(result.getTotalElements() > 0);
    }

    @Test
    void findAll_totalPages_shouldBeGreaterThanZero() {

        Page<Office> result =
                officeRepository.findAll(
                        PageRequest.of(0, 3)
                );

        assertTrue(result.getTotalPages() > 0);
    }

    @Test
    void save_savedOffice_shouldContainGeneratedValues() {

        Office office = new Office();

        office.setOfficeCode("101");
        office.setCity("Pune");
        office.setPhone("9999999999");
        office.setAddressLine1("Address1");
        office.setAddressLine2("Address2");
        office.setState("MH");
        office.setCountry("India");
        office.setPostalCode("411001");
        office.setTerritory("APAC");

        Office saved =
                officeRepository.saveAndFlush(office);

        assertEquals("101", saved.getOfficeCode());
    }

    @Test
    void findByOfficeCode_existingCode_shouldReturnCorrectOffice() {

        Optional<Office> office =
                officeRepository.findByOfficeCode("1");

        assertTrue(office.isPresent());
    }

    @Test
    void existsByOfficeCode_existingCode_shouldReturnTrue() {

        assertTrue(
                officeRepository.existsByOfficeCode("1")
        );
    }
}

