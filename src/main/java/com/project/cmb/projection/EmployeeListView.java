package com.project.cmb.projection;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import com.project.cmb.entity.Employee;

@Projection(name = "employeeList", types = { Employee.class })
public interface EmployeeListView {

    Integer getEmployeeNumber();

    // Full name combined on frontend from these two
    String getFirstName();
    String getLastName();

    String getJobTitle();

    // Office info — officeCode and city only
    OfficeInfo getOffice();
    interface OfficeInfo {
        String getOfficeCode();
        String getCity();
    }

    // Manager info — just enough to show "reports to"
    ManagerInfo getReportsTo();
    interface ManagerInfo {
        Integer getEmployeeNumber();
        String getFirstName();
        String getLastName();
    }
}