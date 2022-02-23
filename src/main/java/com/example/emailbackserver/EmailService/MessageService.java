package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.Message;
import com.example.emailbackserver.EmailService.MessageCriteria.*;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class MessageService {

    private final UserFilesService filesService;

    @Autowired
    public MessageService(UserFilesService filesService) {
        this.filesService = filesService;
    }

    public void sendEmail(String message) throws IOException, CloneNotSupportedException {

        Message eMail = new GsonBuilder().create().fromJson(message, Message.class);
        System.out.println(eMail);
        Message[] sentArray = this.filesService.readMessageFile(eMail.getFrom(), "Sent");
        Message[] modifiedSent = new Message[sentArray.length + 1];
        for (int i = 0; i < sentArray.length; i++) modifiedSent[i] = sentArray[i].clone();
        modifiedSent[sentArray.length] = eMail.clone();
        filesService.updateMessageFile(eMail.getFrom(), modifiedSent, "Sent");

        for (String toEmailAddress : eMail.getTo()) {
            Message[] inboxArray = filesService.readMessageFile(toEmailAddress, "Inbox");
            Message[] modifiedInbox = new Message[inboxArray.length + 1];
            for (int i = 0; i < inboxArray.length; i++) {
                modifiedInbox[i] = inboxArray[i].clone();
            }
            modifiedInbox[inboxArray.length] = eMail.clone();
            filesService.updateMessageFile(toEmailAddress, modifiedInbox, "Inbox");
        }
    }


    public Message[] notFilteredMessageSearch(String searchedFor, String emailAddress)
            throws IOException, CloneNotSupportedException {

        MessageCriteria criteria1 = new ToCriteria(searchedFor, filesService.readMessageFile(emailAddress, "Sent"));
        MessageCriteria criteria2 = new FromCriteria(searchedFor, filesService.readMessageFile(emailAddress, "Inbox"));
        MessageCriteria criteria3 = new SubjectCriteria(searchedFor, filesService.getAllMessages(emailAddress));
        MessageCriteria criteria4 = new BodyCriteria(searchedFor, filesService.getAllMessages(emailAddress));
        MessageCriteria criteria5 = new DateCriteria(searchedFor, filesService.getAllMessages(emailAddress));

        MessageCriteria orCriteria = new OrCriteria(criteria1, criteria2, criteria3, criteria4, criteria5);
        return orCriteria.meetCriteria();
    }

    public Message[] filteredSearch(Message searchedFor, String emailAddress) throws IOException, CloneNotSupportedException {
        List<MessageCriteria> criteria = new ArrayList<>();
        if(searchedFor.getTo().length != 0){
            MessageCriteria toCriteria= new ToCriteria(searchedFor.getTo()[0], filesService.readMessageFile(emailAddress, "Sent"));
            criteria.add(toCriteria);
        }
        if(! searchedFor.getFrom().equals("")){
            MessageCriteria fromCriteria= new FromCriteria(searchedFor.getFrom(), filesService.readMessageFile(emailAddress, "Inbox"));
            criteria.add(fromCriteria);
        }
        if(! searchedFor.getBody().equals("")){
            MessageCriteria bodyCriteria= new BodyCriteria(searchedFor.getBody(), filesService.getAllMessages(emailAddress));
            criteria.add(bodyCriteria);
        }
        if(! searchedFor.getSubject().equals("")){
            MessageCriteria subjectCriteria= new SubjectCriteria(searchedFor.getSubject(), filesService.getAllMessages(emailAddress));
            criteria.add(subjectCriteria);
        }
        if(! searchedFor.getDate().equals("")){
            MessageCriteria dateCriteria= new DateCriteria(searchedFor.getDate(), filesService.getAllMessages(emailAddress));
            criteria.add(dateCriteria);
        }
        MessageCriteria andCriteria = new AndCriteria(criteria);
        return andCriteria.meetCriteria();
    }

    public void starMessage(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> starredMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setStarred(true);
                starredMessages.add(message);
                Message[] sent = filesService.readMessageFile(emailAddress, "Sent");
                Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
                for(Message sentMessage : sent){
                    if(Objects.equals(message.getiD(), sentMessage.getiD()))
                        sentMessage.setStarred(true);
                }
                for(Message inboxMessage : inbox){
                    if(Objects.equals(message.getiD(), inboxMessage.getiD()))
                        inboxMessage.setStarred(true);
                }
                filesService.updateMessageFile(emailAddress, sent, "Sent");
                filesService.updateMessageFile(emailAddress, inbox, "Inbox");
            }
        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(starredMessages)
                , "Starred");
    }
    public void unStar(String id, String emailAddress) throws IOException {
        Message[] starred = filesService.readMessageFile(emailAddress, "Starred");
        List<Message> starredModified = new ArrayList<>();
        Collections.addAll(starredModified, starred);
        int i = -1;
        for(Message message : starredModified){
            if(Objects.equals(message.getiD(), id)) i = starredModified.indexOf(message);
        }
        if(i != -1) starredModified.remove(i);
        filesService.updateMessageFile(emailAddress, filesService.listToArray(starredModified), "Starred");

        Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
        for(i = 0; i < inbox.length; i++)
            if(Objects.equals(inbox[i].getiD(), id)) inbox[i].setStarred(false);

        Message[] sent = filesService.readMessageFile(emailAddress, "Sent");
        for(i = 0; i < sent.length; i++)
            if(Objects.equals(sent[i].getiD(), id)) sent[i].setStarred(false);
        filesService.updateMessageFile(emailAddress, inbox, "Inbox");
        filesService.updateMessageFile(emailAddress, sent, "Sent");
    }

    public void makeMessageImportant(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> importantMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setImportant(true);
                importantMessages.add(message);

                Message[] sent = filesService.readMessageFile(emailAddress, "Sent");
                Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
                for(Message sentMessage : sent){
                    if(Objects.equals(message.getiD(), sentMessage.getiD()))
                        sentMessage.setImportant(true);
                }
                for(Message inboxMessage : inbox){
                    if(Objects.equals(message.getiD(), inboxMessage.getiD()))
                        inboxMessage.setImportant(true);
                }
                filesService.updateMessageFile(emailAddress, sent, "Sent");
                filesService.updateMessageFile(emailAddress, inbox, "Inbox");
            }
        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(importantMessages)
                , "Important");
    }
    public void makeMessageUnImportant(String id, String emailAddress) throws IOException {
        Message[] important = filesService.readMessageFile(emailAddress, "Important");
        List<Message> importantModified = new ArrayList<>();
        Collections.addAll(importantModified, important);
        int i = -1;
        for(Message message : importantModified){
            if(Objects.equals(message.getiD(), id)) i = importantModified.indexOf(message);
        }
        if(i != -1) importantModified.remove(i);
        filesService.updateMessageFile(emailAddress, filesService.listToArray(importantModified), "Important");

        Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
        for(i = 0; i < inbox.length; i++)
            if(Objects.equals(inbox[i].getiD(), id)) inbox[i].setImportant(false);

        Message[] sent = filesService.readMessageFile(emailAddress, "Sent");
        for(i = 0; i < sent.length; i++)
            if(Objects.equals(sent[i].getiD(), id)) sent[i].setImportant(false);
        filesService.updateMessageFile(emailAddress, inbox, "Inbox");
        filesService.updateMessageFile(emailAddress, sent, "Sent");
    }

    public void draftMessage(String message, String emailAddress) throws IOException, CloneNotSupportedException {
        Message eMail = new GsonBuilder().create().fromJson(message, Message.class);
        eMail.setDraft(true);
        Message[] draftArray = this.filesService.readMessageFile(eMail.getFrom(), "Draft");
        Message[] modifiedDraft = new Message[draftArray.length + 1];
        for (int i = 0; i < draftArray.length; i++) modifiedDraft[i] = draftArray[i].clone();
        modifiedDraft[draftArray.length] = eMail.clone();
        filesService.updateMessageFile(eMail.getFrom(), modifiedDraft, "Draft");
    }

    public void makeCustom(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        List<Message> customMessages = new ArrayList();
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for (Message message : allMessages) {
            if (idList.contains(message.getiD())) {
                message.setCustom(true);
                customMessages.add(message);

                Message[] sent = filesService.readMessageFile(emailAddress, "Sent");
                Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
                for(Message sentMessage : sent){
                    if(Objects.equals(message.getiD(), sentMessage.getiD()))
                        sentMessage.setCustom(true);
                }
                for(Message inboxMessage : inbox){
                    if(Objects.equals(message.getiD(), inboxMessage.getiD()))
                        inboxMessage.setCustom(true);
                }
                filesService.updateMessageFile(emailAddress, sent, "Sent");
                filesService.updateMessageFile(emailAddress, inbox, "Inbox");
            }

        }
        filesService.updateMessageFile(emailAddress, filesService.listToArray(customMessages),"Custom");
    }
    public void makeMessageUnCustom(String id, String emailAddress) throws IOException {
        Message[] custom = filesService.readMessageFile(emailAddress, "Custom");
        List<Message> customModified = new ArrayList<>();
        Collections.addAll(customModified, custom);
        int i = -1;
        for(Message message : customModified){
            if(Objects.equals(message.getiD(), id)) i = customModified.indexOf(message);
        }
        if(i != -1) customModified.remove(i);
        filesService.updateMessageFile(emailAddress, filesService.listToArray(customModified), "Custom");

        Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
        for(i = 0; i < inbox.length; i++)
            if(Objects.equals(inbox[i].getiD(), id)) inbox[i].setCustom(false);

        Message[] sent = filesService.readMessageFile(emailAddress, "Sent");
        for(i = 0; i < sent.length; i++)
            if(Objects.equals(sent[i].getiD(), id)) sent[i].setCustom(false);
        filesService.updateMessageFile(emailAddress, inbox, "Inbox");
        filesService.updateMessageFile(emailAddress, sent, "Sent");
    }

    public void deleteMessage(String[] id, String emailAddress) throws IOException {
        List<String> idList = new ArrayList<>();
        Collections.addAll(idList, id);
        Message[] allMessages = filesService.getAllMessages(emailAddress);
        for(Message message : allMessages){
            if(idList.contains(message.getiD())){
                if(Objects.equals(message.getFrom(), emailAddress))
                    filesService.removeMessageFromFile(emailAddress, message, "Sent");
                for(int i = 0; i < message.getTo().length; i++){
                    if(message.getTo()[i].equals(emailAddress))
                        filesService.removeMessageFromFile(emailAddress, message, "Inbox");
                }
                filesService.removeMessageFromFile(emailAddress, message, "Starred");
                filesService.removeMessageFromFile(emailAddress, message, "Important");
                filesService.removeMessageFromFile(emailAddress, message, "Draft");
                filesService.removeMessageFromFile(emailAddress, message, "Custom");

                message.setDeleteDate(System.currentTimeMillis());
                Message[] deleted = filesService.readMessageFile(emailAddress, "Trashed");
                List<Message> deletedModified = new ArrayList<>();
                Collections.addAll(deletedModified, deleted);
                deletedModified.add(message);
                filesService.updateMessageFile(emailAddress, filesService.listToArray(deletedModified),"Trashed");
                return;
            }
        }
    }

    public void readMessage(String id, String emailAddress) throws IOException {
        Message[] inbox = filesService.readMessageFile(emailAddress, "Inbox");
        for(Message message : inbox){
            if (Objects.equals(message.getiD(), id)){
                message.setRead(true);
                filesService.updateMessageFile(emailAddress, inbox, "Inbox");
                return ;
            }
        }
    }

}