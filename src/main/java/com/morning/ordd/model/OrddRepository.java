package com.morning.ordd.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrddRepository extends JpaRepository<OrddVO, Integer> {
	
	@Transactional
	@Modifying
	@Query(value = "delete from orddetails where ordd_id =?1", nativeQuery = true)
	void deleteByOrddId(int orddId);

	//● (自訂)條件查詢
	@Query(value = "from OrddVO where ordd_id=?1 order by ordd_id")
	List<OrddVO> findByOrddId(int orddId);
}