package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyCriteria implements MessageCriteria{
    private String expression;
    private Message[] allMessages;
    private List<Message> filtered;

    public BodyCriteria(String expression, Message[] allMessages) {
        this.expression = expression;
        this.allMessages = allMessages;
        filtered = new ArrayList<>();
    }

    private Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }

    @Override
    public void setDomain(Message[] messages) {
        this.allMessages = messages;
    }
    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        filtered = new ArrayList<>();
        for (Message message : allMessages) {
            String expPattern = ".*" + expression + ".*";
            Pattern pattern = Pattern.compile(expPattern);
            Matcher matcher = pattern.matcher(message.getBody());
            if(matcher.matches()) filtered.add(message.clone());
        }
        return listToArray(filtered);
    }
}
