package com.example.demo.controller;

import com.example.demo.service.DocumentValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentValidationController {


    @Autowired
    private DocumentValidationService documentValidationService;

    @PostMapping("/validate")
    public ResponseEntity<String> validateDocument(@RequestParam("file") MultipartFile file) {
        String result = documentValidationService.validateDocument(file);
        return ResponseEntity.ok(result);
    }
}
