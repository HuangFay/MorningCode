package com.morning.emp.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morning.func.FuncRepository;
import com.morning.func.FuncVO;
import com.morning.mem.model.MemVO;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Emp;

@Service("empService")
public class EmpService {

	
	//提供CRUD
	@Autowired
	EmpRepository repository;
	
	
	@Autowired
	FuncRepository funcRepository;
	
//	複合查詢才需要
//	創建 Hibernate會話，通過 HibernateUtil_CompositeQuery_Emp.getAllC 方法執行動態查詢
	@Autowired
	private SessionFactory sessionFactory;
	
	public EmpVO getOneEmp(Integer empid) {
		Optional<EmpVO> optional =repository.findById(empid);
		
		return optional.orElse(null);
	}
	public List<EmpVO> getAll(){
		return repository.findAll();
	}
	
	public void addEmp(EmpVO empVO) {
		repository.save(empVO);
	}
	public void updateEmp(EmpVO empVO) {
		repository.save(empVO);
	}
	
	public void deleteEmp(Integer empid) {
		if(repository.existsById(empid)) {
			repository.deleteByEmpId(empid);
		}
	}
	//複合查詢
	public List<EmpVO> getAll(Map<String, String[]> map){
		return HibernateUtil_CompositeQuery_Emp.getAllC(map, sessionFactory.openSession());
	}
	
	
	//權限===================================================
	// 添加權限
    public void addFunctionToEmp(Integer empId, Integer functionId) {
        Optional<EmpVO> empOpt = repository.findById(empId);
        Optional<FuncVO> funcOpt = funcRepository.findById(functionId);

        if (empOpt.isPresent() && funcOpt.isPresent()) {
            EmpVO emp = empOpt.get();
            FuncVO func = funcOpt.get();
            emp.getFunctions().add(func);
            repository.save(emp);
        }
    }

    // 刪除權限
    public void removeFunctionFromEmp(Integer empId, Integer functionId) {
        Optional<EmpVO> empOpt = repository.findById(empId);
        Optional<FuncVO> funcOpt = funcRepository.findById(functionId);

        if (empOpt.isPresent() && funcOpt.isPresent()) {
            EmpVO emp = empOpt.get();
            FuncVO func = funcOpt.get();
            emp.getFunctions().remove(func);
            repository.save(emp);
        }
    }
    
    //登入邏輯===============================================
    
    public EmpVO getEmpByAccount(String account) {
        return repository.findByEmpAccount(account);
    }
    
    public boolean authenticateEmp(String account, String password) {
        EmpVO existingEmp = repository.findByEmpAccount(account);
        return existingEmp != null && existingEmp.getEmpPassword().equals(password);
    }
}
