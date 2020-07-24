package com.project.demo.entity.item;

import com.project.demo.entity.EntityAncestor;
import com.project.demo.entity.order.Order;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "BT_ITEM")
public class Item extends EntityAncestor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ITEM_ID")
    private Long id;

    @Column(name = "NAME",nullable = false)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "ITEM_STATUS",nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID",nullable = false)
    private Order order;

}
