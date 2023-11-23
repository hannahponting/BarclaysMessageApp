package com.example.services;

import com.example.TestConstants;
import com.example.TestUtilities;
import com.example.dataaccess.MessageRepository;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Message;
import com.example.entities.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
class MessageServiceImpSomeSpringTest {
    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    MessageServiceImp messageServiceImp;

    @MockBean
    MessageRepository mockMessageRepo;

    @MockBean
    PersonRepository personRepo;

    @BeforeEach
    void beforeEach(){
        reset(mockMessageRepo);
    }

    @Test
    void testOfFindAll(){
        List<Message> message = TestUtilities.getMessageList();
        when(mockMessageRepo.findAll()).thenReturn(message);
        List<Message> actualMessages = messageServiceImp.findAll();

        assertEquals(message, actualMessages);
    }


    @Test
    void getMessageIfEmpty(){
        long messageId = 1;
        Optional<Message> optionalMessage = Optional.empty();
        when(mockMessageRepo.findById(messageId)).thenReturn(optionalMessage);

        Message actualMessages = messageServiceImp.getMessageById(messageId);
        assertNull(actualMessages);
    }

    @Test
    void testGetMessageWithMessage(){
        Message message = new Message("hi");
        when(mockMessageRepo.findById(any())).thenReturn(Optional.of(message));
        Message actual = messageServiceImp.getMessageById(1L);

        assertEquals(message, actual);
    }




    @Test
    void getMessageByEmailIfEmpty(){
        String email = "hannah@gmail.com";
        when(mockMessageRepo.findMessagesBySenderEmailIgnoreCase(email)).thenReturn(null);

        List <Message> actualMessages = messageServiceImp.getMessageBySenderEmail(email);
        assertNull(actualMessages);
    }

    @Test
    void testGetMessageByEmailWithMessage(){
        Person person = new Person("hannah", "email");
        Message message = new Message("hi", person);
        when(mockMessageRepo.findMessagesBySenderEmailIgnoreCase(person.getEmail())).thenReturn(List.of(message));
        List <Message> actual = messageServiceImp.getMessageBySenderEmail(person.getEmail());

        assertEquals(List.of(message), actual);
    }


    @Test
    void getMessageByNameIfEmpty(){
        String name = "hannah";
        when(mockMessageRepo.findMessagesBySenderNameIgnoreCase(name)).thenReturn(null);

        List <Message> actualMessages = messageServiceImp.getMessageBySenderName(name);
        assertNull(actualMessages);
    }

    @Test
    void testGetMessageByNameWithMessage(){
        Person person = new Person("hannah", "email");
        Message message = new Message("hi", person);
        when(mockMessageRepo.findMessagesBySenderNameIgnoreCase(person.getName())).thenReturn(List.of(message));
        List <Message> actual = messageServiceImp.getMessageBySenderName(person.getName());

        assertEquals(List.of(message), actual);
    }

    @Test
    void testAddMessageWithBothIDZero() throws JsonProcessingException {
        String message = TestConstants.BOTH_ID_ZERO;
        Message resultMessage = mapper.readValue(message, Message.class);
        assertEquals(0, resultMessage.getId());
        assertEquals(0, resultMessage.getSender().getId());
        messageServiceImp.updatePerson(resultMessage);

        verify(personRepo, times(0)).findPersonsByEmailIgnoreCase(resultMessage.getSender().getEmail());

    }

    @Test
    void testAddMessageWithBothIDNull() throws JsonProcessingException {
        String message = TestConstants.BOTH_ID_NULL;
        Message resultMessage = mapper.readValue(message, Message.class);
        assertNull(resultMessage.getId());
        assertNull(resultMessage.getSender().getId());
        messageServiceImp.updatePerson(resultMessage);

        verify(personRepo, times(1)).findPersonsByEmailIgnoreCase(resultMessage.getSender().getEmail());

    }
}