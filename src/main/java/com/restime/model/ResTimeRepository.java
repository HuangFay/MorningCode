package com.restime.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface ResTimeRepository extends JpaRepository<ResTimeVO, Integer>{

	@Transactional
	@Modifying
	@Query(value = "delete from morningcode where reservationTimeId =?1", nativeQuery = true)
	void deleteByreservationTimeId(int reservationTimeId);

}
