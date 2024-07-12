package com.morning.mealspic.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface MealsPicRepository extends JpaRepository<MealsPicVO, Integer> {

	@Transactional
	@Modifying
	@Query(value="delate from meals where meals_pic_id =?1", nativeQuery = true)
	void deleteByMealsPicId(int mealsPicId);
	
	@Query(value="select * from meal_picture where meals_id =?1 limit 1", nativeQuery = true)
	MealsPicVO getPicByMealsId(Integer mealsId);
}
