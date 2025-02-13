package com.example.demo.serviceImpl;

import com.example.demo.service.DocumentValidationService;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DocumentValidationServiceImpl implements DocumentValidationService {

/// sdfsdfsd

    //https://github.com/UB-Mannheim/tesseract/wiki
    //download this Application
    private static final String TESSERACT_DATA_PATH = "C:/Program Files/Tesseract-OCR/tessdata";

    @Override
    public String validateDocument(MultipartFile file) {
        if (file.isEmpty()) {
            return "No file uploaded.";
        }

        String extractedText;
        String fileName = file.getOriginalFilename();

        try {
            File tempFile = File.createTempFile("uploaded_", fileName);
            file.transferTo(tempFile);

            if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                extractedText = extractTextFromPDF(tempFile);
            } else if (isImage(fileName)) {
                extractedText = extractTextFromImage(tempFile);
            } else {
                return "Unsupported file format.";
            }

            tempFile.delete();

        } catch (IOException e) {
            return "Error processing file: " + e.getMessage();
        }

        if (extractedText.isEmpty()) {
            return "No text extracted from the document.";
        }

        String aadhaarNumber = extractAadhaarNumber(extractedText);
        if (!aadhaarNumber.isEmpty()) {
            return "Aadhaar Number Detected: " + aadhaarNumber;
        }

        String panNumber = extractPANNumber(extractedText);
        if (!panNumber.isEmpty()) {
            return "PAN Number Detected: " + panNumber;
        }

        return "No Aadhaar or PAN number found in the document.";
    }

    private boolean isImage(String fileName) {
        return fileName != null && (fileName.toLowerCase().endsWith(".jpg") ||
                fileName.toLowerCase().endsWith(".png") ||
                fileName.toLowerCase().endsWith(".jpeg") ||
                fileName.toLowerCase().endsWith(".webp"));
    }

    private String extractTextFromPDF(File file) {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document).replace("\n", " ");
        } catch (IOException e) {
            return "";
        }
    }

    private String extractTextFromImage(File file) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSERACT_DATA_PATH);
        tesseract.setLanguage("eng");

        try {
            return tesseract.doOCR(file).replace("\n", " ");
        } catch (TesseractException e) {
            return "";
        }
    }

    private String extractAadhaarNumber(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{4} \\d{4} \\d{4}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().replace(" ", "");
        }
        return "";
    }

    private String extractPANNumber(String text) {
        Pattern pattern = Pattern.compile("\\b[A-Z]{5}[0-9]{4}[A-Z]\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }
}
