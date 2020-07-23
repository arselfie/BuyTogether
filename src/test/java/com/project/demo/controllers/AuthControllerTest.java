package com.project.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.dto.UserDto;
import com.project.demo.entity.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void registration() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setLogin("Ars");
        userDto.setName("Arsen");
        userDto.setPassword("12345");
        userDto.setUserType(UserType.CUSTOMER);

        String content = objectMapper.writeValueAsString(userDto);
        System.out.println(content);

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/registration")
                .content(content).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        UserDto userDtoResponse = objectMapper.readValue(responseContent, UserDto.class);

        assertNotNull(userDtoResponse.getId());
    }
}