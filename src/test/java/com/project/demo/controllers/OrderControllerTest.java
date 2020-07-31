package com.project.demo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.demo.EntityGenerator;
import com.project.demo.dto.AddressDto;
import com.project.demo.dto.ItemDto;
import com.project.demo.dto.OrderDto;
import com.project.demo.dto.UserDto;
import com.project.demo.entity.address.Address;
import com.project.demo.entity.item.Item;
import com.project.demo.entity.order.Order;
import com.project.demo.entity.token.TokenType;
import com.project.demo.entity.user.User;
import com.project.demo.repository.AddressRepository;
import com.project.demo.repository.ItemRepository;
import com.project.demo.repository.OrderRepository;
import com.project.demo.repository.UserRepository;
import com.project.demo.security.JwtTokenProvider;
import com.project.demo.services.UserService;
import io.jsonwebtoken.lang.Assert;
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


import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerTest extends ControllerTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private final FieldDescriptor[] CREATE_ORDER_REQUEST = new FieldDescriptor[]{
            fieldWithPath("itemList").description("List of items").type(JsonFieldType.ARRAY),
            fieldWithPath("itemList[].name").description("Name of item").type(JsonFieldType.STRING),
            fieldWithPath("itemList[].description").description("Item's description").type(JsonFieldType.STRING),
            fieldWithPath("address").description("Address").type(JsonFieldType.OBJECT),
            fieldWithPath("address.street").description("Street").type(JsonFieldType.STRING),
            fieldWithPath("address.city").description("City").type(JsonFieldType.STRING),
            fieldWithPath("address.postalCode").description("Postal code").type(JsonFieldType.NUMBER),

    };



    private final FieldDescriptor[] CREATE_ORDER_RESPONSE = new FieldDescriptor[]{
            fieldWithPath("id").description("Order id").type(JsonFieldType.NUMBER),

            fieldWithPath("address").description("Address").type(JsonFieldType.OBJECT),
            fieldWithPath("address.id").description("Address' is").type(JsonFieldType.NUMBER),
            fieldWithPath("address.street").description("Street").type(JsonFieldType.STRING),
            fieldWithPath("address.city").description("City").type(JsonFieldType.STRING),
            fieldWithPath("address.postalCode").description("Postal code").type(JsonFieldType.NUMBER),

            fieldWithPath("itemList").description("List of items").type(JsonFieldType.ARRAY),
            fieldWithPath("itemList[].id").description("Id of item").type(JsonFieldType.NUMBER),
            fieldWithPath("itemList[].name").description("Name of item").type(JsonFieldType.STRING),
            fieldWithPath("itemList[].description").description("Item's description").type(JsonFieldType.STRING),
            fieldWithPath("itemList[].itemStatus").description("Item's status").type(JsonFieldType.VARIES),

            fieldWithPath("orderStatus").description("Status of order").type(JsonFieldType.STRING),

    };


    @Test
    void createOrder() throws Exception {

        OrderDto orderDto = new OrderDto();

        User user = EntityGenerator.generateCustomer();

        user.setId(1L);


        Mockito.when(userRepository.findByLogin(user.getLogin())).thenReturn(user);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(java.util.Optional.of(user));

        Mockito.when(orderRepository.save(any()))
                .then(e -> {
                    Order order = e.getArgument(0, Order.class);
                    order.setId(1L);
                    return order;
                });

        Mockito.when(addressRepository.save(any()))
                .thenAnswer(e -> {
                    Address address = e.getArgument(0, Address.class);
                    address.setId(1L);
                    return address;
                });

        Mockito.when(itemRepository.save(any()))
                .thenAnswer(e -> {
                    Item item = e.getArgument(0, Item.class);
                    item.setId(1L);
                    return item;
                });

        //System.out.println(userRepository.findByLogin(user.getLogin()));

        String token = jwtTokenProvider.createToken(user.getLogin(), TokenType.USER_AUTHENTICATION);

        List<ItemDto> itemDtoList = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            Item item = EntityGenerator.generateItem();
            item.setItemStatus(null);
            ItemDto itemDto = new ItemDto(item);
            itemDtoList.add(itemDto);
        }

        //assertEquals(5,itemDtoList.size());

        orderDto.setItemList(itemDtoList);

        AddressDto addressDto = new AddressDto(EntityGenerator.generateAddress());

        orderDto.setAddress(addressDto);

        String content = objectMapper.writeValueAsString(orderDto);


        ResultActions resultActions = mockMvc
                .perform(post("/api/v1/orders")
                        .header("Authorization", token)
                        .content(content).contentType(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = mvcResult.getResponse().getContentAsString();
        OrderDto orderDtoResponse = objectMapper.readValue(responseContent, OrderDto.class);

        resultActions.andDo(document(
                "orders/createOrder",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(CREATE_ORDER_REQUEST),
                responseFields(CREATE_ORDER_RESPONSE)

        ));

        assertNotNull(orderDtoResponse.getId());


    }
}