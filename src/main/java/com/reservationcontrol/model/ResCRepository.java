	package com.reservationcontrol.model;

import com.reservation.model.ResVO;
import com.tabletype.model.TableTypeVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

	public interface ResCRepository extends JpaRepository<ResCVO, Integer>{

	@Transactional
	@Modifying
	@Query(value = "delete from morningcode where reservationControlId =?1", nativeQuery = true)
	void deleteByreservationControlId(int reservationControlId);

		//搜需欄位找出相對應的ResCVO
		List<ResCVO> findByreservationControlDateAndTableTypeVO(Date reservationControlDate, TableTypeVO tableTypeVO);

}
	