// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.morning.assign.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AssignRepository extends JpaRepository<AssignVO, Integer> {

	@Transactional
	@Modifying
	@Query(value = "delete from assign where assign_id =?1", nativeQuery = true)
	void deleteByAssignId(int assignId);

	//● (自訂)條件查詢
	@Query(value = "from AssignVO where assign_id=?1 and emp_hiredate=?2 order by Assign_id")
	List<AssignVO> findByOthers(int empId , java.sql.Date empHiredate);
	
	@Query("SELECT a FROM AssignVO a WHERE YEAR(a.assignDate) = :year AND MONTH(a.assignDate) = :month")
	List<AssignVO> findByMonthAndYear(@Param("year") int year, @Param("month") int month);

	 @Query("SELECT a FROM AssignVO a WHERE a.assignDate = :assignDate")
	    List<AssignVO> findByAssignDate(@Param("assignDate") Date assignDate);

}


