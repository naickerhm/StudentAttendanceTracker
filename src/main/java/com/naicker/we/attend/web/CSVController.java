package com.naicker.we.attend.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.naicker.we.attend.data.models.Student;
import com.naicker.we.attend.data.payloads.MessageResponse;
import com.naicker.we.attend.helper.CSVHelper;
import com.naicker.we.attend.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.WebUtils;


@CrossOrigin("http://localhost:8081")
@Controller
// @RequestMapping("/api/csv")
public class CSVController {

    @Autowired
    CSVService fileService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> uploadFile(@RequestParam("file") MultipartFile file, HttpEntity<String> httpEntity) {
        MultiValueMap<String, String> headers = httpEntity.getHeaders();
        Iterator<Map.Entry<String, List<String>>> s = headers.entrySet().iterator();
        List<String> value = null;
        while(s.hasNext()) {
            Map.Entry<String, List<String>> obj = s.next();
            String key = obj.getKey();
            value = obj.getValue();
        }

        String body = httpEntity.getBody();
 
        System.out.println("Uploaded file" + file.getOriginalFilename());
        String message = "";

        if (CSVHelper.hasCSVFormat(file)) {
            try {
                fileService.save(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageResponse(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(message));
    }

    @GetMapping(value = "/uploadStudents")
    public String uploadStudentsFromFormSub(){
        return "uploadStudents";
    }

}
