package com.ifarmr.repository;

import com.ifarmr.entity.AnimalDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AnimalDetailsRepository extends JpaRepository<AnimalDetails, Long> {
    int countByUserId(Long userId);
}
