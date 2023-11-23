package com.example.controllers;

import com.example.services.PersonServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class PersonControllerNoSpringTest {
    PersonController personController;
    PersonServiceImp personServiceImp;

    @BeforeEach
    void setUp(){
        personServiceImp = Mockito.mock(PersonServiceImp.class);
        personController = new PersonController(personServiceImp);
    }

    @Test
    public void testOfGettingAllPeople(){
        personController.getAllPeople();
        Mockito.verify(personServiceImp, times(1)).findAll();
    }

    @Test
    void getAPersonById(){
        Long personId = 1L;
        try {
            personController.getPersonById(personId);
        } catch (ResponseStatusException rse){
            System.out.println("Expected error thrown");
        }
        Mockito.verify(personServiceImp, times(1)).getPersonById(personId);
    }

    @Test
    void testGetPersonByIdBadRequest(){
        when(personServiceImp.getPersonById(0)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> {
            personController.getPersonById(0);
        });
    }


}