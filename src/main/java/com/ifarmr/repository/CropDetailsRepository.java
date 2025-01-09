package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropDetailsRepository extends JpaRepository<CropDetails, Integer> {

    int countByUserId(Long userId);

    List<CropDetails> findByUserId(Long userId);
}
