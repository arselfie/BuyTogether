package com.project.demo.entity.user;

import com.project.demo.entity.EntityAncestor;
import com.project.demo.entity.address.Address;
import com.project.demo.entity.EntityStatus;
import com.project.demo.entity.order.Order;
import com.project.demo.services.CommonService;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "BT_USER")
public class User extends EntityAncestor implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @OneToMany(mappedBy = "customer")
    private List<Address> addresses;

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

    @NotNull(message = "User type is null")
    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @NotNull(message = "User status is null")
    @Column(name = "USER_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToMany(mappedBy = "courier")
    private List<Order> courierList;

    @OneToMany(mappedBy = "customer")
    private List<Order> customerList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getUsername() {
        return getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return CommonService.objectIn(userStatus, UserStatus.NEW, UserStatus.ACTIVE, UserStatus.BLOCKED);
    }

    @Override
    public boolean isAccountNonLocked() {
        return CommonService.objectIn(userStatus, UserStatus.NEW, UserStatus.ACTIVE, UserStatus.EXPIRED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return CommonService.objectIn(userStatus, UserStatus.NEW, UserStatus.ACTIVE, UserStatus.BLOCKED);
    }

    @Override
    public boolean isEnabled() {
        return CommonService.objectIn(userStatus, UserStatus.ACTIVE);
    }


}
