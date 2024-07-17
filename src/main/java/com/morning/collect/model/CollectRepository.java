package com.morning.collect.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CollectRepository extends JpaRepository<CollectVO, Integer> {

	//照編號刪除
	@Transactional
	@Modifying
	@Query(value = "delete from collect where id=?1", nativeQuery = true)
	void deleteById(Integer id);
	
	//查詢會員收藏
	@Query(value = "select * from collect where mem_no = ?1", nativeQuery = true)
	List<CollectVO> findByMemNo(Integer memNo);
}
