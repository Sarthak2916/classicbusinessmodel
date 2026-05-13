package com.project.cmb.service;

import com.project.cmb.repo.EmployeeRepo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DashBoardService {

    private final EmployeeRepo employeeRepo;

    public long getEmployeeCount(){
        return employeeRepo.count();
    }
}
