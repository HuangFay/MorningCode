package com.tabletype.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tabletype.model.TableTypeService;
import com.tabletype.model.TableTypeVO;

@Controller
@RequestMapping("/tabletype")
public class TableIdController {
	@Autowired
	TableTypeService TableTypeSvc;
	
	@GetMapping("addTableType")
	public String addTableType(ModelMap model) {
		TableTypeVO tableVO = new TableTypeVO();
		model.addAttribute("tableVO", tableVO);
		return "back-end/tabletype/addTableType";
	}
//	@GetMapping("insert")
	@PostMapping("insert")
	public String insert(@Valid TableTypeVO tableTypeVO, BindingResult result, ModelMap model) throws IOException{
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		TableTypeSvc.addTableType(tableTypeVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<TableTypeVO> list = TableTypeSvc.getAll();
		model.addAttribute("tableTypeListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/tabletype/listAllTableType"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
		
		
		
	}}
