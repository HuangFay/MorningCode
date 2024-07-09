package com.morning.func;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;

//@Controller
public class FuncController {

	@Autowired
	EmpService empSvc;
	
	@Autowired
	FuncService funcSvc;
	
	
	@GetMapping("/emp/update/{empId}")
	public String showUpdateForm(@PathVariable("empId") Integer empId, Model model) {
	    EmpVO empVO = empSvc.getOneEmp(empId);
	    model.addAttribute("empVO", empVO);
	    model.addAttribute("allFunctions", funcSvc.getAll()); // 傳遞所有功能
	    return "update_emp_input";
	}

}
