package com.morning.leave.model;

import java.util.List;
import java.util.Map;
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


	
//	public List<LeaveVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_Emp3.getAllC(map,sessionFactory.openSession());
//	}
//	

}