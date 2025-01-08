package com.ifarmr.repository;

import com.ifarmr.entity.AnimalDetails;
import com.ifarmr.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalDetailsRepository extends JpaRepository<AnimalDetails, Integer> {
    List<AnimalDetails> findByUser(User user);

    int countByUserId(Long userId);
}
