package com.example.emailbackserver.EmailService.UserCriteria;

import com.example.emailbackserver.EmailModel.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrCriteria implements UserCriteria{
    private String FilterName;
    private String FilterEmail;
    private UserCriteria criteriaName;
    private UserCriteria criteriaEmail;

    public OrCriteria(String filterEmail, String filterName) {
        FilterName = filterName;
        FilterEmail = filterEmail;
        this.criteriaName = new NameCriteria(FilterName);
        this.criteriaEmail = new EmailCriteria(FilterEmail);
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
    public User[] filter(User[] users) {
        try {
            boolean found = false;
            User[] FilteredName = this.criteriaName.filter(users);
            User[] FilteredEmail = this.criteriaEmail.filter(users);
            List filterResult = new ArrayList<User>();
            filterResult.addAll(Arrays.asList(FilteredName));
            for(int i = 0; i < FilteredEmail.length; i++) {
                if (!filterResult.contains(FilteredEmail[i]))
                    filterResult.add(FilteredName[i]);
            }
            User[] result = new User[filterResult.size()];
            return (User[]) filterResult.toArray(result);
        }
        catch (Exception e){
            return null;
        }
    }
}
