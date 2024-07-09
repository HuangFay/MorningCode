package com.morning.mem.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MemRepository extends JpaRepository<MemVO,Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from member where mem_no =?1", nativeQuery = true)
	void deleteByMemId(int memNo);
	
	 MemVO findByMemEmail(String memEmail);
	
}
