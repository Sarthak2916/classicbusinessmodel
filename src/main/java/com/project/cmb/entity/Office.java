package com.project.cmb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "offices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "employees")
public class Office {

    @Id
    @Column(name = "officeCode", nullable = false, length = 10)
    @NotBlank(message = "Office code is required")
    @Size(max = 10, message = "Office code must not exceed 10 characters")
    private String officeCode;

    @Column(name = "city", nullable = false, length = 50)
    @NotBlank(message = "City is required")
    @Size(max = 50, message = "City must not exceed 50 characters")
    private String city;

    @Column(name = "phone", nullable = false, length = 50)
    @NotBlank(message = "Phone is required")
    @Size(max = 50, message = "Phone must not exceed 50 characters")
    @Pattern(
            regexp = "^[+]?[0-9\\s\\-().]{7,50}$",
            message = "Phone number format is invalid"
    )
    private String phone;

    @Column(name = "addressLine1", nullable = false, length = 50)
    @NotBlank(message = "Address Line 1 is required")
    @Size(max = 50, message = "Address Line 1 must not exceed 50 characters")
    private String addressLine1;

    @Column(name = "addressLine2", length = 50)
    @Size(max = 50, message = "Address Line 2 must not exceed 50 characters")
    private String addressLine2;

    @Column(name = "state", length = 50)
    @Size(max = 50, message = "State must not exceed 50 characters")
    private String state;

    @Column(name = "country", nullable = false, length = 50)
    @NotBlank(message = "Country is required")
    @Size(max = 50, message = "Country must not exceed 50 characters")
    private String country;

    @Column(name = "postalCode", nullable = false, length = 15)
    @NotBlank(message = "Postal code is required")
    @Size(max = 15, message = "Postal code must not exceed 15 characters")
    private String postalCode;

    @Column(name = "territory", nullable = false, length = 10)
    @NotBlank(message = "Territory is required")
    @Size(max = 10, message = "Territory must not exceed 10 characters")
    @Pattern(
            regexp = "^(NA|EMEA|APAC|Japan)$",
            message = "Territory must be one of: NA, EMEA, APAC, Japan"
    )
    private String territory;

    // Reverse side of Employee.office — no DB column, navigation only
    @OneToMany(mappedBy = "office", fetch = FetchType.LAZY)
    private List<Employee> employees;
}
