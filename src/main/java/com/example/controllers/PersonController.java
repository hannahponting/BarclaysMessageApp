package com.example.controllers;

import com.example.entities.Person;
import com.example.services.PersonServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/people")
public class PersonController {

    PersonServiceImp personServiceImp;

    @Autowired
    public PersonController(PersonServiceImp personServiceImp) {
        this.personServiceImp = personServiceImp;
    }

    @GetMapping
    public List<Person> getAllPeople(){
        return personServiceImp.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Person addPerson(@RequestBody Person person){
        Person newPerson;
       try{
           newPerson = personServiceImp.addPerson(person);
       }catch(IllegalArgumentException e){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
       }
       return newPerson;
    }

    @GetMapping("/{personId}")
    public Person getPersonById(@PathVariable long personId){
       Person person = personServiceImp.getPersonById(personId);
        if(person == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return person;
    }
}
