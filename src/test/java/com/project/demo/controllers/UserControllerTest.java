package com.project.demo.controllers;

import com.project.demo.EntityGenerator;
import com.project.demo.dto.UserDto;
import com.project.demo.entity.user.User;
import com.project.demo.entity.user.UserStatus;
import com.project.demo.entity.user.UserType;
import com.project.demo.repository.UserRepository;
import com.project.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends ControllerTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


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
        Mockito.when(userRepository.save(any()))
                .then(e -> {
            User user = e.getArgument(0, User.class);
            user.setId(1L);
            return user;
        });

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

        resultActions.andDo(document(
                "user/registration",
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
                "user/login",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(LOGIN_REQUEST),
                responseFields(LOGIN_RESPONSE)
        ));
    }

    @Test
    void activation() throws Exception {
        User user = EntityGenerator.generateCustomer();
        user.setUserStatus(UserStatus.NEW);
        user.setPassword(PASSWORD_HASH);
        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);

        String token = userService.generateActivationToken(user);

        ResultActions resultActions = mockMvc
                .perform(get("/api/v1/auth/activation/{token}", token));

        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        resultActions.andDo(document(
                "user/activation",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                pathParameters(
                        parameterWithName("token").description("User activation token")
                )
        ));
    }




}