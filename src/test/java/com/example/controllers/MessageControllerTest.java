package com.example.controllers;

import com.example.services.MessageService;
import com.example.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest
class MessageControllerTest {

    @MockBean
    MessageService messageService;

    @MockBean
    PersonService personService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testServiceCalledFor_getAllMessages() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages");
        mockMvc.perform(requestBuilder);

        verify(messageService, times(1)).getAllMessages();
    }

}