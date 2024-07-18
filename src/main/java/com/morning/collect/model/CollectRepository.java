package com.morning.collect.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CollectRepository extends JpaRepository<CollectVO, Integer> {

	// 取消收藏
	@Transactional
	@Modifying
	@Query(value = "delete from collect where mem_no=?1 and meals_id=?2", nativeQuery = true)
	void deleteByMemNoAndMealsId(Integer memNo, Integer mealsId);

	// 查詢會員收藏
	@Query(value = "select * from collect where mem_no = ?1", nativeQuery = true)
	List<CollectVO> findByMemNo(Integer memNo);

	// 查詢會員收藏
	@Query(value = "select count(*) > 0 from collect where mem_no = ?1 and meals_id = ?2", nativeQuery = true)
	boolean existsByMemNoAndMealsId(Integer memNo, Integer mealsId);

	// 用輸入的memNo去CollectVO查mealsId
	@Query("SELECT c.mealsVO.mealsId FROM CollectVO c WHERE c.memVO.memNo = :memNo")
	List<Integer> findMealsIdsByMemNo(@Param("memNo") Integer memNo);
}
