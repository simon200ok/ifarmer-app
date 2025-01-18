package com.ifarmr.repository;

import com.ifarmr.entity.Inventory;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Category;
import com.ifarmr.entity.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findByUser(User user);
    List<Inventory> findByCategory(Category category);

    boolean existsByNameAndUser(String name, User user);

    List<Inventory> findAllByUserId(Long userId);

    Long countByUserIdAndCategory(long userId, Category category);

    Long countByUserIdAndItem(long userId, ItemType item);

    List<Inventory> findByUserIdAndCategory(long userId, Category category);

    List<Inventory> findByUserIdAndItem(long userId, ItemType item);

    List<Inventory> findByUserId(Long userId);

    List<Inventory> findByUserIdAndCategoryAndItem(Long userId, Category category, ItemType item);

    boolean existsByNameAndUserIdNotAndIdNot(String name, Long userId, Long inventoryId);

    Long countByUserId(long userId);

    Optional<Inventory> findByUserIdAndId(Long userId, Long inventoryId);
}

