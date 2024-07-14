package com.morning.mem.controller;



	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;

	@Controller
	public class ForgetPasswordController {

		 @Autowired
		    private MemService memSvc;
		    
		    @Autowired
		    private RedisTemplate<String, String> redisTemplate;
		
		
	    @GetMapping("/reset-password.html")
	    public String showResetPasswordPage(@RequestParam(required = false) String token) {
	        
	    
	    	
	    	
	    	
	    	
	        return "reset-password"; 
	    }
	}

