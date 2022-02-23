package com.example.emailbackserver.EmailController;

import com.example.emailbackserver.EmailModel.Message;
import com.example.emailbackserver.EmailService.MessageService;
import com.example.emailbackserver.EmailService.UserFilesService;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/emailMessage")
public class MessageController {
    private final MessageService messageService;
    private final UserFilesService fileService;
    @Autowired
    public MessageController(MessageService messageService, UserFilesService userFilesService) {
        this.fileService = userFilesService;
        this.messageService = messageService;
    }

    @PostMapping("/send")
    public void SendEmail(@RequestBody String message) throws IOException, CloneNotSupportedException {
        messageService.sendEmail(message);
    }

    @PostMapping("/noFilterSearch/{userEmail}")
    public Message[] nonFilterSearch(@RequestBody String expression, @PathVariable String userEmail)
            throws IOException, CloneNotSupportedException {
        return messageService.notFilteredMessageSearch(expression, userEmail);
    }

    @PostMapping("/filterSearch/{userEmail}")
   public Message[] filterSearch(@PathVariable String userEmail, @RequestBody String message) throws IOException, CloneNotSupportedException {
        System.out.println(message);
        Message filters = new GsonBuilder().create().fromJson(message, Message.class);
        return messageService.filteredSearch(filters, userEmail);
    }

    @PostMapping("/starMessage/{emailAddress}")
    public void starMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        for(int i = 0; i < id.length; i++){
            System.out.println(id[i]);
        }
        messageService.starMessage(id, emailAddress);
    }

    @PostMapping("/importantMessage/{emailAddress}")
    public void makeMessageImportant(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.makeMessageImportant(id, emailAddress);
    }

    @PostMapping("/customMessage/{emailAddress}")
    public void makeCustom(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.makeCustom(id, emailAddress);
    }

    @PostMapping("/draftMessage/{emailAddress}")
    public void draftMessage(@RequestBody String message, @PathVariable String emailAddress) throws IOException, CloneNotSupportedException {
        messageService.draftMessage(message, emailAddress);
    }

    @PostMapping("/deleteMessage/{emailAddress}")
    public void deleteMessage(@RequestBody String[] id, @PathVariable String emailAddress) throws IOException {
        messageService.deleteMessage(id, emailAddress);
    }

    @PostMapping("/readMessage/{emailAddress}")
    public void readMessage(@RequestBody String id, @PathVariable String emailAddress) throws IOException {
        messageService.readMessage(id, emailAddress);
    }
    @PostMapping("/getFile/{emailAddress}")
    public Message[] getMessagesFile(@RequestBody String fileName, @PathVariable String emailAddress) throws IOException {
        System.out.println("UUUUUUU: " + emailAddress);
        return fileService.readMessageFile(emailAddress, fileName);
    }
    @PostMapping("/unstarMessage/{emailAddress}")
    public void unstar(@RequestBody String id, @PathVariable String emailAddress) throws IOException {
        messageService.unStar(id, emailAddress);
    }
    @PostMapping("/makeUnImportant/{emailAddress}")
    public void makeUnImportant(@RequestBody String id, @PathVariable String emailAddress) throws IOException {
        messageService.makeMessageUnImportant(id, emailAddress);
    }
    @PostMapping("/makeUnCustom/{emailAddress}")
    public void makeUnCustom(@RequestBody String id, @PathVariable String emailAddress) throws IOException {
        messageService.makeMessageUnCustom(id, emailAddress);
    }


}
