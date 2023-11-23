package com.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue
    private Long id;
    private String email;
    private String name;

    @OneToMany(mappedBy = "sender")
    private List<Message> sentMessages;

    public Person(){};

    public Person(String name){
        this.name = name;
    }
    public Person(String name, String email){
        this.name = name;
        this.email=email;
    }

//    public List<Message> getSentMessages() {
//        return sentMessages;
//    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getId() {
        return id;
    }


}
