package com.project.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Entity
@Table(name = "BT_ADDRESS")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ADDRESS_ID")
    private Long id;

    @ManyToOne(fetch =FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User customer;

    @OneToMany(fetch =FetchType.LAZY,mappedBy = "address")
    private List<Order> orderList;

    @Size(min = 3)
    @Column(name = "STREET", nullable = false)
    private String street;

    @Column(name = "CITY", nullable = false)
    private String city;

    @Min(value = 1000)
    @Max(value = 100000)
    @Column(name = "POSTAL_CODE", nullable = false)
    private Integer postalCode;


}
