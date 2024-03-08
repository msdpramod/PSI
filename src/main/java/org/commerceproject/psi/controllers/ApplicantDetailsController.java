package org.commerceproject.psi.controllers;

import org.commerceproject.psi.Models.ApplicantDetails;
import org.commerceproject.psi.Service.ApplicantDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/ApplicantProfile/ApplicantDetails")
public class ApplicantDetailsController {

    private final ApplicantDetailsService applicantDetailsService;

    // Assume you have a service that performs CRUD operations on ApplicantDetails
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
}