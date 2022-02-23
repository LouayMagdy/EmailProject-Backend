package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToCriteria implements MessageCriteria {
    private String toEmailAddress;
    private Message[] sent;
    private List<Message> filtered;

    public ToCriteria(String toEmailAddress, Message[] sent) {
        this.toEmailAddress = toEmailAddress;
        this.sent = sent;
        filtered = new ArrayList<>();
    }

    @Override
    public void setDomain(Message[] messages) {
        this.sent = messages;
    }
    private Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }
    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        filtered = new ArrayList<>();
        for (Message message : sent) {
            String toPattern = ".*" + toEmailAddress + ".*";
            Pattern pattern = Pattern.compile(toPattern);
            for(String user : message.getTo()){
                Matcher matcher = pattern.matcher(user);
                if(matcher.matches()) filtered.add(message.clone());
            }
        }
        return listToArray(filtered);
    }
}
