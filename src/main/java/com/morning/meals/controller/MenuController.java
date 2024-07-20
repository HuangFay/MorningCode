package com.morning.meals.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.morning.meals.model.MealsService;
import com.morning.meals.model.MealsVO;

@Controller
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	MealsService mealsSvc;

	// 菜單
	@GetMapping("/menu")
	public String showMenu(Model model) {
		List<MealsVO> mealsList = mealsSvc.getAllMeals();
		model.addAttribute("mealsList", mealsList);
		return "front-end/menu/menu";
	}

}
