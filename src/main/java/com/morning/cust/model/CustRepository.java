package com.morning.cust.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface CustRepository extends JpaRepository<CustVO, Integer> {
    
	@Transactional
	@Modifying
	@Query(value = "delete from customization_options where cust_id =?1", nativeQuery = true)
	void deleteByCustId(int custId);

	//● (自訂)條件查詢
	@Query(value = "from CustVO where cust_id=?1 order by cust_id")
	List<CustVO> findByCustId(int custId);
}
