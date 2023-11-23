package com.example.dataaccess;

import com.example.entities.Message;
import com.example.entities.Person;
import org.springframework.stereotype.Component;

@Component
public class Populator {

    MessageRepository messageRepo;
    PersonRepository personRepo;

    public Populator(MessageRepository messageRepo, PersonRepository personRepo){
        this.messageRepo = messageRepo;
        this.personRepo = personRepo;

    }

//    @EventListener(ContextRefreshedEvent.class)
    public void populate(){
        Person person = new Person("Hannah", "Hannah@gmail.com");
        this.personRepo.save(person);
        Person dave = new Person("Dave", "Dave@dave.com");
        this.personRepo.save(dave);
        Message message = new Message("This is a message", person);
        this.messageRepo.save(message);
        message = new Message("New message", person);
        this.messageRepo.save(message);
        message = new Message("New dave message", dave);
        this.messageRepo.save(message);
    }
}
