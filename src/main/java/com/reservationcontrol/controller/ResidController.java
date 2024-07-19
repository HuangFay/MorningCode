package com.reservationcontrol.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.reservationcontrol.model.ResCService;
import com.reservationcontrol.model.ResCVO;
import com.tabletype.model.TableTypeService;
import com.tabletype.model.TableTypeVO;

@Controller
@RequestMapping("/resc")
public class ResidController {
	@Autowired
	ResCService ResCSvc;
	@Autowired
	TableTypeService TableSvc;
	
	@GetMapping("addRes")
	public String addRes(ModelMap model) {
		ResCVO resCVO = new ResCVO();
		model.addAttribute("resCVO", resCVO);
		return "back-end/resc/addRes";
	}
	@PostMapping("insert")
	public String insert(@Valid ResCVO resCVO, BindingResult result, ModelMap model
//			,@RequestParam("upFiles") MultipartFile[] parts
					)throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(resVO, result, "upFiles");

//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				resVO.setUpFiles(buf);
//			}
//		}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "back-end/res/addRes";
//		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ResCSvc.addRes(resCVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ResCVO> list = ResCSvc.getAll();
		model.addAttribute("resCListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/resc/listAllResC"; // 新增成功後重導至IndexController_inSpringBoot.java的第50行@GetMapping("/emp/listAllEmp")
	}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("reservationControlId") Integer reservationControlId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService(); //autowired
		ResCVO resCVO = ResCSvc.getOneRes(Integer.valueOf(reservationControlId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("resCVO", resCVO);
		return "back-end/resc/update_res_input"; // 查詢完成後轉交update_emp_input.html
	}
	
	@PostMapping("update")
	public String update(@Valid ResCVO resCVO, BindingResult result, ModelMap model
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
		ResCSvc.updateRes(resCVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		resCVO = ResCSvc.getOneRes(Integer.valueOf(resCVO.getReservationControlId()));
		model.addAttribute("resCVO", resCVO);
		return "back-end/resc/listOneRes"; // 修改成功後轉交listOneEmp.html
	}
	@ModelAttribute("tableTypeListData")
	protected List<TableTypeVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<TableTypeVO> list = TableSvc.getAll();
		return list;
	}
	
}
