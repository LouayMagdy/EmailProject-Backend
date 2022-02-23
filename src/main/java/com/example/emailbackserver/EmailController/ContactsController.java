package com.example.emailbackserver.EmailController;

import com.example.emailbackserver.EmailModel.User;
import com.example.emailbackserver.EmailService.ContactsService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/emailUser")
public class ContactsController {
    private ContactsService contactsService;
    private Gson gson = new Gson();

    @Autowired
    public ContactsController(ContactsService contactsService) {
        this.contactsService = contactsService;
    }

    @PostMapping("/searchUsers/{userEmailAddress}")
    public User[] searchAllUsers(@PathVariable String userEmailAddress, @RequestBody String searchedFor){
        return contactsService.searchAllUsers(userEmailAddress, searchedFor);
    }

    @GetMapping("/contacts/{userEmailAddress}")
    public User[] getContacts(@PathVariable String userEmailAddress){
        return contactsService.getContact(userEmailAddress);
    }

    @PostMapping("/searchContact/{userEmailAddress}")
    public User[] searchContact(@PathVariable String userEmailAddress, @RequestBody String searchedFor){
        return contactsService.searchContact(userEmailAddress,searchedFor);
    }

    @PostMapping("/addContact/{userEmailAddress}")
    public boolean addContact(@PathVariable String userEmailAddress, @RequestBody String userAdded){
        return contactsService.addContact(userEmailAddress, gson.fromJson(userAdded,User.class));
    }

    @PostMapping("/deleteContact/{userEmailAddress}")
    public boolean deleteContact(@PathVariable String userEmailAddress, @RequestBody String userDeleted){
        return contactsService.deleteContact(userEmailAddress, gson.fromJson(userDeleted,User.class));
    }

    @PostMapping("/editContact/{userEmailAddress}")
    public boolean editContact(@PathVariable String userEmailAddress,@RequestBody String editedData){
        System.out.println("start Editing");
        return contactsService.editContact(userEmailAddress, editedData);
    }

}
