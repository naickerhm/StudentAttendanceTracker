package com.naicker.we.attend.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;


import com.naicker.we.attend.data.models.Student;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERs = { "Username", "First Name", "Last Name ", "Campus" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }



    public static List<Student> csvToStudents(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.EXCEL.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<Student> students = new ArrayList<Student>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Student student = new Student(
                        csvRecord.get("Username"),
                        csvRecord.get("First Name"),
                        csvRecord.get("Last Name"),
                        csvRecord.get("Campus"),
                        csvRecord.get("Cohort"),
                        csvRecord.get("Email"));
                        
                students.add(student);
            }
            return students;

        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        } catch (Exception e){
            System.out.println("THE ERROR MESSAGE IS: "+e.getMessage());
        }
        return null;
    }

}

