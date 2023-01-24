package com.spring.batch.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SALARY")
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "BASIC")
    private double basic;
    @Column(name = "ALLOWANCE")
    private double allowance;
    @Column(name = "TAX")
    private double tax;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "EMP_ID", referencedColumnName = "ID", updatable = true)
    private Employee employee;
//    @Column(name = "EMP_ID")
//    private Integer empId;
}
