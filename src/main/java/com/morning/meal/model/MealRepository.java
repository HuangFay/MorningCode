package com.morning.meal.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MealRepository extends JpaRepository<MealVO, Integer> {

    @Transactional
    @Modifying
    @Query(value = "delete from meal_customization_details where custom_id =?1", nativeQuery = true)
    void deleteByCustomId(int customId);

    // 自訂條件查詢
    @Query(value = "from MealVO where ordd_id=?1 order by custom_id")
    List<MealVO> findByOrddId(int orddId);

    // 自訂條件查詢
    @Query(value = "from MealVO where cust_id=?1 order by custom_id")
    List<MealVO> findByCustId(int custId);
}
