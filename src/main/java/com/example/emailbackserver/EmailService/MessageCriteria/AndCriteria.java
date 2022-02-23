package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.*;
import java.util.stream.Collectors;


public class AndCriteria implements MessageCriteria{
    private List<MessageCriteria> criteria;
    private List<Message> filtered;
    private Message[] input;

    public AndCriteria(List<MessageCriteria> criteria) {
        input = new Message[0];
        filtered = new ArrayList<>();
        this.criteria = criteria;
    }

    private Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }
    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        for(MessageCriteria criterion: criteria){
            if(criteria.indexOf(criterion) > 0) {
                input = listToArray(filtered);
                criterion.setDomain(input.clone());
            }
            filtered.clear();
            Collections.addAll(filtered, criterion.meetCriteria());
            //getDuplicates(filtered);
        }
        return listToArray(filtered);
    }

    @Override
    public void setDomain(Message[] messages) {}
}
