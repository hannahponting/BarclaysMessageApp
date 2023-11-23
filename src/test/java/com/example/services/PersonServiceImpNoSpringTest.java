package com.example.services;

import com.example.TestUtilities;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PersonServiceImpNoSpringTest {
    PersonServiceImp personServiceImp;
    PersonRepository personRepo;

    @BeforeEach
    void setUp(){
        personRepo = mock(PersonRepository.class);
        personServiceImp = new PersonServiceImp(personRepo);
    }
    @Test
    void testofGetAll(){
        List<Person> people = TestUtilities.getPersonList();
        when(personRepo.findAll()).thenReturn(people);
        List<Person> actualPeople = personServiceImp.findAll();

        assertEquals(people, actualPeople);
    }

    @Test
    void testOfGetAllCalled(){
        List<Person> actualPeople = personServiceImp.findAll();
        verify(personRepo, times(1)).findAll();
    }



}