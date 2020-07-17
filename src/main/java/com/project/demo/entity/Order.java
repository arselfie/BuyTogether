package com.project.demo.entity;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "BT_ORDER")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "COURIER_ID")
    private User courier;

    @OneToMany(mappedBy = "order")
    private List<Item> itemList;

    @Enumerated(EnumType.STRING)
    @Column(name = "ORDER_STATUS", nullable = false)
    private OrderStatus orderStatus;

    @NotNull(message = "Order's address is null")
    @ManyToOne
    @JoinColumn(name = "ADDRESS_ID")
    private Address address;

    @Column(name = "RATING")
    private Integer rating;


}
