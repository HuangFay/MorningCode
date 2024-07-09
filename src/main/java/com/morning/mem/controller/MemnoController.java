package com.morning.mem.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;

@Controller
@Validated
@RequestMapping("/mem")
public class MemnoController {

	
	@Autowired
	MemService memSvc;
	
//	接收getOne_For_Display
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
			/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
			//這邊的驗證是在select_page.html
			
			@NotEmpty(message="Nice try")
			@Digits(integer = 3, fraction = 0,message ="編號: 請填數字-請勿超過{integer}位數")
			@RequestParam("memno") String memno,
			ModelMap model) {
		

		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
//		上面已經注入
		MemVO memVO =memSvc.getOneMem(Integer.valueOf(memno));
		
		List<MemVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);//前面變數名稱後面屬性名稱
		
		if(memVO ==null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/mem/select_page";
		}
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("memVO", memVO);
		return "back-end/mem/listOneMem";//查詢完成後轉交listOneEmp
	}
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<MemVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);     // for select_page.html 第97 109行用
//		model.addAttribute("deptVO", new DeptVO());  // for select_page.html 第133行用
//		List<DeptVO> list2 = deptSvc.getAll();
//    	model.addAttribute("deptListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/mem/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
}
