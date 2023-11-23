package com.example.controllers;

import com.example.entities.Message;
import com.example.entities.Person;
import com.example.services.MessageServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    MessageServiceImp messageServiceImp;

    @Autowired
    public MessageController(MessageServiceImp messageServiceImp) {
        this.messageServiceImp = messageServiceImp;
    }

    @GetMapping("")
    public List<Message> getAllMessages() {
        return messageServiceImp.findAll();
    }

    @GetMapping("/{messageId}")
    public Message getMessageById(@PathVariable long messageId) {
        Message message = messageServiceImp.getMessageById(messageId);
        if (message == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return message;
    }

    @GetMapping("/sender/email/{email}")
    public List<Message> getMessagesBySenderEmail(@PathVariable String email){
       List<Message> message = messageServiceImp.getMessageBySenderEmail(email);
        if (message.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return message;
    }

    @GetMapping("/sender/name/{name}")
    public List<Message> getMessagesBySenderName(@PathVariable String name){
        List<Message> message = messageServiceImp.getMessageBySenderName(name);
        if (message.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return message;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message addMessage(@RequestBody Message message){
        Message newMessage;
        try{
            newMessage = messageServiceImp.addMessage(message);
        }catch(IllegalArgumentException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return newMessage;
    }





    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @GetMapping("/teapot")
    void teaPot() {
    }

}

