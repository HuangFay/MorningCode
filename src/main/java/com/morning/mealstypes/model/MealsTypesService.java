package com.morning.mealstypes.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("mealstypesService")
public class MealsTypesService {
	
	@Autowired
	MealsTypesRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public MealsTypesService(MealsTypesRepository repository) {
		this.repository = repository;
	}
	
	
	public void addMealsTypes(MealsTypesVO mealstypesVO) {
		repository.save(mealstypesVO);
	}
	
	public void updateMealsTypes(MealsTypesVO mealstypesVO) {
		repository.save(mealstypesVO);
	}
	
	
	public MealsTypesVO getOneMealsTypes(Integer mealsTypesId) {
		Optional<MealsTypesVO> optional = repository.findById(mealsTypesId);
		return optional.orElse(null);
	}
	
	public List<MealsTypesVO> getAll(){
		return repository.findAll();
	}


}
