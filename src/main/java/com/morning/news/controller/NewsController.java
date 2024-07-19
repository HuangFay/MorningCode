package com.morning.news.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morning.news.model.NewsService;
import com.morning.news.model.NewsVO;

@Controller
@RequestMapping("news")
public class NewsController {

	@Autowired
	NewsService newsSvc;
	
	// 前台 最新消息列表, 最新消息內容 /news?id=2
	@GetMapping("")
	public String showNews(@RequestParam(value = "id", required = false)
						   String newsIdStr, ModelMap model) {
		if (newsIdStr != null) {
			Integer newId = Integer.valueOf(newsIdStr);
			NewsVO newsVO = newsSvc.getOneNews(newId);
			
			model.addAttribute("newsVO", newsVO);
			
			return "front-end/news/detail";
		}
		
		return "front-end/news/allNews";
	}
	
	// 後台 最新消息列表
	@RequestMapping("all")
	public String listAllNews() {
		return "back-end/news/newsList";
	}
	
	// 後台 編輯
	@PostMapping("edit")
	public String editNews(@RequestParam(value = "newsId", required = false)
	                       String newsIdStr, ModelMap model) {
		NewsVO newsVO = null;
		if (newsIdStr != null) {
			Integer newId = Integer.valueOf(newsIdStr);
			newsVO = newsSvc.getOneNews(newId);
			model.addAttribute("action", "update");
		}
		if( newsVO == null ) {
			newsVO = new NewsVO();
			model.addAttribute("action", "add");
		}
		
		model.addAttribute("newsVO", newsVO);
		return "back-end/news/editNews";
	}
	
	// 後台 送出編輯操作
	@PostMapping("editAction")
	public String editNewsAction(NewsVO newsVO, ModelMap model) {
		
		newsSvc.editNews(newsVO);
		
		return "redirect:/news/all";
	}
	
	// 後台 送出刪除操作
	@PostMapping("deleteAction")
	public String deleteNewsAction(
			@RequestParam(value = "newsId", required = false)
			String newsIdStr,
			ModelMap model) {
		
		Integer newId = Integer.valueOf(newsIdStr);
		newsSvc.deleteNews(newId);
		
		return "redirect:/news/all";
	}
	
	// 最新消息資料
	@ModelAttribute("newsListData")
	protected List<NewsVO> getNewsListData(Model model) {

		return newsSvc.getAll();
	}
}
 