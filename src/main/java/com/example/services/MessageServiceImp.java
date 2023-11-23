package com.example.services;

import com.example.dataaccess.MessageRepository;
import com.example.dataaccess.PersonRepository;
import com.example.entities.Message;
import com.example.entities.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImp implements MessageService{

    MessageRepository messageRepository;
    PersonRepository personRepository;

    @Autowired
    public MessageServiceImp(MessageRepository messageRepository, PersonRepository personRepository) {
        this.messageRepository = messageRepository;
        this.personRepository = personRepository;
    }

    public List<Message> findAll() {
        return this.messageRepository.findAll();
    }


    public Message getMessageById(long messageId) {
        Optional<Message> message = this.messageRepository.findById(messageId);
        return message.orElse(null);
    }

    public List<Message> getMessageBySenderEmail(String personEmail) {
        return messageRepository.findMessagesBySenderEmailIgnoreCase(personEmail);

    }

    public List<Message> getMessageBySenderName(String name) {
        return messageRepository.findMessagesBySenderNameIgnoreCase(name);
    }

    public Message addMessage(Message message) {
         updatePerson(message);
        return this.messageRepository.save(message);
    }

    public void updatePerson(Message message) {
        if (message.getId() != null && message.getId() != 0)
            throw new IllegalArgumentException("The message id provided for a create/post must be null or zero");
        boolean ans = checkIfSenderExists(message);
        if(!ans) {
            lookUpSenderId(message);
        }
    }

    public boolean checkIfSenderExists(Message message){
            if ((message.getSender().getId() == null) && (message.getSender().getEmail() != null) && (message.getSender().getName() != null)) {
                List<Person> listOfPeople = personRepository.findPersonsByEmailIgnoreCase(message.getSender().getEmail());
                if (!listOfPeople.isEmpty()) {
                    for (Person listOfPerson : listOfPeople) {
                        if (message.getSender().getName().equals(listOfPerson.getName())) {
                            message.setSender(listOfPerson);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        void lookUpSenderId(Message message){
            if (message.getSender().getId() != null && message.getSender().getId() != 0) {
                if (!this.personRepository.existsById(message.getSender().getId()))
                    throw new IllegalArgumentException("The person with ID " + message.getSender().getId() + " doesn't exist so illegal argument");
                else {
                    Optional<Person> optionalPerson = this.personRepository.findById(message.getSender().getId());
                    if (optionalPerson.isEmpty())
                        throw new IllegalArgumentException("Person has disappeared?");
                    else
                        message.setSender(optionalPerson.get());
                }
            } else
                this.personRepository.save(message.getSender());
        }
    }

