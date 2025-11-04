package com.user.repository;

import com.user.entity.Item;
import com.user.enums.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByCode(String code);
    List<Item> findByType(ItemType type);
    List<Item> findByAvailable(Boolean available);
    boolean existsByCode(String code);
}
