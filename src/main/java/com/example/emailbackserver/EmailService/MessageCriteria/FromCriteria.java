package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;
import org.apache.catalina.authenticator.SavedRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FromCriteria implements MessageCriteria{
    private String fromEmailAddress;
    private Message[] inbox;
    private List<Message> filtered;

    public FromCriteria(String fromEmailAddress, Message[] inbox) {
        this.fromEmailAddress = fromEmailAddress;
        this.inbox = inbox;
        filtered = new ArrayList<>();
    }
    private Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }
    @Override
    public void setDomain(Message[] messages) {
        this.inbox = messages;
    }
    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        filtered = new ArrayList<>();
        for (Message message : inbox) {
            String fromPattern = ".*" + fromEmailAddress + ".*";
            Pattern pattern = Pattern.compile(fromPattern);
            Matcher matcher = pattern.matcher(message.getFrom());
            if(matcher.matches()) filtered.add(message.clone());
        }
        return listToArray(filtered);
    }
}
