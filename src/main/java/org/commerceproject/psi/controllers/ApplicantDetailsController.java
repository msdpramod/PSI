package org.commerceproject.psi.controllers;

import org.commerceproject.psi.DTOS.ApplicantDetailsDto;
import org.commerceproject.psi.Models.ApplicantPageModels.ApplicantDetails;
import org.commerceproject.psi.Service.ApplicantDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/ApplicantProfile/ApplicantDetails")
public class ApplicantDetailsController {

    private final ApplicantDetailsService applicantDetailsService;

    @Value("${file.upload-dir}") // Ensure you have this property defined in your application.properties or application.yml
    private String uploadDirectory;

    public ApplicantDetailsController(ApplicantDetailsService applicantDetailsService) {
        this.applicantDetailsService = applicantDetailsService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicantDetails> getApplicantDetails(@PathVariable UUID id) {
        ApplicantDetails applicant = applicantDetailsService.getApplicantDetails(id);
        return ResponseEntity.ok(applicant);
    }

    @PostMapping
    public ResponseEntity<ApplicantDetails> createApplicantDetails(@RequestBody ApplicantDetails applicantDetails) {
        ApplicantDetails savedApplicant = applicantDetailsService.saveApplicantDetails(applicantDetails);
        return ResponseEntity.ok(savedApplicant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicantDetails> updateApplicantDetails(@PathVariable UUID id, @RequestBody ApplicantDetails updatedApplicantDetails) {
        ApplicantDetails updatedApplicant = applicantDetailsService.updateApplicantDetails(id, updatedApplicantDetails);
        return ResponseEntity.ok(updatedApplicant);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplicantDetails(@PathVariable UUID id) {
        applicantDetailsService.deleteApplicantDetails(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@Valid @ModelAttribute ApplicantDetailsDto applicantDetailsDto) {
        try {
            Path path = Paths.get(uploadDirectory);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            // Handling PAN card file upload
            MultipartFile panCardFile = applicantDetailsDto.getPanCardFile();
            if (panCardFile != null && !panCardFile.isEmpty()) {
                String panCardName = panCardFile.getOriginalFilename();
                Path panCardPath = Paths.get(uploadDirectory, panCardName);
                Files.write(panCardPath, panCardFile.getBytes());
                applicantDetailsDto.setPanCardFilePath(uploadDirectory + "/" + panCardName);
            }

            // Handling Aadhar card file upload
            MultipartFile aadharCardFile = applicantDetailsDto.getAadharCardFile();
            if (aadharCardFile != null && !aadharCardFile.isEmpty()) {
                String aadharCardName = aadharCardFile.getOriginalFilename();
                Path aadharCardPath = Paths.get(uploadDirectory, aadharCardName);
                Files.write(aadharCardPath, aadharCardFile.getBytes());
                applicantDetailsDto.setAadharCardFilePath(uploadDirectory + "/" + aadharCardName);
            }

            // Respond back with the path of uploaded files or a success message
            return ResponseEntity.ok("Files uploaded successfully.");
            }
            catch (Exception e) {
                return new ResponseEntity<>("File upload failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

    }
