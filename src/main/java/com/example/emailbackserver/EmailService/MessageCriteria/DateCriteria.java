package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateCriteria implements MessageCriteria{
    private String date;
    private Message[] allMessages;
    private List<Message> filtered;

    public DateCriteria(String date, Message[] allMessages) {
        this.date = date;
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
            String datePattern = ".*" + date + ".*";
            Pattern pattern = Pattern.compile(datePattern);
            Matcher matcher = pattern.matcher(message.getDate());
            if(matcher.matches()) filtered.add(message.clone());
        }
        return listToArray(filtered);
    }
}
