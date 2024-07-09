package com.morning.emp.model;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.morning.mem.model.MemVO;



public interface EmpRepository extends JpaRepository<EmpVO,Integer>{

	
	
	@Transactional
	@Modifying
	@Query(value = "delete from employee where emp_id =?1", nativeQuery = true)
	void deleteByEmpId(int empId);
	
	
	EmpVO findByEmpAccount(String empAccount);
//
//	//● (自訂)條件查詢
//	@Query(value = "from EmpVO where empno=?1 and ename like?2 and hiredate=?3 order by empno")
//	List<EmpVO> findByOthers(int empno , String ename , java.sql.Date hiredate);

}
