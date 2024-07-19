package com.morning.meals.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.morning.meals.model.MealsService;
import com.morning.meals.model.MealsVO;
import com.morning.mealspic.model.MealsPicService;
import com.morning.mealspic.model.MealsPicVO;
import com.morning.mealstypes.model.MealsTypesService;
import com.morning.mealstypes.model.MealsTypesVO;

@Controller
@RequestMapping("/meals")
public class MealsController {

	@Autowired
	MealsService mealsSvc;

	@Autowired
	MealsTypesService mealstypesSvc;

	@Autowired
	MealsPicService mealspicSvc;
	
	@GetMapping("addMeals")
	public String addMeals(ModelMap model) {
		MealsVO mealsVO = new MealsVO();
		model.addAttribute("mealsVO", mealsVO);
		return "back-end/meals/addMeals";
	}

	// 新增
	@PostMapping("insert")
	public String insert(@Valid MealsVO mealsVO, BindingResult result, ModelMap model,
			@RequestParam("mealPic") MultipartFile[] parts ) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(mealsVO, result, "mealPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "餐點圖片: 請上傳");
		} else {
			List<MealsPicVO> picSet = new ArrayList<>();
			
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				
				MealsPicVO mealspicVO = new MealsPicVO();
				mealspicVO.setMealPic(buf);
				mealspicVO.setMealsVO(mealsVO);
				
				picSet.add(mealspicVO);
			}
			mealsVO.setMealspics(picSet);
		}
		
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/meals/addMeals";
		}

		/*************************** 2.開始新增資料 *****************************************/

		mealsSvc.addMeals(mealsVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<MealsVO> list = mealsSvc.getAll();
		model.addAttribute("mealsListData", list);	
		model.addAttribute("success", "- (新增成功)");
		System.out.println("ok");
		return "back-end/meals/listOneMeals";
	}

	// 修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("mealsId") String mealsId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		/*************************** 2.開始查詢資料 *****************************************/

		MealsVO mealsVO = mealsSvc.getOneMeals(Integer.valueOf(mealsId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("mealsVO", mealsVO);
		return "back-end/meals/update_meals_input";
	}

	@PostMapping("update")
	public String update(@Valid MealsVO mealsVO, BindingResult result, ModelMap model,
			@RequestParam("mealPic") MultipartFile[] parts ) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(mealsVO, result, "mealPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "餐點圖片: 請上傳");
		} else {
			List<MealsPicVO> picSet = new ArrayList<>();
			
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				
				MealsPicVO mealspicVO = new MealsPicVO();
				mealspicVO.setMealPic(buf);
				mealspicVO.setMealsVO(mealsVO);
				
				picSet.add(mealspicVO);
			}
			mealsVO.setMealspics(picSet);
		}
		
//		if (result.hasErrors()) {
//		
//			return "back-end/meals/update_meals_input";
//		}

		/*************************** 2.開始修改資料 *****************************************/
		mealsSvc.updateMeals(mealsVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		mealsVO = mealsSvc.getOneMeals(Integer.valueOf(mealsVO.getMealsId()));
		model.addAttribute("mealsVO", mealsVO);
		return "back-end/meals/listOneMeals";
	}

//	// 修改，刪除圖片
//	@PostMapping("deletepic")
//	@ResponseBody
//	public Map<String, Object> deleteMealPic(@RequestParam("mealPicId") Integer mealPicId) {
//		Map<String, Object> response = new HashMap<>();
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		/*************************** 2.開始刪除資料 *****************************************/
//		boolean success = mealsSvc.deleteMealPics(mealPicId);
//		
//		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
//		if(success) {
//			response.put("success", true);
//			System.out.println("成功");
//			
//		}else {
//			response.put("success", false);
//			response.put("message", "失敗");
//		}
//
//		return response; // 刪除完成後轉交
//	}

	
	@ModelAttribute("mealstypesListData")
	protected List<MealsTypesVO> referenceListData() {
		List<MealsTypesVO> list = mealstypesSvc.getAll();
		return list;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(MealsVO mealsVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(mealsVO, "mealsVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	// 每天每一個小時(第0秒整點)執行一次更新 meals 表格的 meals_total_score
	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void updateScore() {
		// 查詢 orddetails 表格中的 meals_score 欄位的平均值和總個數
		Integer mealsnumber = mealsSvc.getmealsnumber();
		
		for(int i=1; i <= mealsnumber ; i++) {
						
			Double mealsavg = mealsSvc.getavgscore(i);
			
			if(mealsavg == null) {
				continue;
			}
			
			mealsSvc.updateMealsScore(mealsavg, i); // 更新 meals 實體
		}

	}

}
