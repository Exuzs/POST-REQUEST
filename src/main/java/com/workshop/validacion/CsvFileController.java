package com.workshop.validacion;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api")
public class CsvFileController {

    @PostMapping("/upload-csv")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo CSV está vacío.");
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] record;
            List<String[]> validRecords = new ArrayList<>();
            List<String[]> invalidRecords = new ArrayList<>();

            while ((record = csvReader.readNext()) != null) {
                if (CsvValidator.validateCsvRecord(record)) {
                    validRecords.add(record);
                } else {
                    invalidRecords.add(record);
                }
            }
            return ResponseEntity.ok("Archivo CSV cargado y validado con éxito.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar el archivo CSV: " + e.getMessage());
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }
}
