package com.ifarmr.repository;

import com.ifarmr.entity.CropDetails;
import com.ifarmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropDetailsRepository extends JpaRepository<CropDetails, Integer> {
    List<CropDetails> findByUser(User user);
}
