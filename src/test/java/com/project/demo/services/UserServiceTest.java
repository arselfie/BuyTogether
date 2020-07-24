package com.project.demo.services;

import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserType;
import com.project.demo.exceptions.ValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;


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

        ValidationException exception= assertThrows(ValidationException.class,()->userService.createUser(null));
        assertEquals("User is null",exception.getMessage());
    }

    @Test
    void createUser() {
    User testUser= new User();
    testUser.setName("Arsen");
    testUser.setLogin("Ars");
    testUser.setPassword("123");
    testUser.setEmail("ars@upce.cz");
    testUser.setUserType(UserType.CUSTOMER);

    User newUser= userService.createUser(testUser);

    assertEquals(testUser,newUser);


    }



    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }
}