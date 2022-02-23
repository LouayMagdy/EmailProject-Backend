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
public class LoggerService {
    private User[] currentUsers;
    private String usersDataFilePath ;

    public LoggerService() {
        this.usersDataFilePath = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users\\UsersData.json";
    }
    private User deserialize(String newUser){
        GsonBuilder gsonBuilder = new GsonBuilder();
        return gsonBuilder.setPrettyPrinting().create().fromJson(newUser, User.class);
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
    private void writeInToUserFile() throws IOException {
        String currentUsersJson = new Gson().toJson(currentUsers);
        File file = new File(usersDataFilePath);
        FileWriter writer = new FileWriter(file);
        writer.write(currentUsersJson);
        writer.flush();
        writer.close();
    }


    public String logINCheck(String user) throws IOException {
        String status = "notFound";
        User parsedNewUser = deserialize(user);
        currentUsers = loadUsersData();
        for (User currentUser : currentUsers) {
            if (Objects.equals(currentUser.getEmailAddress(), parsedNewUser.getEmailAddress())) {
                if (Objects.equals(currentUser.isLoggedIn(), true)) status = "loggedIn";
                else if (Objects.equals(currentUser.getPassword(), parsedNewUser.getPassword())) {
                    status = "welcome";
                    currentUser.setLoggedIn(true);
                    writeInToUserFile();
                } else status = "incorrectData";
                break;
            }
        }
        return status;
    }
    public void logOutCheck(String emailAddress) throws IOException {
        currentUsers = loadUsersData();
        for (User currentUser : currentUsers) {
            System.out.println(currentUser.getEmailAddress());
            if (Objects.equals(currentUser.getEmailAddress(), emailAddress)) {
                currentUser.setLoggedIn(false);
                writeInToUserFile();
                return;
            }
        }
    }
}


