package com.ifarmr.repository;

import com.ifarmr.entity.Inventory;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByUser(User user);
    List<Inventory> findByCategory(Category category);
}

