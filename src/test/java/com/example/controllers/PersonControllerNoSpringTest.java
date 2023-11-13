package com.example.controllers;

import com.example.services.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

class PersonControllerNoSpringTest {

    PersonController uut;

    PersonService personService;

    @Test
    public void testOfGettingPerson(){
        personService = Mockito.mock(PersonService.class);
        uut = new PersonController(personService);
        uut.getAllPersons();
        Mockito.verify(personService, times(1)).getAllPeople();

    }

}