package com.workshop.validacion;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.apache.poi.ss.usermodel.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/api")
public class ExcelFileController {

    @PostMapping("/upload-excel")
    public ResponseEntity<String> uploadExcelFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo Excel está vacío.");
        }

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Row> validRows = new ArrayList<>();
            List<Row> invalidRows = new ArrayList<>();

            for (Row row : sheet) {
                if (ExcelValidator.validateExcelRow(row)) {
                    validRows.add(row);
                } else {
                    invalidRows.add(row);
                }
            }
            return ResponseEntity.ok("Archivo Excel cargado y validado con éxito.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error al procesar el archivo Excel: " + e.getMessage());
        }
    }
}
