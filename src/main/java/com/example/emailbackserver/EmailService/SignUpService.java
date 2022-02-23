package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

@Service
public class SignUpService {
    private String usersDataFilePath;
    private String eachUserPath;
    private User[] currentUsers;

    public SignUpService() throws IOException {
        this.usersDataFilePath = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users";
        createFile(usersDataFilePath, "directory");

        this.usersDataFilePath = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users\\UsersData.json";
        this.eachUserPath = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users\\";
        currentUsers = new User[0];

        createFile(usersDataFilePath, "file");
    }

    private void createFile(String path, String type) throws IOException {
        if(!checkFileExistence(path)){
            File file = new File(path);
            if(Objects.equals(type, "file")) file.createNewFile();
            else if(Objects.equals(type, "directory")) file.mkdir();
        }
    }
    private boolean checkFileExistence(String path){
        File file = new File(path);
        return file.exists();
    }

    private User[] loadUsersData() throws IOException {
        File file = new File(usersDataFilePath);
        FileReader fileReader = new FileReader(usersDataFilePath);
        JSONParser jsonParser = new JSONParser();
        Object read;
        try {
            read = jsonParser.parse(fileReader);
        }
        catch (Exception e){
            read = "";
        }

        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if(read instanceof JSONObject){
            jsonObject = (JSONObject) read;
            currentUsers = new User[] { new Gson().fromJson(jsonObject.toJSONString(), User.class) };
        }
        else if(read instanceof JSONArray){
            jsonArray = (JSONArray) read;
            GsonBuilder gsonBuilder = new GsonBuilder();
            currentUsers = gsonBuilder.setPrettyPrinting().create().fromJson(jsonArray.toJSONString(), User[].class);
        }
        else currentUsers = new User[0];
        return currentUsers;
    }
    private User deserialize(String newUser){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.setPrettyPrinting().create().fromJson(newUser, User.class);
    }

    private void register(User newUser, User[] currentUsers) throws CloneNotSupportedException, IOException {
        User[] currentUserModified = new User[currentUsers.length + 1];
        for(int i = 0; i < currentUsers.length; i++){
            currentUserModified[i] = currentUsers[i].clone();
        }
        currentUserModified[currentUsers.length] = newUser.clone();
        String currentUsersJson = new Gson().toJson(currentUserModified);

        File file = new File(usersDataFilePath);
        FileWriter writer = new FileWriter(file);
        writer.write(currentUsersJson);
        writer.flush();
        writer.close();

        createFile(eachUserPath+newUser.getEmailAddress(),"directory");
        String eachPath = eachUserPath;
        eachUserPath += newUser.getEmailAddress() + "\\";
        createFile(eachUserPath + "Inbox.json", "file");
        createFile(eachUserPath + "Sent.json", "file");
        createFile(eachUserPath + "Starred.json", "file");
        createFile(eachUserPath + "Important.json", "file");
        createFile(eachUserPath + "Draft.json", "file");
        createFile(eachUserPath + "Trashed.json", "file");
        createFile(eachUserPath + "Contacts.json", "file");
        createFile(eachUserPath + "Custom.json", "file");
        eachUserPath = eachPath;
    }
    public boolean isRegistered(String newUser) throws IOException, CloneNotSupportedException {
        boolean repeated = false;
        User parsedNewUser = deserialize(newUser);
        if(Objects.equals(parsedNewUser.getName(), "")) return true;
        currentUsers = loadUsersData();
        for (User currentUser : currentUsers) {
            System.out.println("stored:" + currentUser.getEmailAddress() + "...");
            System.out.println("new:" + parsedNewUser.getEmailAddress() + "...");
            if (Objects.equals(currentUser.getEmailAddress(), parsedNewUser.getEmailAddress()))
                repeated = true;
        }
        System.out.println("repeated: " + repeated + "...");
        if(!repeated) register(parsedNewUser, currentUsers);
        return repeated;
    }
}
