package com.morning.leave.controller;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.morning.assign.model.AssignService;
import com.morning.assign.model.AssignVO;
import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;
import com.morning.leave.model.LeaveService;
import com.morning.leave.model.LeaveVO;

@Controller
@RequestMapping("/back-end/leave")
public class LeaveController {

	@Autowired
	LeaveService leaveSvc;

	@Autowired
	EmpService empSvc;

	@Autowired
	AssignService assignSvc;

	// 填寫假單、代班人員過濾掉登入者
	@GetMapping("/addLeave")
	public String addLeave(ModelMap model, HttpSession session) {
		LeaveVO leaveVO = new LeaveVO();
		model.addAttribute("leaveVO", leaveVO);

		EmpVO loggedInEmp = (EmpVO) session.getAttribute("empVO");
		if (loggedInEmp != null) {
			List<EmpVO> filteredEmpList = empSvc.getAll().stream()
					.filter(emp -> !emp.getEmpId().equals(loggedInEmp.getEmpId())).collect(Collectors.toList());
			model.addAttribute("empListData", filteredEmpList);

	        // 獲取該員工的上班日期
	        List<Date> workingDates = assignSvc.getWorkingDatesByEmpId(loggedInEmp.getEmpId());
	        model.addAttribute("workingDates", workingDates.stream()
	                .map(date -> date.toString())  // 將日期轉換為字符串
	                .collect(Collectors.toList()));
		} else {
			model.addAttribute("empListData", empSvc.getAll()); // 如果沒有登入資訊，則返回全部員工名單
		}
		return "back-end/leave/addLeave";
	}

	// 獲取請假員工帳號並帶入
	@PostMapping("/insert")
	public String insert(@Valid LeaveVO leaveVO, HttpSession session, BindingResult result, ModelMap model)
			throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
			model.addAttribute("leaveVO", leaveVO);
			return "back-end/leave/addLeave";
		}

		// 獲取登錄的員工 addLeave.html line95
		EmpVO loggedInEmp = (EmpVO) session.getAttribute("empVO");

		if (loggedInEmp != null) {
			// 設置 leaveVO 的 leaveEmpId
			leaveVO.setLeaveEmpId(loggedInEmp);

			Integer empId = loggedInEmp.getEmpId();
			List<LeaveVO> leaveListData = leaveSvc.getLeavesByEmpId(empId);
			model.addAttribute("leaveListData", leaveListData);
			model.addAttribute("loggedInEmp", loggedInEmp); // 增加這一行來傳遞登入員工資料到前端
		}

		/*************************** 2.開始新增資料 *****************************************/
		leaveSvc.addLeave(leaveVO);

		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<LeaveVO> list = leaveSvc.getAll();
		model.addAttribute("leaveListData", list);
		model.addAttribute("success", "- (新增成功)");

		return "redirect:/back-end/leave/listAllLeaveforEmp"; // 新增成功後重導至列表頁面
	}

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

		return "back-end/leave/listOneLeave";
	}

	// 撤回申請表單
	@PostMapping("delete")
	public String delete(@RequestParam("leaveId") String leaveId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		leaveSvc.deleteLeave(Integer.valueOf(leaveId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<LeaveVO> list = leaveSvc.getAll();
		model.addAttribute("leaveListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "redirect:/back-end/leave/listAllLeaveforEmp"; // 刪除完成後轉交listAllEmp.html
	}

	// 審核判斷、將leave的empId帶入到assign的leaveAssigneeId
	@PostMapping("/updateapproval")
	public String updateApproval(@RequestParam(value = "leaveId") Integer leaveId,
	                             @RequestParam(value = "leaveStatus", required = false) Byte leaveStatus,
	                             @RequestParam(value = "leaveStatusforEmp", required = false) Byte leaveStatusforEmp,
	                             @RequestParam(value = "isSubstituteApproval", required = false) Boolean isSubstituteApproval,
	                             ModelMap model) {
	    LeaveVO leaveVO = leaveSvc.getOneLeave(leaveId);

	    if (leaveVO != null) {
	        // 首先處理代班簽核狀態
	        if (leaveStatusforEmp != null) {
	            leaveVO.setLeaveStatusforEmp(leaveStatusforEmp);
	        }
	        
	        // 如果代班簽核狀態為1（簽核通過）且管理員也進行了簽核
	        if (leaveVO.getLeaveStatusforEmp() == 1 && leaveStatus != null) {
	            if (leaveStatus == 1) {
	                leaveVO.approveLeave(); // 管理員審核通過
	                // 更新 Assign
	                List<AssignVO> assignList = assignSvc.getAssignmentsByDate(leaveVO.getLeaveDate());
	                for (AssignVO assign : assignList) {
	                    if (assign.getAssignDate().equals(leaveVO.getLeaveDate())) {
	                        if (assign.getEmpVO().equals(leaveVO.getLeaveEmpId())) {
	                            assign.setEmpVO(leaveVO.getLeaveAssigneeId());
	                            assignSvc.updateAssign(assign);
	                        } else if (assign.getEmpVO1().equals(leaveVO.getLeaveEmpId())) {
	                            assign.setEmpVO1(leaveVO.getLeaveAssigneeId());
	                            assignSvc.updateAssign(assign);
	                        }
	                    }
	                }
	            } else if (leaveStatus == 2) {
	                leaveVO.rejectLeave(); // 管理員審核不通過
	            }
	        } else if (leaveVO.getLeaveStatusforEmp() == 2) { // 代班簽核不通過
	            leaveVO.setLeaveStatus((byte) 2); // 整個請假申請不通過
	        }
	        
	        leaveSvc.updateLeave(leaveVO); // 更新請假紀錄
	    }


		model.addAttribute("success", "- (修改成功)");
		model.addAttribute("leaveVO", leaveVO);

		// 根據是否為代班簽核決定跳轉的頁面
	    if (isSubstituteApproval != null && isSubstituteApproval) {
	        return "redirect:/back-end/leave/listAllLeaveforEmp";
	    } else {
	        return "redirect:/back-end/leave/listAllLeave";
	    }
	}

	@GetMapping("/getDetails")
	@ResponseBody
	public LeaveVO getLeaveDetails(@RequestParam("leaveId") Integer leaveId) {
	    return leaveSvc.getOneLeave(leaveId);
	}
	
	
	@ModelAttribute("empListData")
	protected List<EmpVO> referenceListData() {
		List<EmpVO> list = empSvc.getAll();
		return list;
	}

}