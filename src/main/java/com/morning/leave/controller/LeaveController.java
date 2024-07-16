package com.morning.leave.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;
import com.morning.leave.model.LeaveService;
import com.morning.leave.model.LeaveVO;


@Controller
@RequestMapping("/leave")
public class LeaveController {

	@Autowired
	LeaveService leaveSvc;

	@Autowired
	EmpService empSvc;

	/*
	 * This method will serve as addEmp.html handler
	 */
	@GetMapping("addLeave")
	public String addLeave(ModelMap model) {
		LeaveVO leaveVO = new LeaveVO();
		model.addAttribute("leaveVO", leaveVO);
		return "back-end/leave/addLeave";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid LeaveVO leaveVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		if (result.hasErrors()) {
			return "back-end/leave/listAllLeave";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService assignSvc = new EmpService();
		leaveSvc.addLeave(leaveVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<LeaveVO> list = leaveSvc.getAll();
		model.addAttribute("leaveListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/leave/listAllLeave"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("leaveId") String leaveId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
	

		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService assignSvc = new EmpService()
		LeaveVO leaveVO = leaveSvc.getOneLeave(Integer.valueOf(leaveId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("leaveVO", leaveVO);
		return "back-end/leave/update_leave_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid LeaveVO leaveVO, BindingResult result, ModelMap model) throws IOException {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
			return "back-end/leave/update_leave_input";
		}
	    if (leaveVO.getLeaveStatus() == 1) {
	        leaveVO.approveLeave();
	    }
//		/*************************** 2.開始修改資料 *****************************************/
		leaveSvc.updateLeave(leaveVO);
		

//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		leaveVO = leaveSvc.getOneLeave(Integer.valueOf(leaveVO.getLeaveId()));
		model.addAttribute("leaveVO", leaveVO);
		
		return "back-end/leave/listOneLeave"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("leaveId") String leaveId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		leaveSvc.deleteLeave(Integer.valueOf(leaveId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<LeaveVO> list = leaveSvc.getAll();
		model.addAttribute("leaveListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/leave/listAllLeave"; // 刪除完成後轉交listAllEmp.html
	}
	
	@PostMapping("updateapproval")
	public String updateApproval(@RequestParam("leaveId") Integer leaveId,
            					 @RequestParam("leaveStatus") Byte leaveStatus,
            					 ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		LeaveVO leaveVO = leaveSvc.getOneLeave(leaveId);
	    if (leaveVO != null) {
	        if (leaveStatus == 1) {
	            leaveVO.approveLeave(); // 審核通過
	        } else if (leaveStatus == 2) {
	            leaveVO.rejectLeave(); // 審核不通過
	         }else {
	        	 return "redirect:/back-end/leave/listAllLeave"; 
	         }
		/*************************** 2.開始修改資料 *****************************************/
		leaveSvc.updateLeave(leaveVO);
		  }

//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");

		model.addAttribute("leaveVO", leaveVO);
		
		return "redirect:/back-end/leave/listAllLeave"; 
	}
	
	@GetMapping("/listAllLeaveforEmp")
    public String listAllLeaveforEmp(HttpSession session, Model model) {
        // 從會話中獲取當前登入員工
        EmpVO loggedInEmp = (EmpVO) session.getAttribute("empVO");

        if (loggedInEmp != null) {
            List<LeaveVO> leaveListData = leaveSvc.getLeavesByEmpId(loggedInEmp.getEmpId());
            model.addAttribute("leaveListData", leaveListData);
        }

        return "back-end/leave/listAllLeave";
    }


	@ModelAttribute("empListData")
	protected List<EmpVO> referenceListData() {
		List<EmpVO> list = empSvc.getAll();
		return list;
	}
	

}