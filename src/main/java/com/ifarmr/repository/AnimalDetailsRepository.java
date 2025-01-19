package com.ifarmr.repository;

import com.ifarmr.entity.AnimalDetails;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.AnimalStatus;
import com.ifarmr.entity.enums.AnimalType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalDetailsRepository extends JpaRepository<AnimalDetails, Long> {
    Long countByUserId(Long userId);
    List<AnimalDetails> findByUserId(long id);
    boolean existsByAnimalNameAndUser(String animalName, User user);

    List<AnimalDetails> findByAnimalType(AnimalType type);

    List<AnimalDetails> findByAnimalStatus(AnimalStatus status);

    Long countByUserIdAndAnimalType(Long userId, AnimalType typ);

    Long countByUserIdAndAnimalStatus(Long userId, AnimalStatus status);

    boolean existsByAnimalNameAndUserIdNotAndIdNot(String animalName, Long userId, Long animalId);

    Optional<AnimalDetails> findByUserIdAndId(Long userId, Long animalId);
}
