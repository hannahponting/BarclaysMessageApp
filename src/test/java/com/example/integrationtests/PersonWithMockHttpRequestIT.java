package com.example.integrationtests;

import com.example.TestConstants;
import com.example.entities.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:test-data.sql")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
public class PersonWithMockHttpRequestIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getPeopleTestWithArray() throws Exception {
         MvcResult result =
                this.mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

                String contentAsJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Person[] actualPeople = mapper.readValue(contentAsJson, Person[].class);
        assertEquals("Hannah", actualPeople[0].getName());
        assertEquals("Hannah2", actualPeople[1].getName());
        assertEquals("Hannah3", actualPeople[2].getName());
        assertEquals("Hannah4", actualPeople[3].getName());
    }

    @Test
    void getOnePerspnTestWithObject() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get("/people/1000"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andReturn();

        String contentAsJson = result.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        Person person = mapper.readValue(contentAsJson, Person.class);
        assertEquals("Hannah", person.getName());
    }

    @Test
    void getPeopleTestWithString() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get("/people"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(TestConstants.EXPECTED_PERSON_JSON))
                        .andReturn();

    }

    @Test
    void getOnePeopleTestWithString() throws Exception {
        MvcResult result =
                this.mockMvc.perform(get("/people/1000"))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().json(TestConstants.EXPECTED_ONE_PERSON_JSON))
                        .andReturn();
    }
}
