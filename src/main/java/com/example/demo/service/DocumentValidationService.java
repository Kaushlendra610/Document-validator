package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentValidationService {


    String validateDocument(MultipartFile file);
}
