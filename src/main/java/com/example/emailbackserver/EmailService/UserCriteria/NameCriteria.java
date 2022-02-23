package com.example.emailbackserver.EmailService.UserCriteria;

import com.example.emailbackserver.EmailModel.User;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameCriteria implements UserCriteria{
    private String FilterName;
    private Matcher matcher;
    private Pattern pattern;

    public NameCriteria() {}

    public NameCriteria(String filterName) {
        FilterName = filterName;
        this.pattern = Pattern.compile(".*" + FilterName + ".*");
    }

    public void setFilterName(String filterName) {
        FilterName = filterName;
        this.pattern = Pattern.compile(".*" + FilterName + ".*");
    }

    public String getFilterName() {
        return FilterName;
    }

    @Override
    public User[] filter(User[] users){
        if(users == null) return null;
        User[] nameFiltered = new User[0];
        for(int i = 0; i < users.length; i++) {
            this.matcher = this.pattern.matcher(users[i].getName());
            if (this.matcher.matches()) {
                nameFiltered = Arrays.copyOf(nameFiltered,nameFiltered.length+1);
                nameFiltered[nameFiltered.length - 1] = users[i];
            }
        }
        return nameFiltered;
    }
}
