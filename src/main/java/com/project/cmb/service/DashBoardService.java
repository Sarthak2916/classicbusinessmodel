package com.project.cmb.service;

import com.project.cmb.repo.EmployeeRepo;
import com.project.cmb.repo.OrderRepo;
import com.project.cmb.repo.ProductRepo;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DashBoardService {

    private final EmployeeRepo employeeRepo;
    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;

    public long getEmployeesCount(){
        return employeeRepo.count();
    }

//    public long getCustomersCount(){
//        return 0;
//    }

    public long getOrdersCount(){
        return orderRepo.count();
    }

    public long getProductsCount(){
        return productRepo.count();
    }

//    public long getPaymentsCount(){
//        return 0;
//    }

//    public BigDecimal getTotalSalesAmount(){
//
//    }

    public Map<String, Long> getOrdersPerMonth(){

        Map<String, Long> ordersPerMonth = orderRepo.findAll()
                .stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().getMonth().name(),
                        Collectors.counting()
                ));

        return ordersPerMonth;
    }
}
