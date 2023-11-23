package com.example.controllers;

import com.example.TestConstants;
import com.example.entities.Message;
import com.example.entities.Person;
import com.example.services.MessageServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MessageControllerNoSpringTest{
    MessageServiceImp messageServiceImp;
    MessageController messageController;

    @BeforeEach
    void setUp(){
        messageServiceImp = mock(MessageServiceImp.class);
        messageController = new MessageController(messageServiceImp);

    }

    @Test
    void getAllMessages(){
        messageController.getAllMessages();
        verify(messageServiceImp, times(1)).findAll();
    }

    @Test
    void getAllMessagesById(){
        Long messageId = 1L;
        try {
            messageController.getMessageById(messageId);
        } catch (ResponseStatusException rse){
            System.out.println("Expected error thrown");
        }
        verify(messageServiceImp, times(1)).getMessageById(messageId);
    }

    @Test
    void testGetMessageByIdBadRequest(){
        when(messageServiceImp.getMessageById(0)).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> {
            this.messageController.getMessageById(0);
        });
    }




    @Test
    void getAllMessagesBySenderName(){
        String sender = "Dave";
        List<Message> message = new ArrayList<Message>();
        message.add(new Message("Hi", new Person(sender)));
        when(messageServiceImp.getMessageBySenderName(sender)).thenReturn(message);
        messageController.getMessagesBySenderName(sender);
        verify(messageServiceImp, times(1)).getMessageBySenderName(sender);
    }

    @Test
    void getAllMessagesBySenderNameBadRequest(){
        String sender = "Dave";
        List<Message> message = new ArrayList<>();
        when(messageServiceImp.getMessageBySenderName(sender)).thenReturn(message);
        assertThrows(ResponseStatusException.class, () -> {
            this.messageController.getMessagesBySenderName(sender);
        });
    }

    @Test
    void getAllMessagesBySenderEmail(){
        String sender = "hannah@gmail.com";
        List<Message> message = new ArrayList<Message>();
        message.add(new Message("Hi", new Person(sender)));
        when(messageServiceImp.getMessageBySenderEmail(sender)).thenReturn(message);
        messageController.getMessagesBySenderEmail(sender);
        verify(messageServiceImp, times(1)).getMessageBySenderEmail(sender);
    }

    @Test
    void getAllMessagesBySenderEmailBadRequest(){
        String sender = "hannah@gmail.com";
        List<Message> message =  new ArrayList<>();
        when(messageServiceImp.getMessageBySenderEmail(sender)).thenReturn(message);
        assertThrows(ResponseStatusException.class, () -> {
            this.messageController.getMessagesBySenderEmail(sender);
        });
    }
}