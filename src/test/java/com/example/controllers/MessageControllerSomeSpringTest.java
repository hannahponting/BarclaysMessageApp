package com.example.controllers;

import com.example.services.MessageServiceImp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class MessageControllerSomeSpringTest {

    @Autowired
    MessageController uut;

    @MockBean
    MessageServiceImp messageServiceImp;


    @Test
    public void getObjectsFromSpring(){
        uut.getAllMessages();
        verify(messageServiceImp, times(1)).findAll();
    }
}