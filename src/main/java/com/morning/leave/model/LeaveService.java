	package com.morning.leave.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("leaveService")
public class LeaveService {
	
	@Autowired
	LeaveRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	public void addLeave(LeaveVO leaveVO) {
		repository.save(leaveVO);
		
	}
	
	public void updateLeave(LeaveVO leaveVO) {
		repository.save(leaveVO);

 
}	
	public void deleteLeave(Integer leaveId) {
		if (repository.existsById(leaveId))
			repository.deleteByLeaveId(leaveId);
	}
	
	public LeaveVO getOneLeave(Integer leaveId) {
		Optional<LeaveVO> optional = repository.findById(leaveId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}
	
	public List<LeaveVO> getAll() {
		return repository.findAll();
	}
	
	public List<LeaveVO> getLeavesByEmpId(Integer empId) {
        return repository.findByLeaveEmpId(empId);
    }

	public List<LeaveVO> getLeaveAssigneeId(Integer empId) {
		return repository.findByAssignEmpId(empId);

	}

//	public List<LeaveVO> getLeavesByEmpAccount(String empAccount) {
//
//		return repository.getLeavesByEmpAccount(empAccount);
//	}


}