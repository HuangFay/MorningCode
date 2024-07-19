package com.morning.collect.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.morning.collect.model.CollectService;
import com.morning.collect.model.CollectVO;
import com.morning.meals.model.MealsVO;
import com.morning.mem.model.MemVO;

@Controller
@Validated
@RequestMapping("/collect")
public class CollectController {

	@Autowired
	CollectService collectSvc;

	// 顯示收藏清單
	@GetMapping("listfavorite")
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

	// 加入收藏
	@PostMapping("add")
	@ResponseBody
	public Map<String, Object> addToFavorite(HttpSession session, @RequestParam Integer mealsId) {
		Map<String, Object> response = new HashMap<>();
		MemVO loginMember = (MemVO) session.getAttribute("memVO");

		if (loginMember == null) {
			response.put("success", false);
			response.put("message", "請先登入");
			return response;
		}

		try {

			CollectVO collectVO = new CollectVO();
			collectVO.setMemVO(loginMember);
			MealsVO mealsVO = new MealsVO();
			mealsVO.setMealsId(mealsId);
			collectVO.setMealsVO(mealsVO);

			collectSvc.addFavorite(collectVO);

			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "添加收藏失敗");
		}

		return response;
	}

	// 取消收藏
	@PostMapping("remove")
	@ResponseBody
	public Map<String, Object> deleteFavorite(HttpSession session, @RequestParam Integer mealsId) {
		Map<String, Object> response = new HashMap<>();

		MemVO loginMember = (MemVO) session.getAttribute("memVO");

		if (loginMember == null) {
			response.put("success", false);
			response.put("message", "請先登入");
			return response;
		}

		try {
			collectSvc.deleteFavorite(loginMember.getMemNo(), mealsId);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "移除收藏失敗");
		}
		return response;
	}

	@GetMapping("getUserFavorites")
	@ResponseBody
	public List<Integer> getUserFavorites(HttpSession session) {
		MemVO loginMember = (MemVO) session.getAttribute("memVO");
		if (loginMember == null) {
			return new ArrayList<>();
		}
		return collectSvc.getUserFavorites(loginMember.getMemNo());
	}
}