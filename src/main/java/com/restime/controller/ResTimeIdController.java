package com.restime.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.restime.model.ResTimeService;
import com.restime.model.ResTimeVO;

@Controller
@RequestMapping("/restime")
public class ResTimeIdController {
	@Autowired
	ResTimeService ResTimeSvc;
	
	@GetMapping("addResTime")
	public String addEmp(ModelMap model) {
		ResTimeVO resTimeVO = new ResTimeVO();
		model.addAttribute("resTimeVO", resTimeVO);
		return "back-end/restime/addResTime";
	}
	@GetMapping("insert")
	public String insert(@Valid ResTimeVO resTimeVO, BindingResult result, ModelMap model) throws IOException{
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
	
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ResTimeSvc.addResTime(resTimeVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ResTimeVO> list = ResTimeSvc.getAll();
		model.addAttribute("resTimeListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/resctime/listAllResTIme"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
	
		
		
	}
	
	
}
