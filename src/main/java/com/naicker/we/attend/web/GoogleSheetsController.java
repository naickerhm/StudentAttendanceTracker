package com.naicker.we.attend.web;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.api.services.sheets.v4.model.Sheet;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.naicker.we.attend.data.payloads.MessageResponse;
import com.naicker.we.attend.data.repository.StudentRepo;
import com.naicker.we.attend.service.GoogleSheetsServiceImpl;
import com.naicker.we.attend.service.StudentService;

import com.naicker.we.attend.config.GoogleAuthorizationConfig;


@RestController
public class GoogleSheetsController {

    @Value("${spreadsheet.id}")
    private String spreadsheetId;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    StudentService studentService;

    @Autowired
    private GoogleSheetsServiceImpl googleSheetsService;

    @Autowired
    private GoogleAuthorizationConfig googleAuthorizationConfig;

    @GetMapping("/loadTimesheet")
    public ResponseEntity<?> getTimeSheetData () throws IOException, GeneralSecurityException {
        MessageResponse sheetsResponse = googleSheetsService.loadTimesheet();
        return new ResponseEntity<>(sheetsResponse, HttpStatus.OK);
    }


    private List<String> getSpreadSheetRange() throws IOException, GeneralSecurityException {
        Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
        Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get("a");
        Spreadsheet spreadsheet = request.execute();
        Sheet sheet = spreadsheet.getSheets().get(0);
        int row = sheet.getProperties().getGridProperties().getRowCount();
        int col = sheet.getProperties().getGridProperties().getColumnCount();
        return Collections.singletonList("R1C1:R".concat(String.valueOf(row))
                .concat("C").concat(String.valueOf(col)));
    }
    
}
