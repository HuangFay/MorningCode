package com.morning.assign.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

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

import com.morning.assign.model.AssignService;
import com.morning.assign.model.AssignVO;
import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;


@Controller
@Validated
@RequestMapping("/assign")
public class AssignnoController {
	
	@Autowired
	EmpService empSvc;
	
	@Autowired
	AssignService assignSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="排班編號: 請勿空白")
		@Pattern(regexp = "^[(0-9)]{1,10}$", message = "排班編號: 只能是數字")
		@RequestParam("assignId") String assignId,ModelMap model) {

		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		
		AssignVO assignVO = assignSvc.getOneAssign(Integer.valueOf(assignId));
		List<AssignVO> list = assignSvc.getAll();
		model.addAttribute("assignListData", list);     // for select_page.html 第97 109行用
		
		model.addAttribute("empVO", new EmpVO());  // for select_page.html 第133行用
		List<EmpVO> list2 = empSvc.getAll();
    	model.addAttribute("empListData",list2);    // for select_page.html 第135行用
		
		if (assignVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/assign/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("assignVO", assignVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/emp/listOneEmp";  // 查詢完成後轉交listOneEmp.html
		return "back-end/assign/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
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
		List<AssignVO> list = assignSvc.getAll();
		model.addAttribute("assignListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("empVO", new EmpVO());  // for select_page.html 第133行用
		List<EmpVO> list2 = empSvc.getAll();
    	model.addAttribute("empListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/assign/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}