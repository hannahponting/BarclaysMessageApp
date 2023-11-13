package com.example.controllers;

import com.example.entities.Message;
import com.example.services.MessageService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class MessageControllerNoSpringTest {

    @Test
    void getAllMessages(){
        MessageService messageService = mock(MessageService.class);
        MessageController messageController = new MessageController(messageService);
        List<Message> messages = messageController.getAllMessages();
        verify(messageService, times(1)).getAllMessages();
    }

}