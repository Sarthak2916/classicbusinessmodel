package com.project.cmb.controller;

import com.project.cmb.projection.CustomerListView;
import com.project.cmb.projection.EmployeeListView;
import com.project.cmb.repo.CustomerRepo;
import com.project.cmb.repo.EmployeeRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean EmployeeRepo employeeRepo;
    @MockBean CustomerRepo customerRepo;

    // --- concrete implementations for projections ---

    private EmployeeListView buildEmployeeListView(
            Integer number, String firstName, String lastName,
            String jobTitle, String officeCode, String city) {
        return new EmployeeListView() {
            public Integer getEmployeeNumber() { return number; }
            public String getFirstName() { return firstName; }
            public String getLastName() { return lastName; }
            public String getJobTitle() { return jobTitle; }
            public OfficeInfo getOffice() {
                return new OfficeInfo() {
                    public String getOfficeCode() { return officeCode; }
                    public String getCity() { return city; }
                };
            }
            public ManagerInfo getReportsTo() { return null; }
        };
    }

    private CustomerListView buildCustomerListView(
            Integer number, String name, String city, String country) {
        return new CustomerListView() {
            public Integer getCustomerNumber() { return number; }
            public String getContactFirstName() { return "John"; }
            public String getContactLastName() { return "Doe"; }
            public String getCity() { return city; }
            public String getCountry() { return country; }
            public BigDecimal getCreditLimit() { return new BigDecimal("10000.00"); }
        };
    }

    // --- GET /api/v1/employees/search?name=diane ---

    @Test
    void searchByName_shouldReturn200AndPage() throws Exception {
        EmployeeListView emp = buildEmployeeListView(
                1002, "Diane", "Murphy", "President", "1", "San Francisco");

        when(employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        eq("diane"), eq("diane"), any()))
                .thenReturn(new PageImpl<>(List.of(emp), PageRequest.of(0, 5), 1));

        mockMvc.perform(get("/api/v1/employees/search")
                        .param("name", "diane"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].employeeNumber").value(1002))
                .andExpect(jsonPath("$.content[0].firstName").value("Diane"))
                .andExpect(jsonPath("$.content[0].lastName").value("Murphy"))
                .andExpect(jsonPath("$.content[0].jobTitle").value("President"))
                .andExpect(jsonPath("$.content[0].office.city").value("San Francisco"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void searchByName_noMatch_shouldReturnEmptyPage() throws Exception {
        when(employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        eq("xyzxyz"), eq("xyzxyz"), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/employees/search")
                        .param("name", "xyzxyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // --- GET /api/v1/employees/search/city?city=san ---

    @Test
    void searchByCity_shouldReturn200AndPage() throws Exception {
        EmployeeListView emp = buildEmployeeListView(
                1002, "Diane", "Murphy", "President", "1", "San Francisco");

        when(employeeRepo.findByOffice_CityContainingIgnoreCase(eq("san"), any()))
                .thenReturn(new PageImpl<>(List.of(emp), PageRequest.of(0, 5), 1));

        mockMvc.perform(get("/api/v1/employees/search/city")
                        .param("city", "san"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].office.city").value("San Francisco"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void searchByCity_noMatch_shouldReturnEmptyPage() throws Exception {
        when(employeeRepo.findByOffice_CityContainingIgnoreCase(eq("xyzxyz"), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/employees/search/city")
                        .param("city", "xyzxyz"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // --- GET /api/v1/employees/office/{officeCode} ---

    @Test
    void getByOffice_shouldReturn200AndPage() throws Exception {
        EmployeeListView emp1 = buildEmployeeListView(
                1002, "Diane", "Murphy", "President", "1", "San Francisco");
        EmployeeListView emp2 = buildEmployeeListView(
                1056, "Mary", "Patterson", "VP Sales", "1", "San Francisco");

        when(employeeRepo.findByOffice_OfficeCode(eq("1"), any()))
                .thenReturn(new PageImpl<>(List.of(emp1, emp2), PageRequest.of(0, 5), 2));

        mockMvc.perform(get("/api/v1/employees/office/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].office.officeCode").value("1"))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void getByOffice_invalidCode_shouldReturnEmptyPage() throws Exception {
        when(employeeRepo.findByOffice_OfficeCode(eq("999"), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/employees/office/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // --- GET /api/v1/employees/{employeeNumber}/reportees ---

    @Test
    void getReportees_shouldReturn200AndList() throws Exception {
        EmployeeListView reportee = buildEmployeeListView(
                1056, "Mary", "Patterson", "VP Sales", "1", "San Francisco");

        when(employeeRepo.findByReportsTo_EmployeeNumber(1002))
                .thenReturn(List.of(reportee));

        mockMvc.perform(get("/api/v1/employees/1002/reportees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].employeeNumber").value(1056))
                .andExpect(jsonPath("$[0].firstName").value("Mary"));
    }

    @Test
    void getReportees_noReportees_shouldReturnEmptyList() throws Exception {
        when(employeeRepo.findByReportsTo_EmployeeNumber(9999))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/employees/9999/reportees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    // --- GET /api/v1/employees/{employeeNumber}/customers ---

    @Test
    void getCustomers_shouldReturn200AndPage() throws Exception {
        CustomerListView customer = buildCustomerListView(
                103, "Atelier graphique", "Nantes", "France");

        when(customerRepo.findBySalesRepEmployee_EmployeeNumber(eq(1370), any()))
                .thenReturn(new PageImpl<>(List.of(customer), PageRequest.of(0, 5), 1));

        mockMvc.perform(get("/api/v1/employees/1370/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].customerNumber").value(103))
                .andExpect(jsonPath("$.content[0].city").value("Nantes"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getCustomers_noCustomers_shouldReturnEmptyPage() throws Exception {
        when(customerRepo.findBySalesRepEmployee_EmployeeNumber(eq(9999), any()))
                .thenReturn(new PageImpl<>(List.of()));

        mockMvc.perform(get("/api/v1/employees/9999/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    // --- GET /api/v1/employees/top-level ---

    @Test
    void getTopLevelEmployees_shouldReturn200AndList() throws Exception {
        EmployeeListView president = buildEmployeeListView(
                1002, "Diane", "Murphy", "President", "1", "San Francisco");

        when(employeeRepo.findByReportsToIsNull())
                .thenReturn(List.of(president));

        mockMvc.perform(get("/api/v1/employees/top-level"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].employeeNumber").value(1002))
                .andExpect(jsonPath("$[0].jobTitle").value("President"));
    }

    @Test
    void getTopLevelEmployees_emptyList_shouldReturn200() throws Exception {
        when(employeeRepo.findByReportsToIsNull()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/employees/top-level"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
}