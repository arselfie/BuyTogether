package com.project.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.dto.UserDto;
import com.project.demo.entity.EntityStatus;
import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserStatus;
import com.project.demo.entity.user.UserType;
import com.project.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

@ExtendWith(MockitoExtension.class)
class UserControllerTest extends ControllerTest {

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

    @InjectMocks
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    private void init() {
        MockitoAnnotations.initMocks(this);
    }

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

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

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

    @Test
    void login() throws Exception {
//        User testUser = new User();
//        testUser.setName("Maks");
//        testUser.setLogin("Maksim");
//        testUser.setPassword("$2a$10$ITjRTdRw4E4n3aa2z0EWteNOavKfBZBl0dS4EkYtZiIKmcL2U9rbC");
//        testUser.setEmail("maks@upce.cz");
//        testUser.setUserType(UserType.CUSTOMER);
//        testUser.setUserStatus(UserStatus.NEW);
//        testUser.setEntityStatus(EntityStatus.ACTIVE);

//        Mockito.when(userRepository.findByLogin(testUser.getLogin())).thenReturn(testUser);

        registration();

        UserDto userDto = new UserDto();
        userDto.setLogin("Ars");
         userDto.setPassword("12345");



        String content = objectMapper.writeValueAsString(userDto);
        System.out.println(content);

        ResultActions resultActions = mockMvc.perform(post("/api/v1/auth/login")
                .content(content).contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();

        UserDto userDtoResponse = objectMapper.readValue(responseContent, UserDto.class);

        assertNotNull(userDtoResponse.getToken());

//        resultActions.andDo(document(
//                "registration",
//                preprocessRequest(prettyPrint()),
//                preprocessResponse(prettyPrint()),
//                requestFields(REGISTRATION_REQUEST),
//                responseFields(REGISTRATION_RESPONSE)
//        ));

    }


}