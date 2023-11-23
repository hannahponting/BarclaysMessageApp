package com.example.services;

import com.example.TestUtilities;
import com.example.dataaccess.MessageRepository;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Message;
import com.example.entities.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class MessageServiceImpNoSpringTest {
    MessageRepository mockRepository;
    MessageServiceImp messageServiceImp;
    PersonRepository personRepository;

    @BeforeEach
    void beforeEach(){
        this.mockRepository = mock(MessageRepository.class);
        this.personRepository = mock(PersonRepository.class);
        messageServiceImp = new MessageServiceImp(mockRepository, personRepository);
    }


    @Test
    void findAll(){
        List<Message> message = TestUtilities.getMessageList();
        when(mockRepository.findAll()).thenReturn(message);
        List<Message> actualMessages = messageServiceImp.findAll();
        Assertions.assertEquals(message, actualMessages);
    }

    @Test
    void testRepoCalled(){
        List<Message> actualMessages = messageServiceImp.findAll();
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    void getMessageIfEmpty(){
        long messageId = 1;
        Optional<Message> optionalMessage = Optional.empty();
        when(mockRepository.findById(messageId)).thenReturn(optionalMessage);
        Message actualMessages = messageServiceImp.getMessageById(messageId);
        Assertions.assertNull(actualMessages);
    }

    @Test
    void testGetMessageWithMessage(){
        Message message = new Message("hi");
        when(mockRepository.findById(any())).thenReturn(Optional.of(message));
        Message actual = messageServiceImp.getMessageById(1);
        Assertions.assertEquals(message.getId(), actual.getId());
        Assertions.assertEquals(message.getContent(), actual.getContent());
    }


    @Test
    void getMessageByNameIfEmpty(){
        String name = "Hannah";
        when(mockRepository.findMessagesBySenderNameIgnoreCase(name)).thenReturn(null);
        List <Message> actualMessages = messageServiceImp.getMessageBySenderName(name);
        Assertions.assertNull(actualMessages);
    }

    @Test
    void testGetMessageByNameWithMessage(){
        Person person = new Person("Hannah", "h@gmai.com");
        Message message = new Message("hi", person);
        when(mockRepository.findMessagesBySenderNameIgnoreCase(any())).thenReturn(List.of(message));
        List<Message> actual = messageServiceImp.getMessageBySenderName("Hannah");
        Assertions.assertEquals(message, actual.get(0));
        Assertions.assertEquals(message.getSender(), person);
    }

    @Test
    void getMessageByEmailIfEmpty(){
        String name = "Hannah@gmail.com";
        when(mockRepository.findMessagesBySenderEmailIgnoreCase(name)).thenReturn(null);
        List <Message> actualMessages = messageServiceImp.getMessageBySenderEmail(name);
        Assertions.assertNull(actualMessages);
    }

    @Test
    void testGetMessageByEmailWithMessage(){
        Person person = new Person("Hannah", "h@gmai.com");
        Message message = new Message("hi", person);
        when(mockRepository.findMessagesBySenderEmailIgnoreCase(person.getEmail())).thenReturn(List.of(message));
        List<Message> actual = messageServiceImp.getMessageBySenderEmail(person.getEmail());
        Assertions.assertEquals(message, actual.get(0));
        Assertions.assertEquals(message.getSender(), person);
    }
}