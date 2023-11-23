package com.example.controllers;

import com.example.TestConstants;
import com.example.TestUtilities;
import com.example.entities.Person;
import com.example.services.PersonServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonController.class)
class PersonControllerFullSpringTest {

    @MockBean
    PersonServiceImp personServiceImp;


    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testOfGetAllPeople() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/people");
        mockMvc.perform(requestBuilder);
        verify(personServiceImp, times(1)).findAll();
    }

    @Test
    void getAllPeopleTest() throws Exception {
        List<Person> people = TestUtilities.getPersonList();
        when(personServiceImp.findAll()).thenReturn(people);

        ResultActions resultActions = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/people")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Person[] actualPeople = mapper.readValue(contentAsString, Person[].class);
        assertEquals(3, actualPeople.length);
    }


    @Test
    void testServiceCallGetPersonById() throws Exception {
        Long personId = 1000L;
        Person person = new Person("Hannah");
        when(personServiceImp.getPersonById(personId)).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/people/"+Long.toString(personId));
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(personServiceImp, times(1)).getPersonById(personId);
    }

    @Test
    void testGetPersonByIdBadIndex() throws Exception {
        long personId = 0;

        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/people/"+Long.toString(personId));
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void addPerson() throws Exception {
        Person person = new Person("Bill", "bill@gmail.com");
        String json = mapper.writeValueAsString(person);
        when(personServiceImp.addPerson(any())).thenReturn(person);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/people")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        Person resultPerson = mapper.readValue(contentAsString, Person.class);

        Mockito.verify(personServiceImp, times(1)).addPerson(any(Person.class));

    }



    @Test
    void addPersonString() throws Exception {
        Person person = new Person("Bill", "bill@gmail.com");
        String json = TestConstants.BILL;
        when(personServiceImp.addPerson(any())).thenReturn(person);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        Person resultPerson = mapper.readValue(contentAsString, Person.class);

        Mockito.verify(personServiceImp, times(1)).addPerson(any(Person.class));

    }


    @Test
    void addPersonWithBadID() throws Exception {
        String json = """
                {"id":10, "name":"Bill", "email":"bill@gmail.com"}""";
        when(personServiceImp.addPerson(any())).thenThrow(new IllegalArgumentException("some stuff"));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Mockito.verify(personServiceImp, times(1)).addPerson(any(Person.class));

    }







}