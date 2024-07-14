package com.morning.mealspic.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mealspicService")
public class MealsPicService {
	
	@Autowired
	MealsPicRepository repository;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	//修改取多張圖片
	public List<MealsPicVO> getPicsByMealsId(Integer mealPicId) {
		return repository.getPicsByMealsId(mealPicId);
	}
	
	//取一張圖片
	public MealsPicVO getPicByMealsId(Integer mealsId) {
		return repository.getPicByMealsId(mealsId);
	}
	
	public void addMealsPic(MealsPicVO mealspicVO) {
		repository.save(mealspicVO);
	}
	
	public void updateMealsPic(MealsPicVO mealspicVO) {
		repository.save(mealspicVO);
	}
	
	public void deleteMealsPic(Integer mealPidId) {
		if(repository.existsById(mealPidId))
			repository.deleteById(mealPidId);
	}
	
	public MealsPicVO getOneMealsPic(Integer mealsId) {
		Optional<MealsPicVO> optional = repository.findById(mealsId);
		return optional.orElse(null);
	}
	
	public List<MealsPicVO> getAll(){
		return repository.findAll();
	}
	
	public boolean deleteMealPics(Integer mealPicId) {
		if(repository.existsById(mealPicId)) {
			repository.deleteByMealsPicId(mealPicId);
			return true;
		}else {
			return false;
		}
	}

}
