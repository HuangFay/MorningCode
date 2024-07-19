package com.morning.meal.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Meal_customization_details;

@Service("mealService")
public class MealService {

    @Autowired
    MealRepository repository;
    
    @Autowired
    private SessionFactory sessionFactory;

    public void addMeal(MealVO mealVO) {
        repository.save(mealVO);
    }

    public void updateMeal(MealVO mealVO) {
        repository.save(mealVO);
    }

    public void deleteMeal(Integer customId) {
        if (repository.existsById(customId))
            repository.deleteByCustomId(customId);
    }

    public MealVO getOneMeal(Integer customId) {
        Optional<MealVO> optional = repository.findById(customId);
        return optional.orElse(null);
    }

    public List<MealVO> getAll() {
        return repository.findAll();
    }

    public List<MealVO> getAll(Map<String, String[]> map) {
        return HibernateUtil_CompositeQuery_Meal_customization_details.getAllC(map, sessionFactory.openSession());
    }

    public List<MealVO> findByOrderDetailId(Integer orddId) {
        return repository.findByOrddId(orddId);
    }
}
