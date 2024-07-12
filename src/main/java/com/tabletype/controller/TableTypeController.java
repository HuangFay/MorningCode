package com.tabletype.controller;


import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tabletype.model.TableTypeService;
import com.tabletype.model.TableTypeVO;

@Controller
@RequestMapping("/tabletype")
public class TableTypeController {
	@Autowired
	TableTypeService TableTypeSvc;
	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		
		@RequestParam("tableId") String tableId,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		TableTypeVO tableTypeVO = TableTypeSvc.getOneTableType(Integer.valueOf(tableId));
		
		List<TableTypeVO> list = TableTypeSvc.getAll();
		model.addAttribute("tableTypeListData", list); // for select_page.html 第97 109行用
		
		if (tableTypeVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/tabletype/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tableTypeVO", tableTypeVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第126行 -->
		
//		
		return "back-end/tabletype/select_page"; // 查詢完成後轉交select_page.html由其第128行insert listOneEmp.html內的th:fragment="listOneEmp-div
	}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("tableId") Integer tableId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService(); //autowired
		TableTypeVO tableTypeVO = TableTypeSvc.getOneTableType(Integer.valueOf(tableId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("tableTypeVO", tableTypeVO);
		return "back-end/tabletype/update_tabletype_input"; // 查詢完成後轉交update_emp_input.html
	}
	
	@PostMapping("update")
	public String update(@Valid TableTypeVO tableTypeVO, BindingResult result, ModelMap model
//			,@RequestParam("upFiles") MultipartFile[] parts
			) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(resVO, result, "upFiles");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] upFiles = empSvc.getOneEmp(empVO.getEmpno()).getUpFiles();
//			empVO.setUpFiles(upFiles);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] upFiles = multipartFile.getBytes();
//				empVO.setUpFiles(upFiles);
//			}
//		}
//		if (result.hasErrors()) {
//			return "back-end/emp/update_emp_input";
//		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		TableTypeSvc.updateTableType(tableTypeVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		tableTypeVO = TableTypeSvc.getOneTableType(Integer.valueOf(tableTypeVO.getTableId()));
		model.addAttribute("tableTypeVO", tableTypeVO);
		return "back-end/tabletype/listOneTableType"; // 修改成功後轉交listOneEmp.html
	}
	@ModelAttribute("tableTypeListData")
	protected List<TableTypeVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<TableTypeVO> list = TableTypeSvc.getAll();
		return list;
	}
	
	
}
