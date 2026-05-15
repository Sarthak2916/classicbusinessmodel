//package com.project.cmb.repository;
//
//import com.project.cmb.entity.Employee;
//import com.project.cmb.entity.Office;
//import com.project.cmb.repo.EmployeeRepo;
//import com.project.cmb.repo.OfficeRepo;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class EmployeeRepoTest {
//
//    @Autowired
//    private EmployeeRepo employeeRepo;
//
//    @Autowired
//    private OfficeRepo officeRepo;
//
//    private Office office1;
//    private Office office2;
//
//    @BeforeEach
//    void setup() {
//        // Use existing offices from DB — officeCode "1" and "2" exist in classicmodels
//        office1 = officeRepo.findById("1").orElseThrow();
//        office2 = officeRepo.findById("2").orElseThrow();
//
//        Employee emp1 = new Employee();
//        emp1.setEmployeeNumber(9001);
//        emp1.setFirstName("Diane");
//        emp1.setLastName("Murphy");
//        emp1.setEmail("test.dmurphy@classicmodel.com");
//        emp1.setExtension("x5800");
//        emp1.setJobTitle("President");
//        emp1.setOffice(office1);
//        emp1.setReportsTo(null);
//        employeeRepo.save(emp1);
//
//        Employee emp2 = new Employee();
//        emp2.setEmployeeNumber(9002);
//        emp2.setFirstName("Mary");
//        emp2.setLastName("Patterson");
//        emp2.setEmail("test.mpatterson@classicmodel.com");
//        emp2.setExtension("x4611");
//        emp2.setJobTitle("VP Sales");
//        emp2.setOffice(office1);
//        emp2.setReportsTo(emp1);
//        employeeRepo.save(emp2);
//
//        Employee emp3 = new Employee();
//        emp3.setEmployeeNumber(9003);
//        emp3.setFirstName("Jeff");
//        emp3.setLastName("Firrelli");
//        emp3.setEmail("test.jfirrelli@classicmodel.com");
//        emp3.setExtension("x9273");
//        emp3.setJobTitle("Sales Rep");
//        emp3.setOffice(office2);
//        emp3.setReportsTo(emp2);
//        employeeRepo.save(emp3);
//    }
//
//    @Test
//    void repo_findAll_shouldReturnAllEmployees() {
//        Page<Employee> result = employeeRepo.findAll(PageRequest.of(0, 5));
//        assertThat(result.getTotalElements()).isGreaterThan(0);
//    }
//
//    @Test
//    void repo_findAll_shouldRespectPageSize() {
//        Page<Employee> result = employeeRepo.findAll(PageRequest.of(0, 2));
//        assertThat(result.getContent()).hasSize(2);
//        assertThat(result.getTotalPages()).isGreaterThan(1);
//    }
//
//    @Test
//    void repo_findById_shouldReturnEmployee() {
//        Optional<Employee> result = employeeRepo.findById(9001);
//        assertThat(result).isPresent();
//        assertThat(result.get().getFirstName()).isEqualTo("Diane");
//        assertThat(result.get().getJobTitle()).isEqualTo("President");
//    }
//
//    @Test
//    void repo_findById_whenNotExists_shouldReturnEmpty() {
//        Optional<Employee> result = employeeRepo.findById(99999);
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    void repo_save_shouldPersistNewEmployee() {
//        Employee emp = new Employee();
//        emp.setEmployeeNumber(9004);
//        emp.setFirstName("John");
//        emp.setLastName("Smith");
//        emp.setEmail("test.jsmith@classicmodel.com");
//        emp.setExtension("x1234");
//        emp.setJobTitle("Sales Rep");
//        emp.setOffice(office1);
//        emp.setReportsTo(employeeRepo.findById(9001).orElseThrow());
//        employeeRepo.save(emp);
//
//        Optional<Employee> saved = employeeRepo.findById(9004);
//        assertThat(saved).isPresent();
//        assertThat(saved.get().getFirstName()).isEqualTo("John");
//    }
//
//    @Test
//    void repo_save_shouldIncreaseCount() {
//        long countBefore = employeeRepo.count();
//
//        Employee emp = new Employee();
//        emp.setEmployeeNumber(9005);
//        emp.setFirstName("Jane");
//        emp.setLastName("Doe");
//        emp.setEmail("test.jdoe@classicmodel.com");
//        emp.setExtension("x5678");
//        emp.setJobTitle("Sales Rep");
//        emp.setOffice(office2);
//        emp.setReportsTo(employeeRepo.findById(9002).orElseThrow());
//        employeeRepo.save(emp);
//
//        assertThat(employeeRepo.count()).isEqualTo(countBefore + 1);
//    }
//
//
//    @Test
//    void repo_update_shouldModifyExistingEmployee() {
//        Employee emp = employeeRepo.findById(9001).orElseThrow();
//        emp.setJobTitle("CEO");
//        employeeRepo.save(emp);
//
//        Employee updated = employeeRepo.findById(9001).orElseThrow();
//        assertThat(updated.getJobTitle()).isEqualTo("CEO");
//    }
//
//    @Test
//    void repo_update_shouldNotChangeOtherFields() {
//        Employee emp = employeeRepo.findById(9001).orElseThrow();
//        emp.setExtension("x9999");
//        employeeRepo.save(emp);
//
//        Employee updated = employeeRepo.findById(9001).orElseThrow();
//        assertThat(updated.getFirstName()).isEqualTo("Diane");
//        assertThat(updated.getLastName()).isEqualTo("Murphy");
//    }
//
//    @Test
//    void repo_deleteById_shouldRemoveEmployee() {
//        employeeRepo.deleteById(9003);
//        assertThat(employeeRepo.findById(9003)).isEmpty();
//    }
//
//    @Test
//    void repo_deleteById_shouldDecreaseCount() {
//        long countBefore = employeeRepo.count();
//        employeeRepo.deleteById(9003);
//        assertThat(employeeRepo.count()).isEqualTo(countBefore - 1);
//    }
//
//
//    @Test
//    void repo_findByName_shouldReturnMatchingEmployee() {
//        Page<Employee> result = employeeRepo
//                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//                        "mary", "mary", PageRequest.of(0, 5));
//        assertThat(result.getContent().stream()
//                .anyMatch(e -> e.getEmployeeNumber() == 9002)).isTrue();
//    }
//
//    @Test
//    void repo_findByName_caseInsensitive_shouldWork() {
//        Page<Employee> result = employeeRepo
//                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//                        "DIANE", "DIANE", PageRequest.of(0, 5));
//        assertThat(result.getContent().stream()
//                .anyMatch(e -> e.getFirstName().equals("Diane"))).isTrue();
//    }
//
//    @Test
//    void repo_findByName_partialName_shouldWork() {
//        Page<Employee> result = employeeRepo
//                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//                        "pat", "pat", PageRequest.of(0, 5));
//        assertThat(result.getContent().stream()
//                .anyMatch(e -> e.getLastName().equals("Patterson"))).isTrue();
//    }
//
//    @Test
//    void repo_findByName_noMatch_shouldReturnEmpty() {
//        Page<Employee> result = employeeRepo
//                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
//                        "xyzxyzxyz", "xyzxyzxyz", PageRequest.of(0, 5));
//        assertThat(result.getTotalElements()).isEqualTo(0);
//    }
//
//
//    @Test
//    void repo_findByOfficeCode_shouldReturnCorrectEmployees() {
//        Page<Employee> result = employeeRepo.findByOffice_OfficeCode(
//                "2", PageRequest.of(0, 5));
//        assertThat(result.getContent().stream()
//                .anyMatch(e -> e.getEmployeeNumber() == 9003)).isTrue();
//    }
//
//    @Test
//    void repo_findByOfficeCode_wrongCode_shouldReturnEmpty() {
//        Page<Employee> result = employeeRepo.findByOffice_OfficeCode(
//                "999", PageRequest.of(0, 5));
//        assertThat(result.getTotalElements()).isEqualTo(0);
//    }
//
//    @Test
//    void repo_findByOfficeCode_shouldReturnOnlyThatOffice() {
//        Page<Employee> result = employeeRepo.findByOffice_OfficeCode(
//                "2", PageRequest.of(0, 5));
//        assertThat(result.getContent().stream()
//                .allMatch(e -> e.getOffice().getOfficeCode().equals("2"))).isTrue();
//    }
//
//    @Test
//    void repo_findByReportsTo_shouldReturnDirectReportees() {
//        List<Employee> result = employeeRepo.findByReportsTo_EmployeeNumber(9001);
//        assertThat(result.stream()
//                .anyMatch(e -> e.getEmployeeNumber() == 9002)).isTrue();
//    }
//
//    @Test
//    void repo_findByReportsTo_shouldReturnCorrectReportee() {
//        List<Employee> result = employeeRepo.findByReportsTo_EmployeeNumber(9002);
//        assertThat(result.stream()
//                .anyMatch(e -> e.getEmployeeNumber() == 9003)).isTrue();
//    }
//
//    @Test
//    void repo_findByReportsTo_noReportees_shouldReturnEmpty() {
//        List<Employee> result = employeeRepo.findByReportsTo_EmployeeNumber(99999);
//        assertThat(result).isEmpty();
//    }
//}

package com.project.cmb.repository;

import com.project.cmb.entity.Employee;
import com.project.cmb.entity.Office;
import com.project.cmb.repo.EmployeeRepo;
import com.project.cmb.repo.OfficeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EmployeeRepoTest {

    @Autowired private EmployeeRepo employeeRepo;
    @Autowired private OfficeRepo officeRepo;

    private Office office1;
    private Office office2;

    @BeforeEach
    void setup() {
        office1 = officeRepo.findById("1").orElseThrow();
        office2 = officeRepo.findById("2").orElseThrow();

        Employee emp1 = new Employee();
        emp1.setEmployeeNumber(9001);
        emp1.setFirstName("Diane");
        emp1.setLastName("Murphy");
        emp1.setEmail("test.dmurphy@classicmodel.com");
        emp1.setExtension("x5800");
        emp1.setJobTitle("President");
        emp1.setOffice(office1);
        emp1.setReportsTo(null);
        employeeRepo.save(emp1);

        Employee emp2 = new Employee();
        emp2.setEmployeeNumber(9002);
        emp2.setFirstName("Mary");
        emp2.setLastName("Patterson");
        emp2.setEmail("test.mpatterson@classicmodel.com");
        emp2.setExtension("x4611");
        emp2.setJobTitle("VP Sales");
        emp2.setOffice(office1);
        emp2.setReportsTo(emp1);
        employeeRepo.save(emp2);

        Employee emp3 = new Employee();
        emp3.setEmployeeNumber(9003);
        emp3.setFirstName("Jeff");
        emp3.setLastName("Firrelli");
        emp3.setEmail("test.jfirrelli@classicmodel.com");
        emp3.setExtension("x9273");
        emp3.setJobTitle("Sales Rep");
        emp3.setOffice(office2);
        emp3.setReportsTo(emp2);
        employeeRepo.save(emp3);
    }

    // --- findAll ---

    @Test
    void repo_findAll_shouldReturnAllEmployees() {
        Page<Employee> result = employeeRepo.findAll(PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isGreaterThan(0);
    }

    @Test
    void repo_findAll_shouldRespectPageSize() {
        Page<Employee> result = employeeRepo.findAll(PageRequest.of(0, 2));
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalPages()).isGreaterThan(1);
    }

    // --- findById ---

    @Test
    void repo_findById_shouldReturnEmployee() {
        Optional<Employee> result = employeeRepo.findById(9001);
        assertThat(result).isPresent();
        assertThat(result.get().getFirstName()).isEqualTo("Diane");
        assertThat(result.get().getJobTitle()).isEqualTo("President");
    }

    @Test
    void repo_findById_whenNotExists_shouldReturnEmpty() {
        assertThat(employeeRepo.findById(99999)).isEmpty();
    }

    // --- save ---

    @Test
    void repo_save_shouldPersistNewEmployee() {
        Employee emp = new Employee();
        emp.setEmployeeNumber(9004);
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setEmail("test.jsmith@classicmodel.com");
        emp.setExtension("x1234");
        emp.setJobTitle("Sales Rep");
        emp.setOffice(office1);
        emp.setReportsTo(employeeRepo.findById(9001).orElseThrow());
        employeeRepo.save(emp);

        Optional<Employee> saved = employeeRepo.findById(9004);
        assertThat(saved).isPresent();
        assertThat(saved.get().getFirstName()).isEqualTo("John");
    }

    @Test
    void repo_save_shouldIncreaseCount() {
        long countBefore = employeeRepo.count();
        Employee emp = new Employee();
        emp.setEmployeeNumber(9005);
        emp.setFirstName("Jane");
        emp.setLastName("Doe");
        emp.setEmail("test.jdoe@classicmodel.com");
        emp.setExtension("x5678");
        emp.setJobTitle("Sales Rep");
        emp.setOffice(office2);
        emp.setReportsTo(employeeRepo.findById(9002).orElseThrow());
        employeeRepo.save(emp);
        assertThat(employeeRepo.count()).isEqualTo(countBefore + 1);
    }

    // --- update ---

    @Test
    void repo_update_shouldModifyJobTitle() {
        Employee emp = employeeRepo.findById(9001).orElseThrow();
        emp.setJobTitle("CEO");
        employeeRepo.save(emp);
        assertThat(employeeRepo.findById(9001).orElseThrow().getJobTitle()).isEqualTo("CEO");
    }

    @Test
    void repo_update_shouldNotChangeOtherFields() {
        Employee emp = employeeRepo.findById(9001).orElseThrow();
        emp.setExtension("x9999");
        employeeRepo.save(emp);
        Employee updated = employeeRepo.findById(9001).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo("Diane");
        assertThat(updated.getLastName()).isEqualTo("Murphy");
    }

    // --- delete ---

    @Test
    void repo_deleteById_shouldRemoveEmployee() {
        employeeRepo.deleteById(9003);
        assertThat(employeeRepo.findById(9003)).isEmpty();
    }

    @Test
    void repo_deleteById_shouldDecreaseCount() {
        long countBefore = employeeRepo.count();
        employeeRepo.deleteById(9003);
        assertThat(employeeRepo.count()).isEqualTo(countBefore - 1);
    }

    // --- findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase ---

    @Test
    void repo_findByName_shouldReturnMatch() {
        Page<Employee> result = employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        "mary", "mary", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9002)).isTrue();
    }

    @Test
    void repo_findByName_caseInsensitive_shouldWork() {
        Page<Employee> result = employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        "DIANE", "DIANE", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getFirstName().equals("Diane"))).isTrue();
    }

    @Test
    void repo_findByName_partialName_shouldWork() {
        Page<Employee> result = employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        "pat", "pat", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getLastName().equals("Patterson"))).isTrue();
    }

    @Test
    void repo_findByName_noMatch_shouldReturnEmpty() {
        Page<Employee> result = employeeRepo
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        "xyzxyzxyz", "xyzxyzxyz", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    // --- findByOffice_OfficeCode ---

    @Test
    void repo_findByOfficeCode_shouldReturnCorrectEmployees() {
        Page<Employee> result = employeeRepo.findByOffice_OfficeCode("2", PageRequest.of(0, 5));
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9003)).isTrue();
    }

    @Test
    void repo_findByOfficeCode_wrongCode_shouldReturnEmpty() {
        Page<Employee> result = employeeRepo.findByOffice_OfficeCode("999", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    void repo_findByOfficeCode_shouldReturnOnlyThatOffice() {
        Page<Employee> result = employeeRepo.findByOffice_OfficeCode("2", PageRequest.of(0, 10));
        assertThat(result.getContent().stream()
                .allMatch(e -> e.getOffice().getOfficeCode().equals("2"))).isTrue();
    }

    // --- findByReportsTo_EmployeeNumber ---

    @Test
    void repo_findByReportsTo_shouldReturnDirectReportees() {
        List<Employee> result = employeeRepo.findByReportsTo_EmployeeNumber(9001);
        assertThat(result.stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9002)).isTrue();
    }

    @Test
    void repo_findByReportsTo_shouldReturnCorrectReportee() {
        List<Employee> result = employeeRepo.findByReportsTo_EmployeeNumber(9002);
        assertThat(result.stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9003)).isTrue();
    }

    @Test
    void repo_findByReportsTo_noReportees_shouldReturnEmpty() {
        List<Employee> result = employeeRepo.findByReportsTo_EmployeeNumber(99999);
        assertThat(result).isEmpty();
    }

    // --- findByOffice_CityContainingIgnoreCase ---

    @Test
    void repo_findByCity_shouldReturnEmployeesInThatCity() {
        // office1 and office2 are real DB offices — get their cities
        String city = office1.getCity();
        Page<Employee> result = employeeRepo.findByOffice_CityContainingIgnoreCase(
                city.substring(0, 3), PageRequest.of(0, 50));
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9001)).isTrue();
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9002)).isTrue();
    }

    @Test
    void repo_findByCity_caseInsensitive_shouldWork() {
        String city = office2.getCity().toUpperCase();
        Page<Employee> result = employeeRepo.findByOffice_CityContainingIgnoreCase(
                city.substring(0, 3), PageRequest.of(0, 50));
        assertThat(result.getContent().stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9003)).isTrue();
    }

    @Test
    void repo_findByCity_noMatch_shouldReturnEmpty() {
        Page<Employee> result = employeeRepo.findByOffice_CityContainingIgnoreCase(
                "xyzxyzxyz", PageRequest.of(0, 5));
        assertThat(result.getTotalElements()).isEqualTo(0);
    }

    // --- findByReportsToIsNull ---

    @Test
    void repo_findByReportsToIsNull_shouldReturnTopLevelEmployees() {
        List<Employee> result = employeeRepo.findByReportsToIsNull();
        assertThat(result.stream()
                .anyMatch(e -> e.getEmployeeNumber() == 9001)).isTrue();
    }

    @Test
    void repo_findByReportsToIsNull_shouldNotReturnEmployeesWithManager() {
        List<Employee> result = employeeRepo.findByReportsToIsNull();
        assertThat(result.stream()
                .noneMatch(e -> e.getEmployeeNumber() == 9002)).isTrue();
        assertThat(result.stream()
                .noneMatch(e -> e.getEmployeeNumber() == 9003)).isTrue();
    }
}