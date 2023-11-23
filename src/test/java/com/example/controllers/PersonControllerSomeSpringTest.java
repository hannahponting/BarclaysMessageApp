package com.example.controllers;

import com.example.services.PersonServiceImp;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;

@SpringBootTest
class PersonControllerSomeSpringTest {

    @MockBean
    PersonServiceImp personServiceImp;

    @Autowired
    PersonController uut;

    @Test
    public void testOfGetAllPersons(){
        uut.getAllPeople();
        Mockito.verify(personServiceImp, times(1)).findAll();
    }




}