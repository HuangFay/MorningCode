package com.tabletype.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.reservationcontrol.model.ResCVO;

public interface TableTypeRepository extends JpaRepository<TableTypeVO, Integer>{
	
	@Transactional
	@Modifying
	@Query(value = "delete from morningcode where tableId =?1", nativeQuery = true)
	void deleteBytableTypelId(int tableId);
}
