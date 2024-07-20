package com.morning.assign.model;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service("assignService")
public class AssignService {

	@Autowired
	AssignRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	public void addAssign(AssignVO assignVO) {
		repository.save(assignVO);
	}

	public void updateAssign(AssignVO assignVO) {
		repository.save(assignVO);
	}

	public void deleteAssign(Integer assignId) {
		if (repository.existsById(assignId))
			repository.deleteByAssignId(assignId);
//		    repository.deleteById(empno);
	}

	public AssignVO getOneAssign(Integer assignId) {
		Optional<AssignVO> optional = repository.findById(assignId);
//		return optional.get();
		return optional.orElse(null);  // public T orElse(T other) : 如果值存在就回傳其值，否則回傳other的值
	}

	public List<AssignVO> getAll() {
		return repository.findAll();
	}


    public List<AssignVO> getMonthlyAssigns(int year, int month) {
        return repository.findByMonthAndYear(year, month);
    }

    public List<AssignVO> getAssignByDate(Date assignDate) {
        return repository.findByAssignDate(assignDate);
    }
    
    public List<AssignVO> getAssignmentsByDate(Date assignDate) {
        return repository.findByAssignDate(assignDate);
    }

    public List<Date> getWorkingDatesByEmpId(Integer empId) {
        List<AssignVO> assignments = repository.findByEmpVO_EmpId(empId);
        assignments.addAll(repository.findByEmpVO1_EmpId(empId));
        return assignments.stream()
                .map(AssignVO::getAssignDate)
                .collect(Collectors.toList());
    }
    
    
}

//	public List<AssignVO> getAll(Map<String, String[]> map) {
//		return HibernateUtil_CompositeQuery_Emp3.getAllC(map,sessionFactory.openSession());
//	}

