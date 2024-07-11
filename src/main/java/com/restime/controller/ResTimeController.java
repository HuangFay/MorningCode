package com.restime.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.restime.model.ResTimeService;
import com.restime.model.ResTimeVO;

@Controller
@RequestMapping("/restime")
public class ResTimeController {
	@Autowired
	ResTimeService ResTimeSvc;
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		
		@RequestParam("reservationTimeId") String reservationTimeId,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		ResTimeVO resTimeVO = ResTimeSvc.getOneResTime(Integer.valueOf(reservationTimeId));
		
		List<ResTimeVO> list = ResTimeSvc.getAll();
		model.addAttribute("resTimeListData", list); // for select_page.html 第97 109行用
		
		if (resTimeVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/restime/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("resTimeVO", resTimeVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第126行 -->
		
//		
		return "back-end/restime/select_page"; // 查詢完成後轉交select_page.html由其第128行insert listOneEmp.html內的th:fragment="listOneEmp-div
	}
	
	  
	  
}
