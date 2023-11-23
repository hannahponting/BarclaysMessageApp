package com.example.integrationtests;

import com.example.entities.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("classpath:test-data.sql")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.sql.init.mode=never"})
public class PersonWithRealHttpRequestIT {

    ObjectMapper mapper = new ObjectMapper();
    @Disabled
    @Test
    public void testOfGetAllPeopleRealHttp() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/people");
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request);
        Person[] actualPeople = mapper.readValue(response.getEntity().getContent(), Person[].class);
        assertEquals("Hannah", actualPeople[0].getName());
        assertEquals("Hannah", actualPeople[1].getName());
        assertEquals("Hannah", actualPeople[2].getName());
        assertEquals("Hannah", actualPeople[3].getName());
    }

    @Disabled
    @Test
    public void testOfGetOnePersonRealHttp() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/people/1000");
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request);
        Person actualPeople = mapper.readValue(response.getEntity().getContent(), Person.class);
        assertEquals("Hannah", actualPeople.getName());

    }

}
