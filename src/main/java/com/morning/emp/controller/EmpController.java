package com.morning.emp.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;
import com.morning.func.FuncService;
import com.morning.func.FuncVO;

@Controller
@RequestMapping("/emp")
public class EmpController {

	
	@Autowired
	EmpService empSvc;
	
	@Autowired
	FuncService funcSvc;
	
	@GetMapping("addEmp")
	public String addEmp(ModelMap model) {
		EmpVO empVO = new EmpVO();
		model.addAttribute("empVO", empVO);
		model.addAttribute("allFunctions", funcSvc.getAll());
		return "back-end/emp/addEmp";
	}
	
	@PostMapping("insert")
	public String insert(@Valid EmpVO empVO,BindingResult result,ModelMap model,
			@RequestParam("upFiles") MultipartFile[] parts)throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 -->
		result = removeFieldError(empVO,result,"upFiles");
		 
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "請選擇圖片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				empVO.setUpFiles(buf);
			}
		}
		if (result.hasErrors()|| parts[0].isEmpty()) {
		        // 校验失败处理逻辑，例如返回原页面并显示错误信息
		        return "back-end/emp/addEmp";
		    }
		/*************************** 2.開始新增資料 *****************************************/
		empSvc.addEmp(empVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<EmpVO> list=empSvc.getAll();
		model.addAttribute("empListData", list);
		model.addAttribute("success", "員工新增成功");
		return "back-end/emp/listAllEmp";  //重導,防止重複提交
	}
	
	
	//List 點擊修改她會接收你點擊的員工資料
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("empId") String empId,ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService(); 上面有注入
		EmpVO empVO =empSvc.getOneEmp(Integer.valueOf(empId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("empVO",empVO);
		model.addAttribute("allFunctions", funcSvc.getAll());
		return "back-end/emp/update_emp_input";// 查詢完成後轉交update_emp_input.html
	}
	
	@PostMapping("update")	
	public String update(@Valid EmpVO empVO,BindingResult result,ModelMap model,
			@RequestParam("upFiles")MultipartFile[] parts)throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		result = removeFieldError(empVO, result, "upFiles");
		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] upFiles = empSvc.getOneEmp(empVO.getEmpId()).getUpFiles();
			empVO.setUpFiles(upFiles);
		} else {
			//接收圖片
			for (MultipartFile multipartFile : parts) {
				byte[] upFiles = multipartFile.getBytes();
				empVO.setUpFiles(upFiles);
			}
		}
		
		
		if(result.hasErrors()) {
			model.addAttribute("allFunctions", funcSvc.getAll());
			return "back-end/emp/update_emp_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		empSvc.updateEmp(empVO);
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "(修改完成)");
		empVO =empSvc.getOneEmp(Integer.valueOf(empVO.getEmpId())); // 因為varchar 要轉整數
		model.addAttribute("empVO", empVO); //顯示在畫面上
		model.addAttribute("allFunctions", funcSvc.getAll());
		return "back-end/emp/listOneEmp";
	}
	
	
	@PostMapping("delete")
	public String delete(@RequestParam("empId") String empId,ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		empSvc.deleteEmp(Integer.valueOf(empId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<EmpVO> list =empSvc.getAll();
		model.addAttribute("empListData", list);
		model.addAttribute("success", "(刪除完成)");
		return "back-end/emp/listAllEmp";
	}
	
	
	
	@PostMapping("listEmps_ByCompositeQuery")
	public String listAllEmp(HttpServletRequest req,Model model) {
		Map<String, String[]> map =req.getParameterMap();
		List<EmpVO> list =empSvc.getAll(map);
		model.addAttribute("empListData", list);//將資料放到listAllEmp
		return "back-end/emp/listAllEmp";
	}
	
	
	// 去除BindingResult中某個欄位的FieldError紀錄
		public BindingResult removeFieldError(EmpVO empVO, BindingResult result, String removedFieldname) {
			List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
					.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
					.collect(Collectors.toList());
			result = new BeanPropertyBindingResult(empVO, "empVO");
			for (FieldError fieldError : errorsListToKeep) {
				result.addError(fieldError);
			}
			return result;
		}
		
		
		//權限=======================================================================
		 @PostMapping("/{empId}/addFunction/{functionId}")
		    public void addFunctionToEmp(@PathVariable Integer empId, @PathVariable Integer functionId) {
			 empSvc.addFunctionToEmp(empId, functionId);
		    }

		    @DeleteMapping("/{empId}/removeFunction/{functionId}")
		    public void removeFunctionFromEmp(@PathVariable Integer empId, @PathVariable Integer functionId) {
		    	empSvc.removeFunctionFromEmp(empId, functionId);
		    }
		    
		//
		    

		    
		    @GetMapping("/loadPermissions")
		    public ResponseEntity<Set<FuncVO>> loadPermissions(@RequestParam Integer empId) {
		        EmpVO empVO = empSvc.getOneEmp(empId); // 獲取員工訊息,假設這裡是業務邏輯
		        if (empVO != null) {
		            return ResponseEntity.ok(empVO.getPermissions());
		        } else {
		            return ResponseEntity.notFound().build();
		        }
		    }
		    
		    
			@GetMapping("/update/{empId}")
			public String showUpdateForm(@PathVariable("empId") Integer empId, Model model) {
			    EmpVO empVO = empSvc.getOneEmp(empId);
			    model.addAttribute("empVO", empVO);
			    model.addAttribute("allFunctions", funcSvc.getAll()); // 傳遞所有功能
			    return "update_emp_input";
			}

}
