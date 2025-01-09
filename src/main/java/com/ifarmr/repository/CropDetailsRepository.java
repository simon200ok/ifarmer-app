package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CropDetailsRepository extends JpaRepository<CropDetails, Long> {
}
