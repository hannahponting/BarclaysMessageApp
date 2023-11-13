package com.example.controllers;

import com.example.services.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class PersonControllerSomeSpringTest {

    @MockBean
    PersonService personService;

    @Autowired
    PersonController uut;

    @Test
    public void testOfGetAllPersons(){
        uut.getAllPersons();
        Mockito.verify(personService, times(1)).getAllPeople();
    }




}