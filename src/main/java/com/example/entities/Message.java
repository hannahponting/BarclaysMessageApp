package com.example.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Message {

    @Id
    @GeneratedValue
    private Long id;


    @ManyToOne
    private Person sender;

    private String content;

    public Message(){}
    public Message(String content){
        this.content=content;
    }
    public Message(String content, Person sender){
        this.content=content;
        this.sender= sender;
    }


    public Long getId() {
        return id;
    }
    public Person getSender() {
        return sender;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setSender(Person sender) {
        this.sender = sender;
    }

}
