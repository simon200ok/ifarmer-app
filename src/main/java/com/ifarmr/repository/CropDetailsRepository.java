package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Arrays;
import java.util.List;


public interface CropDetailsRepository extends JpaRepository<CropDetails, Long> {
    int countByUserId(Long userId);
    List<CropDetails> findByUserId(Long userId);
    boolean existsByCropNameAndUser(String cropName, User user);

    @Query("SELECT c.cropStatus, COUNT(c) FROM CropDetails c GROUP BY c.cropStatus")
    List<Object[]> countCropsByStatus();
}
