// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

package com.morning.leave.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;



public interface LeaveRepository extends JpaRepository<LeaveVO, Integer> {
	
    @Query("SELECT l FROM LeaveVO l WHERE l.leaveEmpId.empId = :empId")
	List<LeaveVO> findByLeaveEmpId(Integer empId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from `leave` where leave_id =?1", nativeQuery = true)
	void deleteByLeaveId(int leaveId);

	@Query("SELECT l FROM LeaveVO l WHERE l.leaveAssigneeId.empId = :empId")
	List<LeaveVO> findByAssignEmpId(Integer empId);


	
	
	
//	//● (自訂)條件查詢
//	@Query(value = "from LeaveVO where leave_id=?1 and emp_hiredate=?2 order by Assign_id")
//	List<LeaveVO> findByOthers(int empId , java.sql.Date empHiredate);
}