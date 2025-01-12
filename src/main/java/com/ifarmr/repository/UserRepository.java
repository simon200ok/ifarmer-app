package com.ifarmr.repository;

import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long > {
    Optional<User> findByEmail(String username);
    long countByGender(Gender gender);

}


