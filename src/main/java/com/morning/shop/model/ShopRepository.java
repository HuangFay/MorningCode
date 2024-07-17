package com.morning.shop.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ShopRepository extends JpaRepository<ShopVO, Integer> {

    Optional<ShopVO> findByMealsIdAndMemNo(Integer mealsId, Integer memNo);
    
    @Transactional
    void deleteByItemId(int itemId);

    List<ShopVO> findByItemId(int itemId);

    List<ShopVO> findByMemNo(Integer memNo);
}
