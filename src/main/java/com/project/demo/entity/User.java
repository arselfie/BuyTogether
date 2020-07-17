package com.project.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "BT_USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToMany(mappedBy = "customer")
    private List <Address> addresses;

    @NotBlank
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotBlank(message = "Login is blank")
    @Column(name = "LOGIN", nullable = false, unique = true)
    private String login;

    @NotBlank(message = "Password is blank")
    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Email
    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityStatus entityStatus;

    @NotNull(message = "User type is null")
    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @OneToMany(mappedBy = "courier")
    private List<Order> courierList;

    @OneToMany(mappedBy = "customer")
    private List<Order> customerList;



}
