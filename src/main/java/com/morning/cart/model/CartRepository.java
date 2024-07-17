package com.morning.cart.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "delete from cart_items where cart_item_id =?1", nativeQuery = true)
    void deleteByCartItemId(int cartItemId);

    @Query(value = "from CartVO where memVO.memNo=?1 order by cartItemId")
    List<CartVO> findByMemNo(Integer memNo);

    Optional<CartVO> findByMemNoAndMealsId(Integer memNo, Integer mealsId);
}
