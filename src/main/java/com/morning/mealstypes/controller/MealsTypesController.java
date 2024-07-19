package com.morning.mealstypes.controller;

import java.io.IOException;
import java.util.List;
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

import com.morning.meals.model.MealsVO;
import com.morning.mealstypes.model.MealsTypesService;
import com.morning.mealstypes.model.MealsTypesVO;

@Controller
@RequestMapping("/mealstypes")
public class MealsTypesController {

	@Autowired
	MealsTypesService mealstypesSvc;

	@GetMapping("addMealsTypes")
	public String addMealsTypes(ModelMap model) {
		MealsTypesVO mealstypesVO = new MealsTypesVO();
		model.addAttribute("mealstypesVO", mealstypesVO);
		return "back-end/mealstypes/addMealsTypes";
	}

	// 新增
	@PostMapping("insert")
	public String insert(@Valid MealsTypesVO mealstypesVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(mealstypesVO, result, "mealsTypesName");

		if (result.hasErrors()) {
			model.addAttribute("mealstypesVO", mealstypesVO);
			return "redirect:mealstypes/addMealsTypes";
		}

		/*************************** 2.開始新增資料 *****************************************/

		mealstypesSvc.addMealsTypes(mealstypesVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<MealsTypesVO> list = mealstypesSvc.getAll();
		model.addAttribute("mealstypesListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "/back-end/mealstypes/listAllMealsTypes";
	}

	// 修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("mealsTypesId") String mealsTypesId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		/*************************** 2.開始查詢資料 *****************************************/

		MealsTypesVO mealstypesVO = mealstypesSvc.getOneMealsTypes(Integer.valueOf(mealsTypesId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("mealstypesVO", mealstypesVO);
		return "back-end/mealstypes/update_mealstypes_input";
	}



	// 更新
	@PostMapping("update")
	public String update(@Valid MealsTypesVO mealstypesVO, BindingResult result, ModelMap model) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(mealstypesVO, result, "mealsTypesId");


		if (result.hasErrors()) {
			model.addAttribute("mealstypesVO", mealstypesVO);
			return "back-end/mealstypes/update_mealstypes_input";
		}

		/*************************** 2.開始修改資料 *****************************************/
		mealstypesSvc.updateMealsTypes(mealstypesVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		mealstypesVO = mealstypesSvc.getOneMealsTypes(Integer.valueOf(mealstypesVO.getMealsTypesId()));
		model.addAttribute("mealstypesVO", mealstypesVO);
		return "back-end/mealstypes/listAllMealsTypes";
	}

	// 全資料
	@ModelAttribute("mealstypesListData")
	protected List<MealsTypesVO> referenceListData() {
		List<MealsTypesVO> list = mealstypesSvc.getAll();
		return list;
	}
	
	// 去除BindingResult中某個欄位的FieldError紀錄
		public BindingResult removeFieldError(MealsTypesVO mealstypesVO, BindingResult result, String removedFieldname) {
			List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
					.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
			result = new BeanPropertyBindingResult(mealstypesVO, "mealstypesVO");
			for (FieldError fieldError : errorsListToKeep) {
				result.addError(fieldError);
			}
			return result;
		}


}
