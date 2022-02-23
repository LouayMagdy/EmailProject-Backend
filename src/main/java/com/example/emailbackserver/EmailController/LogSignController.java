package com.example.emailbackserver.EmailController;


import com.example.emailbackserver.EmailService.LoggerService;
import com.example.emailbackserver.EmailService.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/emailLogger")
public class LogSignController {
    private final SignUpService signUpService;
    private final LoggerService loggerService;

    @Autowired
    public LogSignController(SignUpService signUpService, LoggerService loggerService) {
        this.signUpService = signUpService;
        this.loggerService = loggerService;
    }

    @PostMapping("/signUp")
    public boolean signUp(@RequestBody String user) throws IOException, CloneNotSupportedException {
        System.out.println("2"+user);
        return signUpService.isRegistered(user);
    }

    @PostMapping("/logIn")
    public String login(@RequestBody String user) throws IOException {
         return loggerService.logINCheck(user);
    }

    @PostMapping("/logOut")
    public void logOut(@RequestBody String emailAddress) throws IOException {
        loggerService.logOutCheck(emailAddress);
    }

}



