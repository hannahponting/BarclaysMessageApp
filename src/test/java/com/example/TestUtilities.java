package com.example;

import com.example.entities.Message;
import com.example.entities.Person;

import java.util.ArrayList;
import java.util.List;

public class TestUtilities {
    public static List<Person> getPersonList() {
            ArrayList<Person> people = new ArrayList<>();
            Person person1 = new Person("Hannah");
            Person person2 = new Person("Maria");
            Person person3 = new Person("Rui");

            people.add(person1);
            people.add(person2);
            people.add(person3);

            return people;
        }

        public static List<Message> getMessageList() {
            ArrayList<Message> message = new ArrayList<>();
            Message message1 = new Message("content");
            Message message2 = new Message("content");
            Message message3 = new Message("Content");

            message.add(message1);
            message.add(message2);
            message.add(message3);


            return message;
            }
    }

