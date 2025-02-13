package com.example.demo;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentValidator {

    private static final String TESSERACT_DATA_PATH = "C:/Program Files/Tesseract-OCR/tessdata";

    public static void main(String[] args) {
        String filePath = "D:/a2.pdf";
        extractAndPrintAadhaarNumber(filePath);
    }

    public static void extractAndPrintAadhaarNumber(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File does not exist!");
            return;
        }

        String extractedText = extractTextFromImage(filePath);
        if (extractedText.isEmpty()) {
            System.out.println("No text extracted from the image.");
            return;
        }

        String aadhaarNumber = extractAadhaarNumber(extractedText);
        if (!aadhaarNumber.isEmpty()) {
            System.out.println("Aadhaar Number Detected: " + aadhaarNumber);
        } else {
            System.out.println("No Aadhaar Number Found in the Image.");
        }
    }

    private static String extractTextFromImage(String imagePath) {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(TESSERACT_DATA_PATH);
        tesseract.setLanguage("eng");

        try {
            return tesseract.doOCR(new File(imagePath)).replace("\n", " ");
        } catch (TesseractException e) {
            System.out.println("Error during OCR: " + e.getMessage());
            return "";
        }
    }

    private static String extractAadhaarNumber(String text) {
        Pattern pattern = Pattern.compile("\\b\\d{4} \\d{4} \\d{4}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().replace(" ", "");
        }
        return "";
    }
}
