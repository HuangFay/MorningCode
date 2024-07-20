package com.morning.emp.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;


//用於處理 RESTful API 請求,直接返回資料（例如 JSON、XML）。
//加上@ResponseBody 註解来返回資料
@RestController
@RequestMapping("/api")
public class EmpVerifyController {
	
	
	@Autowired
	private EmpService empSvc;
	
	
//	登入===============================================================================================
	@PostMapping("/emplogin")
//	@PostMapping(value = "/emplogin", consumes = "application/json", produces = "application/json")
	public ResponseEntity<String> loginEmp(@RequestBody LoginRequest loginRequest,HttpSession session){
		 try {
			 	//	確保只有經過驗證的用戶可以成功登入並訪問受保護的內容或功能
	            boolean isAuthenticated = empSvc.authenticateEmp(loginRequest.getEmpAccount(), loginRequest.getEmpPassword());
	            
	            if (isAuthenticated) {
	            	
//	            	MemService從服務層的方法,用email去獲取其他資料會員資訊賦值給 authenticatedMember 變數。
	            	 EmpVO authenticatedEmp = empSvc.getEmpByAccount(loginRequest.getEmpAccount());
//	            	 然後將上一行的接收到的memVO物件存儲在session,使用"memVO"作為KEY
	            	 authenticatedEmp.getFunctions().size();
	            	 session.setAttribute("empPermissions", authenticatedEmp.getFunctions());
	            	 session.setAttribute("empVO", authenticatedEmp);
	            	 String redirectLogin = (String) session.getAttribute("location");
//	            	 String front = "/index3";
//	            	session.setAttribute("empVO", empVO);// 多餘的
	            	 if (redirectLogin != null ) {
		 					session.removeAttribute("redirectLogin");
		 					return ResponseEntity.ok(redirectLogin); // 導到存URL的位置
		 				} else {
		 					 String contextPath = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		 					  return ResponseEntity.ok(contextPath + "/index3"); //首頁搂
		 				}
		 			} else {
		 				return ResponseEntity.status(401).body("帳號或密碼錯誤");
		 			}
		 		} catch (Exception e) {
		 			return ResponseEntity.status(500).body("登入失敗：" + e.getMessage());
		 		}
	}

	
	private static class LoginRequest {
	    private String empAccount;
	    private String empPassword;
		public String getEmpAccount() {
			return empAccount;
		}
		public void setEmpAccount(String empAccount) {
			this.empAccount = empAccount;
		}
		public String getEmpPassword() {
			return empPassword;
		}
		public void setEmpPassword(String empPassword) {
			this.empPassword = empPassword;
		}
		
	}
	
	
//  登出=====================================================================================

}
