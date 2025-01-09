package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CropDetailsRepository extends JpaRepository<CropDetails, Long> {
    int countByUserId(Long userId);
}
