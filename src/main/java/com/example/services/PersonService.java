package com.example.services;

import com.example.entities.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PersonService {
    public Iterable<Person> getAllPeople() {
        return new ArrayList<>();
    }
}
