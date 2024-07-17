package com.morning.collect.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.morning.collect.model.CollectService;
import com.morning.collect.model.CollectVO;
import com.morning.mem.model.MemVO;

@Controller
@Validated
@RequestMapping("/collect")
public class CollectController {
	
	@Autowired
	CollectService collectSvc;
	
	
	@GetMapping("remove")
	public String addToFavorite(HttpSession session, Model model, String memNo) {
		
		MemVO loginid = (MemVO) session.getAttribute("memVO");
		
//不需要驗證
//		if(loginid == null) {
//			return "/front-end/mem/signup";
//		}
		
		Integer loginidmemno = loginid.getMemNo();
		
		List<CollectVO> collectVO = collectSvc.showFavorite(loginidmemno);
		
		model.addAttribute("collectVO", collectVO);
		return "/front-end/menu/listfavorite";
		
		
		
	}

}
