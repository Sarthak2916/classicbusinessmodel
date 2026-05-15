package com.project.cmb.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PaymentId implements Serializable {

    @Column(name = "customerNumber")
    private Integer customerNumber;

    @Column(name = "checkNumber")
    private String checkNumber;
}

