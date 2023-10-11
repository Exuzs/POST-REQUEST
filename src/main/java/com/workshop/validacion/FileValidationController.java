package com.workshop.validacion;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.*;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

    @RestController
    @RequestMapping("/api")
    public class FileValidationController {

        @Autowired
        private CsvValidator csvValidator;

        @Autowired
        private ExcelValidator excelValidator;

        @PostMapping("/upload")
        public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("El archivo está vacío.");
            }

            try {
                if (file.getOriginalFilename().toLowerCase().endsWith(".csv")) {
                    return processCsvFile(file);
                } else if (file.getOriginalFilename().toLowerCase().endsWith(".xlsx")) {
                    return processExcelFile(file);
                } else {
                    return ResponseEntity.badRequest().body("Formato de archivo no admitido.");
                }
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Error al procesar el archivo: " + e.getMessage());
            }
        }

        private ResponseEntity<String> processCsvFile(MultipartFile file) throws IOException {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
                String[] record;
                int validCount = 0;
                int invalidCount = 0;

                while ((record = csvReader.readNext()) != null) {
                    if (csvValidator.validateCsvRecord(record)) {
                        validCount++;
                    } else {
                        invalidCount++;
                    }
                }

                return ResponseEntity.ok("Registros válidos: " + validCount + ", Registros inválidos: " + invalidCount);
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }
        }

        private ResponseEntity<String> processExcelFile(MultipartFile file) throws IOException {
            try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
                Sheet sheet = workbook.getSheetAt(0);
                int validCount = 0;
                int invalidCount = 0;

                for (Row row : sheet) {
                    if (excelValidator.validateExcelRow(row)) {
                        validCount++;
                    } else {
                        invalidCount++;
                    }
                }

                return ResponseEntity.ok("Registros válidos: " + validCount + ", Registros inválidos: " + invalidCount);
            }
        }
    }

