package com.project.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.demo.EntityGenerator;
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
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @MockBean
    private UserRepository userRepository;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private final FieldDescriptor[] REGISTRATION_REQUEST = new FieldDescriptor[]{
            fieldWithPath("login").description("Login").type(JsonFieldType.STRING),
            fieldWithPath("name").description("User name").type(JsonFieldType.STRING),
            fieldWithPath("password").description("Password").type(JsonFieldType.STRING),
            fieldWithPath("email").description("Email").type(JsonFieldType.STRING),
            fieldWithPath("userType").description("User type").type(JsonFieldType.STRING),
    };

    private final FieldDescriptor[] LOGIN_REQUEST = new FieldDescriptor[]{
            fieldWithPath("login").description("Login").type(JsonFieldType.STRING),
            fieldWithPath("password").description("Password").type(JsonFieldType.STRING),
    };

    private final FieldDescriptor[] LOGIN_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("token").description("Password").type(JsonFieldType.STRING),
    };

    @Test
    void registration() throws Exception {
        Mockito.when(userRepository.save(any())).thenCallRealMethod();

        UserDto userDto = new UserDto();
        userDto.setLogin("Ars");
        userDto.setName("Arsen");
        userDto.setPassword("12345");
        userDto.setEmail("123@test.test");
        userDto.setUserType(UserType.CUSTOMER);

        String content = objectMapper.writeValueAsString(userDto);

        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/auth/registration")
                .content(content).contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        UserDto userDtoResponse = objectMapper.readValue(responseContent, UserDto.class);

        assertNotNull(userDtoResponse.getId());

        resultActions.andDo(document(
                "registration",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(REGISTRATION_REQUEST)
        ));
    }

    @Test
    void login() throws Exception {
        User user = EntityGenerator.generateCustomer();
        user.setPassword(PASSWORD_HASH);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);

        UserDto userDto = new UserDto();
        userDto.setLogin(user.getLogin());
        userDto.setPassword(PASSWORD);


        String content = objectMapper.writeValueAsString(userDto);

        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/auth/login")
                .content(content).contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        UserDto userDtoResponse = objectMapper.readValue(responseContent, UserDto.class);

        assertNotNull(userDtoResponse.getToken());

        resultActions.andDo(document(
                "login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(LOGIN_REQUEST),
                responseFields(LOGIN_RESPONSE)
        ));
    }


}