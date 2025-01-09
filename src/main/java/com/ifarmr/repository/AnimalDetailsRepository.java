package com.ifarmr.repository;

import com.ifarmr.entity.AnimalDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalDetailsRepository extends JpaRepository<AnimalDetails, Integer> {
    int countByUserId(Long userId);

    List<AnimalDetails> findByUserId(Long userId);
}
