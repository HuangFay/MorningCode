package com.morning.meals.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface MealsRepository extends JpaRepository<MealsVO, Integer> {

	
	
	@Query(value="SELECT COUNT(*) FROM meals;" , nativeQuery = true)
	Integer getmealsnumber(); 
	
	@Query(value="select AVG(meals_score) from orddetails where meals_id=?1" , nativeQuery = true)
	Double getavgscore(Integer mealsId);
	
	@Modifying
	@Query(value = "UPDATE meals SET meals_total_score = ?1 WHERE meals_id = ?2" , nativeQuery = true)
	Integer updateMealsScore(Double mealsScore, Integer mealsId);
}
