package org.commerceproject.psi.DTOS;

import lombok.Getter;
import lombok.Setter;
import org.commerceproject.psi.Models.*;

import java.util.UUID;

@Getter @Setter
    public class ApplicantDetailsDto {
        private UUID id;
        private User user;
        private String entityName;
        private ConstitutionType constitutionType;
        private Designation designation;
        private String addressField1;
        private String addressField2;
        private State state;
        private District district;
        private Taluka taluka;
        private Village village;
        private String pincode;
        private  String applicantPanNumber;
        private String panCardFilePath;
        private String applicantAadharNumber;
        private String aadharCardFilePath;
        private String applicantAppointmentLocation;

    }

