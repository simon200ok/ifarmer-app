package com.ifarmr.repository;

import com.ifarmr.entity.Farmer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarmerRepository extends JpaRepository<Farmer,Long > {
    Optional<Farmer> findByEmail(String username);
}
