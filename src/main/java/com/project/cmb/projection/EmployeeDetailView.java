package com.project.cmb.projection;

import org.springframework.data.rest.core.config.Projection;
import com.project.cmb.entity.Employee;

@Projection(name = "employeeDetail", types = { Employee.class })
public interface EmployeeDetailView {

    Integer getEmployeeNumber();
    String getFirstName();
    String getLastName();
    String getEmail();
    String getExtension();
    String getJobTitle();

    // Full office details
    OfficeInfo getOffice();
    interface OfficeInfo {
        String getOfficeCode();
        String getCity();
        String getCountry();
        String getPhone();
        String getAddressLine1();
        String getTerritory();
    }

    // Manager — enough to identify and link to their profile
    ManagerInfo getReportsTo();
    interface ManagerInfo {
        Integer getEmployeeNumber();
        String getFirstName();
        String getLastName();
        String getJobTitle();
    }
}