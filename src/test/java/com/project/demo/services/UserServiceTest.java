package com.project.demo.services;

import com.project.demo.entity.EntityStatus;
import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserStatus;
import com.project.demo.entity.user.UserType;
import com.project.demo.exceptions.ValidationException;
import com.project.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest extends ServiceTestAncestor {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void find() {
    }

    @Test
    void testFind() {
    }

    @Test
    void findByLogin() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void createUserWhenUserIsNull() {

        ValidationException exception = assertThrows(ValidationException.class, () -> userService.createUser(null));
        assertEquals("User is null", exception.getMessage());
    }

    @Test
    void createUser() {
        User testUser = new User();
        testUser.setName("Arsen");
        testUser.setLogin("Ars1");
        testUser.setPassword("123");
        testUser.setEmail("ars@upce.cz");
        testUser.setUserType(UserType.CUSTOMER);

        User newUser = userService.createUser(testUser);

        assertEquals(testUser, newUser);


    }


    @Test
    void activation() throws Exception {
        User testUser = new User();
        testUser.setName("Arsen");
        testUser.setLogin("Arsen1");
        testUser.setPassword("123");
        testUser.setEmail("ars@upce.cz");
        testUser.setUserType(UserType.CUSTOMER);
        testUser.setUserStatus(UserStatus.NEW);
        testUser.setEntityStatus(EntityStatus.ACTIVE);

        Mockito.when(userRepository.findByLogin(testUser.getUsername())).thenReturn(testUser);

        Mockito.when(userRepository.save(testUser)).thenReturn(testUser);

        userService.activateUser("Arsen1");

        assertEquals(UserStatus.ACTIVE, testUser.getUserStatus());

    }


    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}