package com.example.emailbackserver.EmailService.UserCriteria;

import com.example.emailbackserver.EmailModel.User;

public interface UserCriteria {
    User[] filter(User[] users);
}
