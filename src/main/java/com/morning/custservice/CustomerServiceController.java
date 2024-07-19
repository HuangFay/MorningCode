package com.morning.custservice;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
//@RequestMapping("customer-service")
public class CustomerServiceController {
	
	// 前端
	@GetMapping("customer-service")
	public String customerService(ModelMap model) {

		return "front-end/custservice/cust-service.html";
	}
	
	// 後端
	@GetMapping("/back-end/customer-service")
	public String customerServiceList(ModelMap model) {

		return "back-end/custservice/cust-list.html";
	}
	
}
