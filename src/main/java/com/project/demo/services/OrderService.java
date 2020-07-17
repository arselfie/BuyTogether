package com.project.demo.services;

import com.project.demo.entity.*;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private CommonService commonService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private ItemService itemService;

    public List<Order> find() {
        return orderRepository.findAll();
    }

    public Order find(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public boolean isOrderNull(Order order) {

        if (order == null) {
            throw new ValidationException("Order is null");
        }
        return true;
    }

    public User extractCustomer(Order order) {

        if (order.getCustomer() == null) {
            throw new ValidationException("Customer is null");
        }

        User customer = userService.find(order.getCustomer().getId());

        if (customer == null) {
            throw new ValidationException("Customer not found");
        }

        if (customer.getEntityStatus()!=EntityStatus.ACTIVE){
            throw new ValidationException("Customer's status is not active");
        }

        if (customer.getUserType() != UserType.CUSTOMER) {
            throw new ValidationException("Customer is not customer");
        }

            return customer;
    }

    @Transactional(rollbackOn = Exception.class)
    public Order createOrder(Order order) {

        if (order == null) {
            throw new ValidationException("Order is null");
        }

        commonService.validate(order);


        User customer = extractCustomer(order);

        order.setCustomer(customer);

        Address orderAddress = order.getAddress();

        if (customer.getAddresses().stream().noneMatch(address -> address.getId().equals(orderAddress.getId()))) {
            customer.getAddresses().add(orderAddress);
            orderAddress.setCustomer(customer);
            addressService.createAddress(orderAddress);

        }

        order.setOrderStatus(OrderStatus.NEW);

        if (order.getItemList().isEmpty()) {
            throw new ValidationException("Item list is empty");
        }

        order= orderRepository.save(order);

        for (Item item : order.getItemList()) {
            item.setItemStatus(ItemStatus.NEW);
            item.setOrder(order);
            itemService.createItem(item);
        }

        return order;
    }

    public Order updateOrder(Long id, Order order) {
        Order existedOrder = find(id);

        if (existedOrder != null) {
            existedOrder.setOrderStatus(order.getOrderStatus());
            existedOrder.setAddress(order.getAddress());
            orderRepository.save(existedOrder);

        }
        return existedOrder;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public void assignOrder(Order order, User courier) {
        Order orderFromDb = find(order.getId());

        User courierFromDb = userService.find(courier.getId());


        if (orderFromDb == null) {
            throw new ValidationException("Order not exist");
        }

        if (courierFromDb == null) {
            throw new ValidationException("Courier not exist");
        }

        if (orderFromDb.getOrderStatus() != OrderStatus.NEW) {
            throw new ValidationException("Order should have status NEW");
        }

        if (courierFromDb.getUserType() != UserType.COURIER) {
            throw new ValidationException("Courier is not courier");
        }

        if (courierFromDb.getEntityStatus() != EntityStatus.ACTIVE) {
            throw new ValidationException("Courier is not active");
        }

        orderFromDb.setOrderStatus(OrderStatus.IN_PROGRESS);
        orderFromDb.setCourier(courierFromDb);

        orderRepository.save(orderFromDb);

    }

}
