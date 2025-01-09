package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;


public interface CropDetailsRepository extends JpaRepository<CropDetails, Long> {
    int countByUserId(Long userId);

    List<CropDetails> findByUserId(Long userId);
}
