package com.project.demo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.demo.entity.*;
import com.project.demo.entity.order.Order;
import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserType;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public UserDto() {
    }

    private Long id;

    private List<AddressDto> addresses;

    private String name;

    private String login;

    private String password;

    private String email;

    private EntityStatus entityStatus;

    private UserType userType;

    private List<Order> orderList;

    public UserDto(User user) {
        this.id = user.getId();
        this.addresses = user.getAddresses().stream().map(AddressDto::new).collect(Collectors.toList());
        this.name=user.getName();
        this.login=user.getLogin();
        this.email=user.getEmail();
        this.entityStatus= user.getEntityStatus();
        this.userType=user.getUserType();

    }

    public User toEntity() {
        User user = new User();

        user.setId(id);
        user.setAddresses(addresses.stream().map(AddressDto::toEntity).collect(Collectors.toList()));
        user.setName(name);
        user.setLogin(login);
        user.setEmail(email);
        user.setEntityStatus(entityStatus);
        user.setUserType(userType);

        return user;
    }


}
