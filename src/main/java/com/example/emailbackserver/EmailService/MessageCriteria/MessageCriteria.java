package com.example.emailbackserver.EmailService.MessageCriteria;

import com.example.emailbackserver.EmailModel.Message;

import java.util.List;

public interface MessageCriteria {
    Message[] meetCriteria() throws CloneNotSupportedException;
    void setDomain(Message[] messages);
}
