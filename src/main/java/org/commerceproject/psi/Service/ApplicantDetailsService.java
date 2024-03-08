package org.commerceproject.psi.Service;

import jakarta.transaction.Transactional;
import org.commerceproject.psi.Exceptions.ResourceNotFoundException;
import org.commerceproject.psi.Models.ApplicantDetails;
import org.commerceproject.psi.Repository.ApplicantDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApplicantDetailsService {
    private final ApplicantDetailsRepository applicantDetailsRepository;

    @Autowired
    public ApplicantDetailsService(ApplicantDetailsRepository applicantDetailsRepository) {
        this.applicantDetailsRepository = applicantDetailsRepository;
    }

    public ApplicantDetails getApplicantDetails(UUID id) {
        return applicantDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Applicant Details not found for ID: " + id));
    }

    @Transactional
    public ApplicantDetails saveApplicantDetails(ApplicantDetails applicantDetails) {
        return applicantDetailsRepository.save(applicantDetails);
    }

    @Transactional
    public ApplicantDetails updateApplicantDetails(UUID id, ApplicantDetails updatedApplicantDetails) {
        return applicantDetailsRepository.findById(id)
                .map(applicantDetails -> {
                    applicantDetails.setUser(updatedApplicantDetails.getUser());
                    applicantDetails.setEntityName(updatedApplicantDetails.getEntityName());
                    applicantDetails.setConstitutionType(updatedApplicantDetails.getConstitutionType());
                    applicantDetails.setDesignation(updatedApplicantDetails.getDesignation());
                    applicantDetails.setAddressField1(updatedApplicantDetails.getAddressField1());
                    applicantDetails.setAddressField2(updatedApplicantDetails.getAddressField2());
                    applicantDetails.setState(updatedApplicantDetails.getState());
                    applicantDetails.setDistrict(updatedApplicantDetails.getDistrict());
                    applicantDetails.setTaluka(updatedApplicantDetails.getTaluka());
                    applicantDetails.setVillage(updatedApplicantDetails.getVillage());
                    applicantDetails.setPincode(updatedApplicantDetails.getPincode());
                    applicantDetails.setApplicantPanNumber(updatedApplicantDetails.getApplicantPanNumber());
                    applicantDetails.setPanCardFilePath(updatedApplicantDetails.getPanCardFilePath());
                    // Add other fields here as necessary
                    return applicantDetailsRepository.save(applicantDetails);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Applicant Details not found for ID: " + id));
    }
    @Transactional
    public void deleteApplicantDetails(UUID id) {
        if (!applicantDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Applicant Details not found for ID: " + id);
        }
        applicantDetailsRepository.deleteById(id);
    }
}
