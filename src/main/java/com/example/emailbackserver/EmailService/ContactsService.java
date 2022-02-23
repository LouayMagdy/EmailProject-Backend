package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.User;
import com.example.emailbackserver.EmailService.UserCriteria.OrCriteria;
import com.example.emailbackserver.EmailService.UserCriteria.UserCriteria;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

@Service
public class ContactsService {
    private String contactEmail;
    private String staticPath = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users";
    private String contactsFilePath;
    private UserCriteria critera;

    public ContactsService() {}

    // optimize add existed user
    public boolean addContact(String userOwnerEmail, User userAdded){
        User[] contactArr = getContact(userOwnerEmail);
        for(int i=0; i < contactArr.length;i++) {
            if ((contactArr[i].getEmailAddress()).equals(userAdded.getEmailAddress()))
                return false;
        }
        Gson gson = new Gson();
        String serializedTemp;
        this.contactEmail = userOwnerEmail;
        this.contactsFilePath = this.staticPath + "/" + this.contactEmail + "/Contacts.json";
        JSONArray contacts = new JSONArray();
        serializedTemp = gson.toJson(ignoreFields(userAdded));
        if (readFile(this.contactsFilePath) != null) {
            contacts = readFile(this.contactsFilePath);
        }
        contacts.add(gson.fromJson(serializedTemp, JSONObject.class));
        writeFile(contacts,this.contactsFilePath);
        return true;
    }

    public User[] searchAllUsers(String userOwnerEmail,String filter){
        OrCriteria orCriteria = new OrCriteria(filter,filter);
        String serializedTemp;
        String usersPath = "C:\\Users\\HP\\Desktop\\louay\\programming2\\emailBackServer\\users\\UsersData.json";
        JSONArray allUsersJSON=readFile(usersPath);
        JSONObject temp;
        Gson gson = new Gson();
        User[] searchedUsers = new User[0];
        for(Object obj:allUsersJSON){
            temp = (JSONObject) obj;
            if(!temp.get("emailAddress").equals(userOwnerEmail)) {
                searchedUsers = Arrays.copyOf(searchedUsers, searchedUsers.length + 1);
                searchedUsers[searchedUsers.length - 1] = ignoreFields(gson.fromJson(temp.toJSONString(), User.class));
            }
        }
        return orCriteria.filter(searchedUsers);
    }

    // search for name or email or both only
    public User[] searchContact(String userOwnerEmail, String filter){
        String serializedTemp;
        this.contactEmail = userOwnerEmail;
        this.contactsFilePath = this.staticPath + "\\" + this.contactEmail +"\\"+ "Contacts.json";
        System.out.println(this.contactsFilePath);
        serializedTemp = readFile(this.contactsFilePath).toJSONString();
        GsonBuilder gson = new GsonBuilder();
        User[] contactsUsers = gson.create().fromJson(serializedTemp,User[].class);
        if(contactsUsers == null) return null;
        this.critera = new OrCriteria(filter,filter);
        return this.critera.filter(contactsUsers);
    }

    public boolean deleteContact(String userOwnerEmail, User userDeleted){
        String serializedTemp;
        this.contactEmail = userOwnerEmail;
        this.contactsFilePath = this.staticPath + "/" + this.contactEmail + "/Contacts.json";
        JSONArray contacts = readFile(this.contactsFilePath);
        Gson gson = new Gson();
        userDeleted = ignoreFields(userDeleted);
        serializedTemp = gson.toJson(userDeleted);
        JSONObject delUser = gson.fromJson(serializedTemp,JSONObject.class);
        if(readFile(this.contactsFilePath) == null)
            return false;
        int s = contacts.size();
        contacts.remove(delUser);
        writeFile(contacts, this.contactsFilePath);
        if(s == contacts.size())  return false;
        return true;

    }

    public User[] getContact(String userOwnerEmail){
        GsonBuilder gsonBuilder = new GsonBuilder();
        this.contactEmail = userOwnerEmail;
        this.contactsFilePath = this.staticPath + "/" + this.contactEmail + "/Contacts.json";
        JSONArray contacts = readFile(this.contactsFilePath);
        if(contacts == null)
            return new User[0];
        return gsonBuilder.create().fromJson(contacts.toJSONString(),User[].class);

    }

    public boolean editContact(String userOwnerEmail,String editedData){
        GsonBuilder gsonBuilder = new GsonBuilder();
        JSONObject temp = gsonBuilder.create().fromJson(editedData,JSONObject.class);
        String editedUserEmail = temp.get("emailAddress").toString();
        String editName = temp.get("name").toString();
        boolean edited = false;
        this.contactEmail = userOwnerEmail;
        this.contactsFilePath = this.staticPath + "\\" + this.contactEmail + "\\"+"Contacts.json";
        System.out.println(this.contactsFilePath);
        if(readFile(this.contactsFilePath) == null) return false;
        JSONArray contacts = readFile(this.contactsFilePath);
        for(Object o : contacts){
            JSONObject jsonObject = (JSONObject) o;
            if(jsonObject.get("emailAddress").equals(editedUserEmail)){
                jsonObject.put("name",editName);
                edited = true;
            }
        }
        writeFile(contacts,this.contactsFilePath);
        return edited;
    }


    public JSONArray readFile(String userFilePath){
        Object object = null;
        try{
            JSONParser parser = new JSONParser();
            FileReader reader = new FileReader(userFilePath);
            object = parser.parse(reader);
        }
        catch(Exception e){
            return null;
        }

        if(object != null)
            return (JSONArray) object;

        return null;
    }

    public void writeFile(JSONArray jsonArray, String userFilePath){

        Gson gson = new Gson();

        try{
            FileWriter file = new FileWriter(userFilePath);
            file.write(gson.toJson(jsonArray));
            file.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public User ignoreFields(User user){
        Customization customization = new Customization();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class,new Customization());
        String jsonStr = gsonBuilder.create().toJson(user);
        Gson gson = new Gson();
        User modifiedUser = gson.fromJson(jsonStr,User.class);
        return modifiedUser;
    }
}
