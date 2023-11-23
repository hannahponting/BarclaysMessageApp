package com.example.integrationtests;

import com.example.TestConstants;
import com.example.entities.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:test-data.sql")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
public class MessageWithMockHttpRequestIT {
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGettingAllMessages() throws Exception {
        MvcResult result =
               this.mockMvc.perform(get("/messages"))
                       .andExpect(status().isOk())
                       .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                       .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();

        Message [] actualMessages = mapper.readValue(contentAsJson, Message[].class);
        assertEquals("First text message", actualMessages[0].getContent());
        assertEquals("Second text message", actualMessages[1].getContent());
        assertEquals("Third text message", actualMessages[2].getContent());
        assertEquals("Fourth text message", actualMessages[3].getContent());
    }

    @Test
    public void testGettingAllMessagesWithString() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get("/messages"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(TestConstants.EXPECTED_MESSAGE_JSON))
                        .andReturn();

    }

    @Test
    public void testGettingOneMessage() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get("/messages/1000"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Message actualMessages = mapper.readValue(contentAsJson, Message.class);
        assertEquals("First text message", actualMessages.getContent());

    }

    @Test
    public void testGettingOneMessagesWithString() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get("/messages/1000"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(TestConstants.EXPECTED_ONE_MESSAGE_JSON))
                        .andReturn();
    }

    @Test
    public void testFindMessageBySenderEmail() throws Exception {
        String senderEmail = "hannah@gmail.com";
        MvcResult result =
                this.mockMvc.perform(get("/messages/sender/email/"+senderEmail))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Message[] messages = mapper.readValue(contentAsJson, Message[].class);
        assertEquals(3, messages.length);
    }

    @Test
    public void testFindMessageBySenderName() throws Exception {
        String senderName = "Hannah";
        MvcResult result =
                this.mockMvc.perform(get("/messages/sender/name/"+senderName))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Message[] messages = mapper.readValue(contentAsJson, Message[].class);
        assertEquals(3, messages.length);
    }



   @Test
   void testAddMessageHappyPath() throws Exception {
       String json = """
               {
               "id": 0,
                   "sender": {
                     "id": 0,
                     "email": "Josh@joshuatree.com",
                     "name": "Josh"
                   },
                    "content": "Joshs first message"
                }""";

       MvcResult result =
               this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(json)
                       .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
               .andReturn();


       String resultJson = result.getResponse().getContentAsString();
       Message resultMessage = mapper.readValue(resultJson, Message.class);

       assertEquals("Josh", resultMessage.getSender().getName());
       assertEquals("Josh@joshuatree.com", resultMessage.getSender().getEmail());
       assertEquals("Joshs first message", resultMessage.getContent());

       Assertions.assertNotNull(resultMessage.getId());
       Assertions.assertTrue(resultMessage.getId()>0);
       Assertions.assertNotNull((resultMessage.getSender().getId()));
       Assertions.assertTrue(resultMessage.getSender().getId()>0);

   }

    @Test
    void testAddMessageIllegalID() throws Exception {
        String json = """
               {
               "id": 10,
                   "sender": {
                     "id": 0,
                     "email": "Josh@joshuatree.com",
                     "name": "Josh"
                   },
                    "content": "Joshs first message"
                }""";

        MvcResult result =
                this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }


    @Test
    void testAddMessageIllegalMessageID() throws Exception {
        String json = """
                {
                "id": 0,
                    "sender": {
                      "id": 10,
                      "email": "Josh@joshuatree.com",
                      "name": "Josh"
                    },
                     "content": "Joshs first message"
                 }""";

        MvcResult result =
                this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest())
                        .andReturn();

    }


    @Test
    void testAddMessageMissingIDNotExistingSender() throws Exception {
        String json = """
               {
                   "sender": {
                     "email": "Josh@joshuatree.com",
                     "name": "Josh"
                   },
                    "content": "Joshs first message"
                }""";

        MvcResult result =
                this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        Message resultMessage = mapper.readValue(resultJson, Message.class);

        assertEquals("Josh", resultMessage.getSender().getName());
        assertEquals("Josh@joshuatree.com", resultMessage.getSender().getEmail());
        assertEquals("Joshs first message", resultMessage.getContent());

        Assertions.assertNotNull(resultMessage.getId());
        Assertions.assertTrue(resultMessage.getId()>0);
        Assertions.assertNotNull((resultMessage.getSender().getId()));
        Assertions.assertTrue(resultMessage.getSender().getId()>0);

    }



    @Test
    void testAddMessageMissingIDExistingSender() throws Exception {
        String json = """
               {
                   "sender": {
                     "email": "hannah@gmail.com",
                    "name": "Hannah"
                   },
                    "content": "Joshs first message"
                }""";

        MvcResult result =
                this.mockMvc.perform(MockMvcRequestBuilders.post("/messages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        Message resultMessage = mapper.readValue(resultJson, Message.class);
        assertEquals("Hannah", resultMessage.getSender().getName());
        assertEquals("hannah@gmail.com", resultMessage.getSender().getEmail());
        assertEquals("Joshs first message", resultMessage.getContent());

        Assertions.assertNotNull(resultMessage.getId());
        assertEquals(1000, resultMessage.getSender().getId());
        Assertions.assertNotNull((resultMessage.getSender().getId()));
        Assertions.assertTrue(resultMessage.getSender().getId()>0);
    }

    @Test
    void testAddMessageWithIDExistingSender() throws Exception {
        String json = """
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
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isCreated())
                        .andReturn();

        String resultJson = result.getResponse().getContentAsString();
        Message resultMessage = mapper.readValue(resultJson, Message.class);
        assertEquals("Hannah", resultMessage.getSender().getName());
        assertEquals("hannah@gmail.com", resultMessage.getSender().getEmail());
        assertEquals("Joshs first message", resultMessage.getContent());

        Assertions.assertNotNull(resultMessage.getId());
        assertEquals(1000, resultMessage.getSender().getId());
        Assertions.assertNotNull((resultMessage.getSender().getId()));
        Assertions.assertTrue(resultMessage.getSender().getId()>0);
    }

}
