package org.commerceproject.psi.DTOS;

import lombok.Getter;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.commerceproject.psi.Models.ApplicantPageModels.*;
import org.commerceproject.psi.utilclass.RegexConstants;
import org.commerceproject.psi.utilclass.ValidationMessages;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;


@Getter @Setter
    public class ApplicantDetailsDto {
    private UUID id;

    @NotNull()
    private User user;

    @Size(max = 100, message = ValidationMessages.SIZE_100)
    private String entityName;

    @NotNull()
    private ConstitutionType constitutionType;

    @NotNull()
    private Designation designation;

    @Size(max = 200, message = ValidationMessages.SIZE_200)
    private String addressField1;

    @Size(max = 200, message = ValidationMessages.SIZE_200)
    private String addressField2;

    @NotNull()
    private State state;

    @NotNull()
    private District district;

    @NotNull()
    private Taluka taluka;

    @NotNull()
    private Village village;

    @Pattern(regexp=RegexConstants.PINCODE, message=ValidationMessages.INVALID_PINCODE)
    private String pincode;


    @Pattern(regexp=RegexConstants.PAN_NUMBER, message=ValidationMessages.INVALID_PAN_NUMBER)
    private String applicantPanNumber;

    private String panCardFilePath;

    private MultipartFile panCardFile;

    @Pattern(regexp=RegexConstants.AADHAR_NUMBER, message=ValidationMessages.INVALID_AADHAR_NUMBER)
    private String applicantAadharNumber;

    private String aadharCardFilePath;

    private MultipartFile aadharCardFile;

    @Size(max = 200, message =  ValidationMessages.SIZE_200)
    private String applicantAppointmentLocation;

    }

