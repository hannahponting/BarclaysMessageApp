package com.example.services;

import com.example.TestConstants;
import com.example.TestUtilities;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class PersonServiceImpSomeSpringTest {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    PersonServiceImp personServiceImp;

    @MockBean
    PersonRepository personRepo;

    @BeforeEach
    void setUp(){
        personRepo = mock(PersonRepository.class);
        personServiceImp = new PersonServiceImp(personRepo);
        reset(this.personRepo);
    }

    @Test
    void testOfGetAll(){
        List<Person> people = TestUtilities.getPersonList();
        when(personRepo.findAll()).thenReturn(people);
        List<Person> actualPeople = personServiceImp.findAll();
        assertEquals(people, actualPeople);
    }

    @Test
    void addPersonHappyPath(){
        when(personRepo.save(any())).thenReturn((TestConstants.PERSON_BILL));
        when(personRepo.existsById(null)).thenThrow(new InvalidDataAccessApiUsageException("Given id must not be null"));
        Person actual = this.personServiceImp.addPerson(TestConstants.PERSON_BILL);
        verify(personRepo, times(1)).save(TestConstants.PERSON_BILL);
    }

    @Test
    void addPersonWithExistingID() throws JsonProcessingException {
        String json = """
                {"id":5, "name":"Bill", "email":"bill@gmail.com"}""";
        Person personWithIdNumber = mapper.readValue(json, Person.class);

        assertThrows(IllegalArgumentException.class, () -> {
            this.personServiceImp.addPerson(personWithIdNumber);
        });
    }

        @Test
        void addPersonWithIDZero() throws JsonProcessingException {
            String json = """
                {"id":0, "name":"Bill", "email":"bill@gmail.com"}""";
            Person personWithIdNumber = mapper.readValue(json, Person.class);

            this.personServiceImp.addPerson(personWithIdNumber);
            verify(personRepo, times(1)).save(personWithIdNumber);
//
    }

}