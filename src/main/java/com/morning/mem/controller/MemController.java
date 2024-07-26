package com.morning.mem.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;

@Controller
@RequestMapping("/mem")
public class MemController {

	@Autowired
	MemService memSvc;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("addMem")
	public String addMem(ModelMap model) {
		MemVO memVO = new MemVO();
		model.addAttribute("memVO", memVO);
		return "back-end/mem/addMem";
	}

	@PostMapping("insert")
	public String insert(@Valid MemVO memVO, BindingResult result, ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 -->
		result = removeFieldError(memVO, result, "upFiles");

		if (parts[0].isEmpty()) {
			model.addAttribute("errorMessage", "請選擇圖片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				memVO.setUpFiles(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/mem/addMem";
		}
	    if (memSvc.emailExists(memVO.getMemEmail())) {
	        model.addAttribute("errorMessage", "該Email已存在");
	        return "back-end/mem/addMem";
	    }

		/*************************** 2.開始新增資料 *****************************************/
		String encodedPassword = passwordEncoder.encode(memVO.getMemPassword());
		memVO.setMemPassword(encodedPassword);
		memVO.setMemVerified((byte) 0);
		memSvc.addMem(memVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/

		List<MemVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);
		model.addAttribute("sucess", "成功");
		return "back-end/mem/listAllMem";// 重導,防止重複提交
	}

	// List 點擊修改她會接收你點擊的員工資料
//	@PostMapping("getOne_For_Update")
//	public String getOne_For_Update(@RequestParam("memNo") String memNo,ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		/*************************** 2.開始查詢資料 *****************************************/
//		// MemService memSvc = new MemService(); 上面有注入
//		MemVO memVO = memSvc.getOneMem(Integer.valueOf(memNo));
//		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("memVO", memVO);
//		return "back-end/mem/update_mem_input";// 查詢完成後轉交update_mem_input.html
//	}
//	 
//	
//	@PostMapping("update")
//	public String update(@Valid MemVO memVO,BindingResult result,ModelMap model,
//			@RequestParam("upFiles")MultipartFile[] parts)throws IOException {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		result = removeFieldError(memVO, result, "upFiles");
//		
//		if(parts[0].isEmpty()) {// 使用者未選擇要上傳的新圖片時
//			byte[] upFiles =memSvc.getOneMem(memVO.getMemNo()).getUpFiles();
//			memVO.setUpFiles(upFiles);
//		}else {
//			for(MultipartFile multipartFile : parts) {
//				byte[] upFiles =multipartFile.getBytes();
//				memVO.setUpFiles(upFiles);
//		    }
//		}
//		
//		if(result.hasErrors()) {
//			return"back-end/mem/update_mem_input";
//		}
//		/*************************** 2.開始修改資料 *****************************************/
//		memSvc.updateMem(memVO);
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("success", "(修改完成)");
//		memVO=memSvc.getOneMem(Integer.valueOf(memVO.getMemNo()));//因為varchar 要轉整數
//		model.addAttribute("memVO", memVO);//顯示在畫面上
//		return "back-end/mem/listOneMem";
//	}

	@PostMapping("delete")
	public String delete(@RequestParam("memNo") String memNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		memSvc.deleteMem(Integer.valueOf(memNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<MemVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);
		model.addAttribute("success", "(刪除完成)");
		return "back-end/mem/listAllMem";
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(MemVO memVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(memVO, "memVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// 使用swal

	@GetMapping("getOne_For_Update")
	@ResponseBody
	public MemVO getOne_For_Update(@RequestParam("memNo") String memNo) {
		return memSvc.getOneMem(Integer.valueOf(memNo));
	}

	@PostMapping("update")
	@ResponseBody
	public String update(@Valid MemVO memVO, BindingResult result) {
		System.out.println("Update method called with memVO: " + memVO);
		if (result.hasErrors()) {
			System.out.println("Validation errors: " + result.getAllErrors());
			return "error";
		}
		try {
			    	 MemVO existingMem = memSvc.getOneMem(memVO.getMemNo());
//			    	 memVO.setMemPassword(existingMem.getMemPassword());
			
			   memVO.setUpFiles(existingMem.getUpFiles());
			memSvc.updateMem(memVO);
			System.out.println("Update successful");
			return "success";
		} catch (Exception e) {
			System.out.println("Update failed: " + e.getMessage());
			e.printStackTrace();
			return "error";
		}
	}
	
	
	
}
