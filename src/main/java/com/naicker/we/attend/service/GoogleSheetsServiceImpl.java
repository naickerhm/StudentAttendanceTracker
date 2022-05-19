package com.naicker.we.attend.service;

import com.google.api.services.sheets.v4.Sheets;
import com.naicker.we.attend.config.GoogleAuthorizationConfig;
import com.naicker.we.attend.data.models.AttendanceData;
import com.naicker.we.attend.data.models.Student;
import com.naicker.we.attend.data.payloads.MessageResponse;
import com.naicker.we.attend.data.repository.AttendanceRepo;
import com.naicker.we.attend.data.repository.StudentRepo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class GoogleSheetsServiceImpl implements GoogleSheetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleSheetsServiceImpl.class);

    @Value("${spreadsheet.id}")
    private String spreadsheetId;

    @Autowired
    private GoogleAuthorizationConfig googleAuthorizationConfig;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;
    
    @Override
    public MessageResponse loadTimesheet(){
        String body = "";
        HttpStatus responseStatus = HttpStatus.OK;
        String sheetName = "Sheet1";
        String timesheetName = "Time Sheet (Time)";
        // String timesheetName = "Sheet1";
        String rangeDataToRead = "A1:A2";

        List<List<Object>> timeSheetData = new ArrayList<List<Object>>();

        try{
            Sheets sheetsService = googleAuthorizationConfig.getSheetsService();
            
            timeSheetData = sheetsService.spreadsheets().values()
                    .get(spreadsheetId,timesheetName)
                    .execute().getValues();

            processTimeSheetData(timeSheetData);

            body = "worked";
            responseStatus = HttpStatus.OK;
        } catch(IOException e) {
            body = "failed, io error: " + e.getMessage();
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        } catch(GeneralSecurityException e) {
            body = "failed, general sec error: " + e.getMessage();
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            e.printStackTrace();
        }
        return new MessageResponse(body);
    }


    private void processTimeSheetData(List<List<Object>> timeSheetData) {

        List<String> studentUsernames = getStudentUsernamesFromDB();
        HashMap<String, String> regexPatterns = getRegexPatterns();
        List<?> cells = new ArrayList<>();
        String usernameFromSheet ="";
        List<AttendanceData> allAttendance = new ArrayList<>();

        int row = 0;
        for(Object rowItem:timeSheetData){

            if (rowItem.getClass().isArray()) {
                cells = Arrays.asList((Object[])rowItem);
            } else if (rowItem instanceof Collection) {
                cells = new ArrayList<>((Collection<?>)rowItem);
            }

            // String usernameFromSheet ="";
            String date = "";
            
            int startCol = 0;
            int endCol = cells.size();

            for(int count=startCol; count < endCol; count++){
                String time = "";
                String cellValue = cells.get(count).toString();
                //get student from db and update them 
                if((stringMatchesRegex(cellValue, regexPatterns.get("usernameRegex"))) && (checkIfCellContainsUsername(studentUsernames, cellValue))){
                    usernameFromSheet = cellValue.substring(5);
                }
                //get date entry
                else if(stringMatchesRegex(cellValue, regexPatterns.get("dateRegex"))){
                    date = cellValue;
                }
                 //get time entry
                else if(stringMatchesRegex(cellValue, regexPatterns.get("timeRegex"))){
                    time = cellValue;
                }
                
                if(usernameFromSheet != "" && date != "" && time != ""){

                    AttendanceData newAttendanceEntry = createAttendanceData(usernameFromSheet, date, time);
                    allAttendance.add(newAttendanceEntry);
                }
                
            }
        }
        //add addendance data to database
        addAttDataToDB(allAttendance);

    }

    private void addAttDataToDB(List<AttendanceData> attendanceLogs) {
        attendanceRepo.saveAll(attendanceLogs);
    }


    private AttendanceData createAttendanceData(String usernameFromSheet, String date, String time) {
        LocalDateTime dateTime = createDateTimeFromStrings(date, time);
        Student currentStudent = studentRepo.findByUsername(usernameFromSheet);
        AttendanceData newAttendance = new AttendanceData(dateTime, currentStudent);
        // addAttDataToDB(newAttendance);
        return newAttendance;
    }


    private LocalDateTime createDateTimeFromStrings(String date, String time) {
        DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        LocalDateTime newDateTime = LocalDateTime.parse(date.replace("/","-")+" "+time, dtFormatter);

        return newDateTime;
    }


    private HashMap<String,String> getRegexPatterns(){
        
        HashMap<String, String> regexPatterns = new HashMap<String, String>();
        regexPatterns.put("usernameRegex","\\d{4} [a-z]*");
        // regexPatterns.put("dateRegex","\\d{2}\\d{2}\\d{4}");
        regexPatterns.put("dateRegex", "^\\d{2}/\\d{2}/\\d{4}$");
        regexPatterns.put("timeRegex","\\d{2}:\\d{2}");
        return regexPatterns;
    }


    private boolean stringMatchesRegex(String testWord, String regexPattern){
        boolean patternMatch = false;
        // Pattern pattern = Pattern.compile("\\d{4} [a-z]*");
        // Matcher m = pattern.matcher(testWord);
        if(Pattern.matches(regexPattern, testWord)){
            patternMatch = true;
        }
        return patternMatch;
    }


    private boolean checkIfCellContainsUsername(List<String> usernames, String cell){
        return usernames.stream().filter(k -> cell.contains(k)).collect(Collectors.toList()).size() > 0;
    }


    private List<String> getStudentUsernamesFromDB() {
        List<String> usernames = studentRepo.findUsernames();

        return usernames;
    }

}
