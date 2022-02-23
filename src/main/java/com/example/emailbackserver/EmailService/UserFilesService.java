package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class UserFilesService {
    private final String path ;
    private JSONParser parser;

    public UserFilesService() {
        this.path = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users\\";
    }

    private Message[] messageParser(FileReader reader){
        parser = new JSONParser();
        Object read;
        try{read = parser.parse(reader);}
        catch (Exception e){return new Message[]{};}
        JSONArray jsonArray = (JSONArray) read;
        return new GsonBuilder().create().fromJson(jsonArray.toJSONString(), Message[].class);
    }
    private void writer(String filePath, String toBeTyped) throws IOException {
        File file = new File(filePath);
        FileWriter writer = new FileWriter(file);
        writer.write(toBeTyped);
        writer.flush();
        writer.close();
    }

    public Message[] listToArray(List<Message> messages){
        Message[] temp = new Message[messages.size()];
        temp = messages.toArray(temp);
        return temp;
    }

    public Message[] readMessageFile(String userEmailAddress, String fileName) throws IOException {
        String inboxPath = this.path + userEmailAddress + "\\" + fileName + ".json";
        if(Objects.equals(fileName, "Trashed")){
            long deadline = 1000 * 3600 * 24 * 30;
            Message[] trashed = messageParser(new FileReader(inboxPath));
            List<Message> trashedModified = new ArrayList<>();
            Collections.addAll(trashedModified, trashed);
            int[] indices = new int[trashedModified.size()];
            for(int i = 0; i < indices.length; i++) indices[i] = 0;
            for(Message message: trashedModified){
                if(System.currentTimeMillis() - message.getDeleteDate() > deadline)
                    indices[trashedModified.indexOf(message)] = 1;
            }
            for(int i = 0; i < indices.length; i++)
                if(indices[i] == 1) trashedModified.remove(i);
            trashed = listToArray(trashedModified);
            updateMessageFile(userEmailAddress, trashed, "Trashed");
            return trashed;
        }
        return messageParser(new FileReader(inboxPath));
    }

    private Message[] removeDuplicates(List<Message> duplicated){
        for(int i = 0; i< duplicated.size() - 1; i++){
            for(int j = i+1; j< duplicated.size(); j++){
                if(duplicated.get(i).equals(duplicated.get(j))) duplicated.remove(j);
            }
        }
        return listToArray(duplicated);
    }


    public Message[] getAllMessages(String userEmailAddress) throws IOException {
        List<Message> allMessages= new ArrayList<>();

        Message[] gotMessages = readMessageFile(userEmailAddress, "Inbox");
        Collections.addAll(allMessages, gotMessages.clone());

        gotMessages = readMessageFile(userEmailAddress, "Sent");
        Collections.addAll(allMessages, gotMessages.clone());

        gotMessages = readMessageFile(userEmailAddress, "Trashed").clone();
        Collections.addAll(allMessages, gotMessages);

        if(allMessages.isEmpty()) return new Message[]{};
        return  removeDuplicates(allMessages);
    }


    public void updateMessageFile(String userEmailAddress, Message[] eMail, String fileName) throws IOException {
        String filePath = this.path + userEmailAddress + "\\" +fileName+".json";
        String jsonEmail = new Gson().toJson(eMail);
        writer(filePath, jsonEmail);
    }
    public void removeMessageFromFile(String userEmailAddress, Message message,String fileName) throws IOException {
        Message[] messages = readMessageFile(userEmailAddress, fileName);

        List<Message> messagesModified = new ArrayList<>();
        Collections.addAll(messagesModified, messages);

        messagesModified.remove(message);

        updateMessageFile(userEmailAddress, listToArray(messagesModified), fileName);
    }
}
