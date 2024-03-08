package org.commerceproject.psi.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @Entity
public class ApplicantDetails extends BaseClass{
    @Enumerated(EnumType.STRING)
    private User user;
    private String EntityName;
    @Enumerated(EnumType.STRING)
    private ConstitutionType constitutionType;
    @Enumerated(EnumType.STRING)
    private Designation designation;
    private String AddressField1;
    private String AddressField2;
    @Enumerated(EnumType.STRING)
    private State state;
    @Enumerated(EnumType.STRING)
    private District district;
    @Enumerated(EnumType.STRING)
    private Taluka taluka;
    @Enumerated(EnumType.STRING)
    private Village village;
    private String pincode;
    private  String ApplicantPanNumber;
    private String panCardFilePath;
    private String ApplicantAadharNumber;
    private String aadharCardFilePath;
    private String ApplicantAppointmentLocation;
}
