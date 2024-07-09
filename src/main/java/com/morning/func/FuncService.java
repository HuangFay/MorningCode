package com.morning.func;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("funcSrevice")
public class FuncService {

	
	@Autowired
	FuncRepository repository;
	
	public void addFunc(FuncVO funcVO) {
		repository.save(funcVO);
	}
	
	public void updateFunc(FuncVO funcVO) {
		repository.save(funcVO);
	}
	public void deleteFunc(Integer funcid) {
		if(repository.existsById(funcid)) {
			repository.deleteById(funcid);
		}
	}
	public FuncVO getOneFunc(Integer funcid) {
		Optional<FuncVO> optional=repository.findById(funcid);
		return optional.orElse(null);
		
	}
	public List<FuncVO> getAll(){
		return repository.findAll();
		
	}
	
	
}
