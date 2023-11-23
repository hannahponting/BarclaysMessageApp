package com.example.dataaccess;

import com.example.entities.Person;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PersonRepository  extends ListCrudRepository<Person, Long> {

    List<Person> findPersonsByEmailIgnoreCase(String email);

}
