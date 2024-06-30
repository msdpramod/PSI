package org.commerceproject.psi.Repository;

import org.commerceproject.psi.Models.ApplicantPageModels.ApplicantDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicantDetailsRepository extends JpaRepository<ApplicantDetails, UUID> {
}
