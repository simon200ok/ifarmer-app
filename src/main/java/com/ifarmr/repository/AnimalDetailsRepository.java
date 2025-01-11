package com.ifarmr.repository;

import com.ifarmr.entity.AnimalDetails;
import com.ifarmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalDetailsRepository extends JpaRepository<AnimalDetails, Long> {
    int countByUserId(Long userId);
    List<AnimalDetails> findByUserId(long id);

    boolean existsByAnimalNameAndUser(String animalName, User user);
}
