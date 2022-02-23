package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.ArrayList;
import java.util.List;

public class OrCriteria implements MessageCriteria {
    private MessageCriteria criteria1;
    private MessageCriteria criteria2;
    private MessageCriteria criteria3;
    private MessageCriteria criteria4;
    private MessageCriteria criteria5;

    public OrCriteria(MessageCriteria criteria1, MessageCriteria criteria2,
                           MessageCriteria criteria3, MessageCriteria criteria4, MessageCriteria criteria5) {
        this.criteria1 = criteria1;
        this.criteria2 = criteria2;
        this.criteria3 = criteria3;
        this.criteria4 = criteria4;
        this.criteria5 = criteria5;
    }
    private Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }
    @Override
    public void setDomain(Message[] messages) {}

    @Override
    public Message[] meetCriteria() throws CloneNotSupportedException {
        Message[] filtered1 = criteria1.meetCriteria();
        Message[] filtered2 = criteria2.meetCriteria();
        Message[] filtered3 = criteria3.meetCriteria();
        Message[] filtered4 = criteria4.meetCriteria();
        Message[] filtered5 = criteria5.meetCriteria();

        List <Message> outPut = new ArrayList<>();
        for(Message message : filtered1) if(!outPut.contains(message)) outPut.add(message);
        for(Message message : filtered2) if(!outPut.contains(message)) outPut.add(message);
        for(Message message : filtered3) if(!outPut.contains(message)) outPut.add(message);
        for(Message message : filtered4) if(!outPut.contains(message)) outPut.add(message);
        for(Message message : filtered5) if(!outPut.contains(message)) outPut.add(message);

        return listToArray(outPut);
    }
}
