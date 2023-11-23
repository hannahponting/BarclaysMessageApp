package com.example.controllers;

import com.example.TestUtilities;
import com.example.entities.Message;
import com.example.entities.Person;
import com.example.services.MessageServiceImp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
class MessageControllerTest {

    @MockBean
    MessageServiceImp messageServiceImp;


    @Autowired
    MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void testServiceCalledFor_getAllMessages() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages");
        mockMvc.perform(requestBuilder);

        verify(messageServiceImp, times(1)).findAll();
    }

    @Test
    void getAllMessagesTest() throws Exception {
        List<Message> messages = TestUtilities.getMessageList();
        when(messageServiceImp.findAll()).thenReturn(messages);

        ResultActions resultActions = this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept("application/json"))
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        Message[] actualMessage = mapper.readValue(contentAsString, Message[].class);
        assertEquals(3, actualMessage.length);
    }

    @Test
    void testServiceCallGetMessageById() throws Exception {
        Long messageId = 1000L;
        Message message = new Message("Hi");
        when(messageServiceImp.getMessageById(messageId)).thenReturn(message);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/"+messageId);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(messageServiceImp, times(1)).getMessageById(messageId);
    }

    @Test
    void testGetMessageByIdBadIndex() throws Exception {
        long messageId = 0;
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/messages/"+Long.toString(messageId));
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }




    @Test
    void testServiceCallGetMessageByEmail() throws Exception {
        String sender = "hannah@gmail.com";
        List<Message> messages = new ArrayList<Message>();
        Message message = new Message("Hi", new Person("Hannah", sender));
        messages.add(message);
        when(messageServiceImp.getMessageBySenderEmail(sender)).thenReturn(messages);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/sender/email/"+sender);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(messageServiceImp, times(1)).getMessageBySenderEmail(sender);
    }

    @Test
    void testGetMessageBySenderEmailBadIndex() throws Exception {
        String sender = "";
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/messages/sender/email/"+sender);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }



    @Test
    void testServiceCallGetMessageByName() throws Exception {
        String sender = "Dave";
        List<Message> messages = new ArrayList<Message>();
        Message message = new Message("Hi", new Person(sender, "dave@gmail.com"));
        messages.add(message);
        when(messageServiceImp.getMessageBySenderName(sender)).thenReturn(messages);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/messages/sender/name/"+sender);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn();

        verify(messageServiceImp, times(1)).getMessageBySenderName(sender);
    }

    @Test
    void testGetMessageBySenderNameBadIndex() throws Exception {
        String sender = "";
        MockHttpServletRequestBuilder requestBuilder =
                MockMvcRequestBuilders.get("/messages/sender/name/"+sender);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void testPostMessageWithSenderAlreadyExisting() throws Exception {
        String message = """
               {
                   "sender": {
                     "id":1000,
                     "email": "hannah@gmail.com",
                    "name": "Hannah"
                   },
                    "content": "Joshs first message"
                }""";

        MvcResult result =
                this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(message)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        verify(messageServiceImp, times(1)).addMessage(any());


    }



}