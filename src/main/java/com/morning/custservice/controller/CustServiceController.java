package com.morning.custservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
//@RequestMapping("customer-service")
public class CustServiceController {
	
	// 前端
	@GetMapping("customer-service")
	public String customerService(@RequestParam(name = "id", required = false) String id, ModelMap model) {

		model.addAttribute("userId", id);
		return "front-end/custservice/cust-service.html";
	}
	
	// 後端
	@GetMapping("/back-end/customer-service")
	public String customerServiceList(ModelMap model) {

		return "back-end/custservice/cust-list.html";
	}
	
}
