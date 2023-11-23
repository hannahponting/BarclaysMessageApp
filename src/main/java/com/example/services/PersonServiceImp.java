package com.example.services;

import com.example.dataaccess.PersonRepository;
import com.example.entities.Person;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImp implements PersonService{

    PersonRepository personRepository;

    @Autowired
    public PersonServiceImp(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return this.personRepository.findAll();
    }

    public Person getPersonById(long personId) {
        Optional<Person> person = this.personRepository.findById(personId);
        return person.orElse(null);
    }

    public Person addPerson(Person person){
        if(person.getId()!= null && person.getId() != 0)
            throw new IllegalArgumentException("The message id provided for a create/post must be null or zero");
        if (person.getId() != null)
            if (this.personRepository.existsById(person.getId()))
                throw new EntityNotFoundException("The person with ID " + person.getId() + "already exists");

        return this.personRepository.save(person);
    }
}
