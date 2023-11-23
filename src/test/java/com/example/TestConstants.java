package com.example;

import com.example.entities.Person;

public class TestConstants {
    public static final String EXPECTED_PERSON_JSON = """
            [{"id":1000,"name":"Hannah"},{"id":2000,"name":"Hannah2"},{"id":3000,"name":"Hannah3"},{"id":4000,"name":"Hannah4"}]""";
    public static final String EXPECTED_MESSAGE_JSON = """
            [{"id":1000,"content":"First text message"},{"id":2000,"content":"Second text message"}, {"id":3000,"content":"Third text message"},{"id":4000,"content":"Fourth text message"} ]""";
    public static final String EXPECTED_ONE_PERSON_JSON = """
            {"id":1000,"name":"Hannah"}""";
    public static final String EXPECTED_ONE_MESSAGE_JSON = """
            {"id":1000,"content":"First text message"}""";
    public static final String BILL = """
            {"name":"Bill", "email":"bill@gmail.com"}""";
    public static final Person PERSON_BILL = new Person("Bill", "bill@gmail.com");

    public static final String BOTH_ID_ZERO = """
           {"id":0, "sender": {"id":0, "email": "hannah@gmail.com","name": "Hannah"}, "content": "Joshs first message" }""";
    public static final String BOTH_ID_NULL = """
           {"sender": {"email": "hannah@gmail.com","name": "Hannah"}, "content": "Joshs first message" }""";
}

