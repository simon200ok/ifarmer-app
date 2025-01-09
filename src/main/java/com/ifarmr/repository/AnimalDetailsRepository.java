package com.ifarmr.repository;

import com.ifarmr.entity.AnimalDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;


public interface AnimalDetailsRepository extends JpaRepository<AnimalDetails, Long> {
    int countByUserId(Long userId);

    List<AnimalDetails> findByUserId(long id);
}
