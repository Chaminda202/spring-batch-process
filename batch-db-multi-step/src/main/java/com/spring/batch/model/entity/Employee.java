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
@Table(name = "EMPLOYEE")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "AGE")
    private int age;
    @Column(name = "OCCUPATION")
    private String occupation;
    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    private Salary salary;
//    @OneToOne(mappedBy = "employee", fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//    private transient Salary salary;
}
