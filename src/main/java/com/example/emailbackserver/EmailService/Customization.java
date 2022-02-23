package com.example.emailbackserver.EmailService;

import com.example.emailbackserver.EmailModel.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class Customization implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User user, Type type, JsonSerializationContext jsonSerializationContext) {
        String firstField = "emailAddress";
        String secondField = "name";
        String thirdField = "loggedIn";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(firstField, user.getEmailAddress());
        jsonObject.addProperty(secondField, user.getName());
        jsonObject.addProperty(thirdField, user.isLoggedIn());
        return jsonObject;
    }
}
