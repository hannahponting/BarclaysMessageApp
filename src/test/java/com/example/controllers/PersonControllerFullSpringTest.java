package com.example.controllers;

import com.example.entities.Message;
import com.example.services.MessageService;
import com.example.services.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest
class PersonControllerFullSpringTest {

    @MockBean
    PersonService personService;

    @MockBean
    MessageService messageService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testOfGetAllPerson() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/people");
        mockMvc.perform(requestBuilder);
        verify(personService, times(1)).getAllPeople();
    }
}