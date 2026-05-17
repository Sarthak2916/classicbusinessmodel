//package com.project.cmb.controller;
//
//import com.project.cmb.entity.Customer;
//import com.project.cmb.entity.Employee;
//import com.project.cmb.repo.EmployeeRepo;
//import lombok.AllArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/employees")
//@AllArgsConstructor
//public class EmployeeController {
//
//    private final EmployeeRepo employeeRepo;
//
//    // GET /api/v1/employees/search?name=john&page=0&size=10
//    // Search by first or last name
//    @GetMapping("/search")
//    public ResponseEntity<Page<Employee>> searchByName(
//            @RequestParam String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(
//                employeeRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//                        name, name, pageable));
//    }
//
//    // GET /api/v1/employees/search/city?city=london&page=0&size=10
//    // Search by office city
//    @GetMapping("/search/city")
//    public ResponseEntity<Page<Employee>> searchByCity(
//            @RequestParam String city,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(
//                employeeRepo.findByOffice_CityContainingIgnoreCase(city, pageable));
//    }
//
//    // GET /api/v1/employees/office/{officeCode}?page=0&size=10
//    // Filter employees by office
//    @GetMapping("/office/{officeCode}")
//    public ResponseEntity<Page<Employee>> getByOffice(
//            @PathVariable String officeCode,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        Pageable pageable = PageRequest.of(page, size);
//        return ResponseEntity.ok(
//                employeeRepo.findByOffice_OfficeCode(officeCode, pageable));
//    }
//
//    // GET /api/v1/employees/{employeeNumber}/reportees
//    // Get all employees who report to this employee
//    @GetMapping("/{employeeNumber}/reportees")
//    public ResponseEntity<List<Employee>> getReportees(
//            @PathVariable Integer employeeNumber) {
//        return ResponseEntity.ok(
//                employeeRepo.findByReportsTo_EmployeeNumber(employeeNumber));
//    }
//
//    // GET /api/v1/employees/{employeeNumber}/customers
//    // Get all customers assigned to this employee as sales rep
//    @GetMapping("/{employeeNumber}/customers")
//    public ResponseEntity<List<Customer>> getCustomers(
//            @PathVariable Integer employeeNumber) {
//        return ResponseEntity.ok(
//                employeeRepo.findById(employeeNumber)
//                        .map(Employee::getCustomers)
//                        .orElse(List.of()));
//    }
//
//    // GET /api/v1/employees/top-level
//    // Get all top level employees (no manager)
//    @GetMapping("/top-level")
//    public ResponseEntity<List<Employee>> getTopLevelEmployees() {
//        return ResponseEntity.ok(employeeRepo.findByReportsToIsNull());
//    }
//}

package com.project.cmb.controller;

//import com.project.cmb.projection.CustomerListView;
import com.project.cmb.projection.CustomerListView;
import com.project.cmb.projection.EmployeeListView;
import com.project.cmb.repo.CustomerRepo;
import com.project.cmb.repo.EmployeeRepo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeRepo employeeRepo;
    private final CustomerRepo customerRepo;

    // GET /api/v1/employees/search?name=john&page=0&size=5
    // Search by first or last name
    @GetMapping("/search")
    public ResponseEntity<Page<EmployeeListView>> searchByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                employeeRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        name, name, pageable));
    }

    // GET /api/v1/employees/search/city?city=london&page=0&size=5
    // Search by office city
    @GetMapping("/search/city")
    public ResponseEntity<Page<EmployeeListView>> searchByCity(
            @RequestParam String city,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                employeeRepo.findByOffice_CityContainingIgnoreCase(city, pageable));
    }

    // GET /api/v1/employees/office/{officeCode}?page=0&size=5
    // Filter employees by office
    @GetMapping("/office/{officeCode}")
    public ResponseEntity<Page<EmployeeListView>> getByOffice(
            @PathVariable String officeCode,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                employeeRepo.findByOffice_OfficeCode(officeCode, pageable));
    }

    // GET /api/v1/employees/{employeeNumber}/reportees
    // Get all employees who report to this employee
    @GetMapping("/{employeeNumber}/reportees")
    public ResponseEntity<List<EmployeeListView>> getReportees(
            @PathVariable Integer employeeNumber) {
        return ResponseEntity.ok(
                employeeRepo.findByReportsTo_EmployeeNumber(employeeNumber));
    }

    // GET /api/v1/employees/{employeeNumber}/customers?page=0&size=5
//    // Get all customers assigned to this employee as sales rep — paginated
    @GetMapping("/{employeeNumber}/customers")
    public ResponseEntity<Page<CustomerListView>> getCustomers(
            @PathVariable Integer employeeNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(
                customerRepo.findBySalesRepEmployee_EmployeeNumber(
                        employeeNumber, pageable));
    }

    // GET /api/v1/employees/top-level
    // Get all top level employees (no manager) — org chart roots
    @GetMapping("/top-level")
    public ResponseEntity<List<EmployeeListView>> getTopLevelEmployees() {
        return ResponseEntity.ok(employeeRepo.findByReportsToIsNull());
    }
}