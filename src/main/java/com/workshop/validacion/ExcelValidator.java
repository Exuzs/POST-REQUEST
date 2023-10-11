package com.workshop.validacion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelValidator {
    public static boolean validateExcelRow(Row row) {
        if (row == null) {
            return false; // La fila no es válida
        }

        if (row.getPhysicalNumberOfCells() != 3) {
            return false; // Debe haber exactamente 3 celdas por fila
        }

        // Realizar validaciones específicas para las celdas en la fila
        Cell emailCell = row.getCell(0);
        Cell dobCell = row.getCell(1);
        Cell titleCell = row.getCell(2);

        if (emailCell == null || dobCell == null || titleCell == null) {
            return false; // Al menos una celda es nula
        }

        // Validar la celda de correo electrónico (usando una expresión regular)
        if (!validateEmail(emailCell.getStringCellValue())) {
            return false; // Correo electrónico no válido
        }

        // Validar la celda de fecha de nacimiento
        if (!validateDateOfBirth(dobCell.getStringCellValue())) {
            return false; // Fecha de nacimiento no válida
        }

        // Validar la celda de título del trabajo
        if (!validateJobTitle(titleCell.getStringCellValue())) {
            return false; // Título del trabajo no válido
        }

        return true; // La fila es válida
    }

    private static boolean validateEmail(String email) {
        // Validar el correo electrónico usando una expresión regular
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailPattern);
    }

    private static boolean validateDateOfBirth(String date) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = dateFormat.parse(date);
            Date minDate = dateFormat.parse("1980-01-01");
            return dob.after(minDate);
        } catch (ParseException e) {
            return false;
        }
    }

    private static boolean validateJobTitle(String title) {
        // Validar el título del trabajo
        String[] validJobTitles = {
                "Haematologist", "Phytotherapist", "Building surveyor",
                "Insurance account manager", "Educational psychologist"
        };

        for (String validTitle : validJobTitles) {
            if (title.equalsIgnoreCase(validTitle)) {
                return true;
            }
        }

        return false; // Título del trabajo no válido
    }
}
