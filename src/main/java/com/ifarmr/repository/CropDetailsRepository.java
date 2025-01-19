package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.CropStatus;
import com.ifarmr.entity.enums.CropType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public interface CropDetailsRepository extends JpaRepository<CropDetails, Long> {
    Long countByUserId(Long userId);
    List<CropDetails> findByUserId(Long userId);
    boolean existsByCropNameAndUser(String cropName, User user);

    List<CropDetails> findByUserIdAndCropType(Long userId, CropType type);

    List<CropDetails> findByUserIdAndCropStatus(Long userId, CropStatus status);

    boolean existsByCropNameAndUserIdNotAndIdNot(String cropName, Long userId, Long cropId);

    Optional<CropDetails> findByUserIdAndId(Long userId, Long cropId);

    @Query("SELECT c.cropStatus, COUNT(c) FROM CropDetails c WHERE c.user.id = :userId GROUP BY c.cropStatus")
    List<Object[]> countCropsByStatusForUser(Long userId);

    @Query("SELECT c.cropType, COUNT(c) FROM CropDetails c WHERE c.user.id = :userId GROUP BY c.cropType")
    List<Object[]> countCropsByCropTypeForUser(Long userId);
}
