package com.project.cmb.repository;

import com.project.cmb.entity.Employee;
import com.project.cmb.repo.EmployeeRepo;
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

    @Autowired
    private EmployeeRepo employeeRepository;

    @BeforeEach
    void setup() {

        Employee emp1 = new Employee();
        emp1.setEmployeeNumber(9001);
        emp1.setFirstName("Diane");
        emp1.setLastName("Murphy");
        emp1.setEmail("test.dmurphy@classicmodel.com");
        emp1.setExtension("x5800");
        emp1.setJobTitle("President");
        emp1.setOfficeCode("1");
        emp1.setReportsTo(null);

        Employee emp2 = new Employee();
        emp2.setEmployeeNumber(9002);
        emp2.setFirstName("Mary");
        emp2.setLastName("Patterson");
        emp2.setEmail("test.mpatterson@classicmodel.com");
        emp2.setExtension("x4611");
        emp2.setJobTitle("VP Sales");
        emp2.setOfficeCode("1");
        emp2.setReportsTo(9001);

        Employee emp3 = new Employee();
        emp3.setEmployeeNumber(9003);
        emp3.setFirstName("Jeff");
        emp3.setLastName("Firrelli");
        emp3.setEmail("test.jfirrelli@classicmodel.com");
        emp3.setExtension("x9273");
        emp3.setJobTitle("Sales Rep");
        emp3.setOfficeCode("2");
        emp3.setReportsTo(9002);

        employeeRepository.save(emp1);
        employeeRepository.save(emp2);
        employeeRepository.save(emp3);
    }


    @Test
    void repo_findAll_shouldReturnAllEmployees() {

        Page<Employee> result =
                employeeRepository.findAll(
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isGreaterThan(0);

        assertThat(result.getSize())
                .isEqualTo(5);
    }

    @Test
    void repo_findAll_shouldRespectPageSize() {

        Page<Employee> result =
                employeeRepository.findAll(
                        PageRequest.of(0, 2)
                );

        assertThat(result.getContent())
                .hasSize(2);

        assertThat(result.getTotalPages())
                .isGreaterThan(1);
    }

    @Test
    void repo_findById_shouldReturnEmployee() {

        Optional<Employee> result =
                employeeRepository.findById(9001);

        assertThat(result).isPresent();

        assertThat(result.get().getFirstName())
                .isEqualTo("Diane");

        assertThat(result.get().getJobTitle())
                .isEqualTo("President");
    }

    @Test
    void repo_findById_whenNotExists_shouldReturnEmpty() {

        Optional<Employee> result =
                employeeRepository.findById(99999);

        assertThat(result).isEmpty();
    }


    @Test
    void repo_save_shouldPersistNewEmployee() {

        Employee emp = new Employee();

        emp.setEmployeeNumber(9004);
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setEmail("test.jsmith@classicmodel.com");
        emp.setExtension("x1234");
        emp.setJobTitle("Sales Rep");
        emp.setOfficeCode("1");
        emp.setReportsTo(9001);

        employeeRepository.save(emp);

        Optional<Employee> saved =
                employeeRepository.findById(9004);

        assertThat(saved).isPresent();

        assertThat(saved.get().getFirstName())
                .isEqualTo("John");
    }

    @Test
    void repo_save_shouldIncreaseCount() {

        long countBefore = employeeRepository.count();

        Employee emp = new Employee();

        emp.setEmployeeNumber(9005);
        emp.setFirstName("Jane");
        emp.setLastName("Doe");
        emp.setEmail("test.jdoe@classicmodel.com");
        emp.setExtension("x5678");
        emp.setJobTitle("Sales Rep");
        emp.setOfficeCode("2");
        emp.setReportsTo(9002);

        employeeRepository.save(emp);

        assertThat(employeeRepository.count())
                .isEqualTo(countBefore + 1);
    }


    @Test
    void repo_update_shouldModifyExistingEmployee() {

        Employee emp =
                employeeRepository.findById(9001).get();

        emp.setJobTitle("CEO");

        employeeRepository.save(emp);

        Employee updated =
                employeeRepository.findById(9001).get();

        assertThat(updated.getJobTitle())
                .isEqualTo("CEO");
    }

    @Test
    void repo_update_shouldNotChangeOtherFields() {

        Employee emp =
                employeeRepository.findById(9001).get();

        emp.setExtension("x9999");

        employeeRepository.save(emp);

        Employee updated =
                employeeRepository.findById(9001).get();

        assertThat(updated.getFirstName())
                .isEqualTo("Diane");

        assertThat(updated.getLastName())
                .isEqualTo("Murphy");
    }

    @Test
    void repo_deleteById_shouldRemoveEmployee() {

        employeeRepository.deleteById(9003);

        Optional<Employee> result =
                employeeRepository.findById(9003);

        assertThat(result).isEmpty();
    }

    @Test
    void repo_deleteById_shouldDecreaseCount() {

        long countBefore = employeeRepository.count();

        employeeRepository.deleteById(9003);

        assertThat(employeeRepository.count())
                .isEqualTo(countBefore - 1);
    }



    @Test
    void repo_findByName_shouldReturnMatchingEmployee() {

        Page<Employee> result =
                employeeRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                "mary",
                                "mary",
                                PageRequest.of(0, 5)
                        );

        assertThat(result.getTotalElements())
                .isGreaterThan(0);

        boolean found = result.getContent().stream()
                .anyMatch(e ->
                        e.getFirstName().equals("Mary")
                                && e.getEmployeeNumber() == 9002
                );

        assertThat(found).isTrue();
    }

    @Test
    void repo_findByName_caseInsensitive_shouldWork() {

        Page<Employee> result =
                employeeRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                "DIANE",
                                "DIANE",
                                PageRequest.of(0, 5)
                        );

        boolean found = result.getContent().stream()
                .anyMatch(e ->
                        e.getFirstName().equals("Diane")
                                && e.getEmployeeNumber() == 9001
                );

        assertThat(found).isTrue();
    }

    @Test
    void repo_findByName_partialName_shouldWork() {

        Page<Employee> result =
                employeeRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                "pat",
                                "pat",
                                PageRequest.of(0, 5)
                        );

        boolean found = result.getContent().stream()
                .anyMatch(e ->
                        e.getLastName().equals("Patterson")
                                && e.getEmployeeNumber() == 9002
                );

        assertThat(found).isTrue();
    }

    @Test
    void repo_findByName_noMatch_shouldReturnEmpty() {

        Page<Employee> result =
                employeeRepository
                        .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                                "xyzxyzxyz",
                                "xyzxyzxyz",
                                PageRequest.of(0, 5)
                        );

        assertThat(result.getTotalElements())
                .isEqualTo(0);
    }


    @Test
    void repo_findByOfficeCode_shouldReturnCorrectEmployees() {

        Page<Employee> result =
                employeeRepository.findByOfficeCode(
                        "2",
                        PageRequest.of(0, 5)
                );

        boolean found = result.getContent().stream()
                .anyMatch(e ->
                        e.getEmployeeNumber() == 9003
                );

        assertThat(found).isTrue();
    }

    @Test
    void repo_findByOfficeCode_wrongCode_shouldReturnEmpty() {

        Page<Employee> result =
                employeeRepository.findByOfficeCode(
                        "999",
                        PageRequest.of(0, 5)
                );

        assertThat(result.getTotalElements())
                .isEqualTo(0);
    }

    @Test
    void repo_findByOfficeCode_shouldReturnOnlyThatOffice() {

        Page<Employee> result =
                employeeRepository.findByOfficeCode(
                        "2",
                        PageRequest.of(0, 5)
                );

        boolean allOffice2 = result.getContent().stream()
                .allMatch(e ->
                        e.getOfficeCode().equals("2")
                );

        assertThat(allOffice2).isTrue();
    }


    @Test
    void repo_findByReportsTo_shouldReturnDirectReports() {

        List<Employee> result =
                employeeRepository.findByReportsTo(9001);

        boolean found = result.stream()
                .anyMatch(e ->
                        e.getEmployeeNumber() == 9002
                );

        assertThat(found).isTrue();
    }

    @Test
    void repo_findByReportsTo_shouldReturnCorrectReport() {

        List<Employee> result =
                employeeRepository.findByReportsTo(9002);

        boolean found = result.stream()
                .anyMatch(e ->
                        e.getEmployeeNumber() == 9003
                );

        assertThat(found).isTrue();
    }

    @Test
    void repo_findByReportsTo_topLevel_shouldReturnEmpty() {

        List<Employee> result =
                employeeRepository.findByReportsTo(99999);

        assertThat(result).isEmpty();
    }
}