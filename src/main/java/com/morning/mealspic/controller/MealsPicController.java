package com.morning.mealspic.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.morning.meals.model.MealsService;
import com.morning.meals.model.MealsVO;
import com.morning.mealspic.model.MealsPicService;
import com.morning.mealspic.model.MealsPicVO;

@Controller
@RequestMapping("/mealspic")
public class MealsPicController {

	@Autowired
	MealsPicService mealspicSvc;

	@Autowired
	MealsService mealsSvc;

	@GetMapping("addMealsPic")
	public String addMeals(ModelMap model) {
		MealsPicVO mealspicVO = new MealsPicVO();
		model.addAttribute("mealspicVO", mealspicVO);
		return "back-end/mealspic/addMealsPic";
	}

	// 新增
	@PostMapping("insert")
	public String insert(@Valid MealsPicVO mealspicVO, BindingResult result, ModelMap model,
			@RequestParam("mealPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(mealspicVO, result, "mealPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "餐點圖片: 請上傳");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				mealspicVO.setMealPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/mealspic/addMealsPic";
		}
		/*************************** 2.開始新增資料 *****************************************/

		mealspicSvc.addMealsPic(mealspicVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<MealsPicVO> list = mealspicSvc.getAll();
		model.addAttribute("mealspicListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "back-end/meals/listAllMeals";
	}

	// 修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("mealPicId") String mealPicId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/

		MealsPicVO mealspicVO = mealspicSvc.getOneMealsPic(Integer.valueOf(mealPicId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("mealspicVO", mealspicVO);
		return "back-end/mealspic/update_mealspic_input";
	}

	@PostMapping("update")
	public String update(@Valid MealsPicVO mealspicVO, BindingResult result, ModelMap model,
			@RequestParam("mealPic") MultipartFile[] parts) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(mealspicVO, result, "mealPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "餐點圖片: 請上傳");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				mealspicVO.setMealPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/mealspic/update_mealspic_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		mealspicSvc.updateMealsPic(mealspicVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		mealspicVO = mealspicSvc.getOneMealsPic(Integer.valueOf(mealspicVO.getMealPicId()));
		model.addAttribute("mealspicVO", mealspicVO);
		return "back-end/mealspic/listOneMealsPic";
	}

	// 刪除
	@PostMapping("delete")
	public String delete(@RequestParam("mealPicId") String mealPicId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/

		mealspicSvc.deleteMealsPic(Integer.valueOf(mealPicId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<MealsPicVO> list = mealspicSvc.getAll();
		model.addAttribute("mealspicListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/mealspic/listAllMealsPic"; // 刪除完成後轉交
	}
	
	// 修改，刪除圖片
		@PostMapping("deletepic")
		@ResponseBody
		public Map<String, Object> deleteMealPic(@RequestParam("mealPicId") Integer mealPicId) {
			Map<String, Object> response = new HashMap<>();
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			/*************************** 2.開始刪除資料 *****************************************/
			boolean success = mealspicSvc.deleteMealPics(mealPicId);
			
			/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
			if(success) {
				response.put("success", true);
				System.out.println("成功");
				
			}else {
				response.put("success", false);
				response.put("message", "失敗");
			}

			return response; // 刪除完成後轉交
		}

	@ModelAttribute("mealsListData")
	protected List<MealsVO> referenceListData() {
		List<MealsVO> list = mealsSvc.getAll();
		return list;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(MealsPicVO mealspicVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(mealspicVO, "mealspicVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}
