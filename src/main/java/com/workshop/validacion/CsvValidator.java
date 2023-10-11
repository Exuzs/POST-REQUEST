package com.workshop.validacion;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CsvValidator {
    public static boolean validateCsvRecord(String[] record) {
        if (record.length != 3) {
            return false;
        }

        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!validateFieldWithPattern(record[0], emailPattern)) {
            return false;
        }

        if (!validateDateOfBirth(record[1])) {
            return false;
        }

        if (!validateJobTitle(record[2])) {
            return false;
        }

        return true;
    }

    private static boolean validateFieldWithPattern(String field, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(field);
        return matcher.matches();
    }

    private static boolean validateDateOfBirth(String date) {
        try {
            return date.compareTo("1980-01-01") > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean validateJobTitle(String title) {
        String[] validJobTitles = {
                "Haematologist", "Phytotherapist", "Building surveyor",
                "Insurance account manager", "Educational psychologist"
        };

        for (String validTitle : validJobTitles) {
            if (title.equalsIgnoreCase(validTitle)) {
                return true;
            }
        }

        return false;
    }
}
