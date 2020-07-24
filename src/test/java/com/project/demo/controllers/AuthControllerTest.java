package com.project.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.dto.UserDto;
import com.project.demo.entity.user.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AuthControllerTest extends ControllerTest {

    private final FieldDescriptor[] REGISTRATION_REQUEST = new FieldDescriptor[]{
            fieldWithPath("login").description("Login").type(JsonFieldType.STRING),
            fieldWithPath("name").description("User name").type(JsonFieldType.STRING),
            fieldWithPath("password").description("Password").type(JsonFieldType.STRING),
            fieldWithPath("email").description("Email").type(JsonFieldType.STRING),
            fieldWithPath("userType").description("User type").type(JsonFieldType.VARIES),
    };

    private final FieldDescriptor[] REGISTRATION_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").description("Id").type(JsonFieldType.NUMBER),
            fieldWithPath("login").description("Login").type(JsonFieldType.STRING),
            fieldWithPath("name").description("User name").type(JsonFieldType.STRING),
            fieldWithPath("email").description("Email").type(JsonFieldType.STRING),
            fieldWithPath("userType").description("User type").type(JsonFieldType.VARIES),
            fieldWithPath("entityStatus").description("Entity status").type(JsonFieldType.VARIES),
    };




    @Test
    void registration() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setLogin("Ars");
        userDto.setName("Arsen");
        userDto.setPassword("12345");
        userDto.setEmail("123@test.test");
        userDto.setUserType(UserType.CUSTOMER);

        String content = objectMapper.writeValueAsString(userDto);
        System.out.println(content);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/registration")
                .content(content).contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult =resultActions.andExpect(status().isOk()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        UserDto userDtoResponse = objectMapper.readValue(responseContent, UserDto.class);

        assertNotNull(userDtoResponse.getId());

        resultActions.andDo(document(
                "registration",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(REGISTRATION_REQUEST),
                responseFields(REGISTRATION_RESPONSE)
        ));

    }
}