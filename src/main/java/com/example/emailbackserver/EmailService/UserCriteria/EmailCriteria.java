package com.example.emailbackserver.EmailService.UserCriteria;

import com.example.emailbackserver.EmailModel.User;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailCriteria implements UserCriteria{
    private String FilterEmail;
    private Matcher matcher;
    private Pattern pattern;

    public EmailCriteria(String filterEmail) {
        FilterEmail = filterEmail;
        this.pattern = Pattern.compile(".*" + FilterEmail + ".*");
    }

    public String getFilterEmail() { return FilterEmail; }

    public void setFilterEmail(String filterEmail) {
        FilterEmail = filterEmail;
        this.pattern = Pattern.compile(".*" + FilterEmail + ".*");
    }

    @Override
    public User[] filter(User[] users) {
        if(users == null) return null;
        User[] emailFiltered = new User[0];
        for(int i = 0; i < users.length; i++){
            this.matcher = this.pattern.matcher(users[i].getEmailAddress());
            if(this.matcher.matches()){
                emailFiltered = Arrays.copyOf(emailFiltered,emailFiltered.length+1);
                emailFiltered[emailFiltered.length-1]= users[i];
            }
        }
        return emailFiltered;
    }
}
