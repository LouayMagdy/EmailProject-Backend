package com.example.emailbackserver.EmailController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/attachment")
public class AttachmentController {

    public AttachmentController(){

    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String downloadAttachment(@RequestParam("file") MultipartFile file) throws IOException {
        String path = "C:\\Users\\HP\\Desktop\\louay\\programming2\\EmailFront\\email-frontEnd\\src\\assets\\Attachments\\" + file.getOriginalFilename();
        File attachment = new File(path);
        attachment.createNewFile();
        try(FileOutputStream fout = new FileOutputStream(attachment)){
            fout.write(file.getBytes());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }
}
