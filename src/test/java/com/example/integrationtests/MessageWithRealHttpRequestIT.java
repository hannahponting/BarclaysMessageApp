package com.example.integrationtests;

import com.example.entities.Message;
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
public class MessageWithRealHttpRequestIT {

    ObjectMapper mapper = new ObjectMapper();

    @Disabled
    @Test
    public void testOfGetAllMessagesHttpReal() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/messages");
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request);
        Message [] actualMessages = mapper.readValue(response.getEntity().getContent(), Message[].class);
        assertEquals("First text message", actualMessages[0].getContent());
        assertEquals("Second text message", actualMessages[1].getContent());
        assertEquals("Third text message", actualMessages[2].getContent());
        assertEquals("Fourth text message", actualMessages[3].getContent());
    }

    @Disabled
    @Test
    public void testOfGetOneMessageHttpReal() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:8080/messages/1000");
        CloseableHttpResponse response = HttpClientBuilder.create().build().execute(request);
        Message actualMessage = mapper.readValue(response.getEntity().getContent(), Message.class);
        assertEquals("First text message", actualMessage.getContent());

    }


}

