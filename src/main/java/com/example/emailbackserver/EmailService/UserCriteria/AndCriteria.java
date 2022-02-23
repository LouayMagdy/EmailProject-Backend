package com.example.emailbackserver.EmailService.UserCriteria;

import com.example.emailbackserver.EmailModel.User;
import org.springframework.beans.factory.annotation.Autowired;

public class AndCriteria implements UserCriteria{

    private String FilterName;
    private String FilterEmail;
    private UserCriteria criteraName;
    private UserCriteria criteraEmail;

    @Autowired
    public AndCriteria(String filterEmail, String filterName) {
        FilterName = filterName;
        FilterEmail = filterEmail;
        this.criteraName = new NameCriteria(FilterName);
        this.criteraEmail = new EmailCriteria(FilterEmail);
    }

    public String getFilterName() {
        return FilterName;
    }

    public void setFilterName(String filterName) {
        FilterName = filterName;
    }

    public String getFilterEmail() {
        return FilterEmail;
    }

    public void setFilterEmail(String filterEmail) {
        FilterEmail = filterEmail;
    }

    @Override
    public User[] filter(User[] users){
        try {
            User[] FilteredAND = this.criteraName.filter(users);
            FilteredAND = this.criteraEmail.filter(FilteredAND);
            return FilteredAND;
        }
        catch (Exception e){
            return null;
        }
    }
}
