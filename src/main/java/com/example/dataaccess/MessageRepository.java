package com.example.dataaccess;

import com.example.entities.Message;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageRepository extends ListCrudRepository<Message, Long> {

    List<Message> findMessagesBySenderEmailIgnoreCase(String personEmail);

    List<Message> findMessagesBySenderNameIgnoreCase(String name);
}
