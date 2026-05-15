package com.project.cmb.repo;

import com.project.cmb.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource(path = "employees")
public interface EmployeeRepo extends JpaRepository<Employee, Integer> {
    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName, Pageable pageable);
    Page<Employee> findByOffice_OfficeCode(String officeCode, Pageable pageable);
    Page<Employee> findByOffice_CityContainingIgnoreCase(String city, Pageable pageable);
    List<Employee> findByReportsTo_EmployeeNumber(Integer employeeNumber);
    List<Employee> findByReportsToIsNull();
}

