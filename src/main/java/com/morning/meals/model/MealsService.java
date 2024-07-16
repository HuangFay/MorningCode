package com.morning.meals.model;

import java.util.List;
import java.util.Optional;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("mealsService")
public class MealsService {
	
	@Autowired
	MealsRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public void addMeals(MealsVO mealsVO) {
		repository.save(mealsVO);
	}
	
	public void updateMeals(MealsVO mealsVO) {
		repository.save(mealsVO);
	}
	
	
	
	public MealsVO getOneMeals(Integer mealsId) {
		Optional<MealsVO> optional = repository.findById(mealsId);
		return optional.orElse(null);
	}
	
	public List<MealsVO> getAll(){
		return repository.findAll();
	}

//	個數
	public Integer getmealsnumber() {
		return repository.getmealsnumber();
	}
//	平均評價
	public Double getavgscore(Integer mealsId) {
		return repository.getavgscore(mealsId);
	}
//	更新meals表格
	public void updateMealsScore(Double mealsTotalScore, Integer mealsId) {
		repository.updateMealsScore(mealsTotalScore, mealsId);
	}
	
//  菜單
	public List<MealsVO> getAllMeals(){
		return repository.findAll();
	}
	
}
