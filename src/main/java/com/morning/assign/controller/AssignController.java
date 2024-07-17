package com.morning.assign.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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


@Controller
@RequestMapping("/back-end/assign")
public class AssignController {

	@Autowired
	AssignService assignSvc;

	@Autowired
	EmpService empSvc;
	
    @GetMapping("/api/employees")
    @ResponseBody
    public Map<String, List<EmpVO>> getEmployees() {
        List<EmpVO> employees = empSvc.getAll();
        Map<String, List<EmpVO>> response = new HashMap<>();
        response.put("employees", employees);
        return response;
    }

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("/addAssign")
	public String addAssign(ModelMap model) {
	    AssignVO assignVO = new AssignVO();
	    List<EmpVO> List = empSvc.getAll();
	    model.addAttribute("assignVO", assignVO);
	    model.addAttribute("empListData", List);
	    return "back-end/assign/addAssign";
	}

	//取得每月的排班資料
	@GetMapping("/api/getMonthlyAssign")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getMonthlyAssign(@RequestParam("year") int year, @RequestParam("month") int month) {
	    try {
	        List<AssignVO> monthlyAssigns = assignSvc.getMonthlyAssigns(year, month);
	        List<Map<String, Object>> response = new ArrayList<>();
	        for (AssignVO assign : monthlyAssigns) {
	            Map<String, Object> assignMap = new HashMap<>();
	            assignMap.put("assignDate", assign.getAssignDate().toString());
	            assignMap.put("empVO", assign.getEmpVO());
	            assignMap.put("empVO1", assign.getEmpVO1());
	            response.add(assignMap);
	        }
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
	    }
	}


	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 
	 */
	
	//保存排班
	@PostMapping("/insert")
	@ResponseBody
	public ResponseEntity<Map<String, String>> insertAssignments(@RequestParam("dates[]") List<String> dates, 
	                                                             @RequestParam("employees1[]") List<Integer> employees1,
	                                                             @RequestParam("employees2[]") List<Integer> employees2) {
	    Map<String, String> response = new HashMap<>();
	    try {
	        for (int i = 0; i < dates.size(); i++) {
	            AssignVO assignVO = new AssignVO();
	            assignVO.setAssignDate(Date.valueOf(dates.get(i)));  // 轉換日期格式

	            EmpVO empVO1 = new EmpVO();
	            empVO1.setEmpId(employees1.get(i));
	            assignVO.setEmpVO(empVO1);

	            EmpVO empVO2 = new EmpVO();
	            empVO2.setEmpId(employees2.get(i));
	            assignVO.setEmpVO1(empVO2);

	            assignSvc.addAssign(assignVO);
	        }
	        response.put("message", "排班保存成功");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("error", "保存失敗：" + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}
	
	
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("assignId") String assignId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
	

		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService assignSvc = new EmpService();
		AssignVO assignVO = assignSvc.getOneAssign(Integer.valueOf(assignId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("assignVO", assignVO);
		return "back-end/assign/update_assign_input"; // 查詢完成後轉交update_emp_input.html
	}

	//編輯排班
	@PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateAssignments(@RequestParam("dates[]") List<String> dates, 
                                                                 @RequestParam("employees1[]") List<Integer> employees1,
                                                                 @RequestParam("employees2[]") List<Integer> employees2) {
        Map<String, String> response = new HashMap<>();
        try {
            for (int i = 0; i < dates.size(); i++) {
                AssignVO assignVO = assignSvc.getAssignByDate(Date.valueOf(dates.get(i))).get(0); // 假設每個日期只有一條記錄
                
                	 EmpVO empVO = new EmpVO();
                     empVO.setEmpId(employees1.get(i));
                     assignVO.setEmpVO(empVO);

                     EmpVO empVO1 = new EmpVO();
                     empVO1.setEmpId(employees2.get(i));
                     assignVO.setEmpVO1(empVO1);

                     assignSvc.updateAssign(assignVO);
                
            }
            response.put("message", "排班更新成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "更新失敗：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("assignId") String assignId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService assignSvc = new EmpService();
		assignSvc.deleteAssign(Integer.valueOf(assignId));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<AssignVO> list = assignSvc.getAll();
		model.addAttribute("assignListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/assign/listAllAssign"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("empListData")
	protected List<EmpVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<EmpVO> list = empSvc.getAll();
		return list;
	}
	



	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("deptMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(10, "財務部");
//		map.put(20, "研發部");
//		map.put(30, "業務部");
//		map.put(40, "生管部");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
//	public BindingResult removeFieldError(AssignVO AssignVO, BindingResult result, String removedFieldname) {
//		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
//				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
//				.collect(Collectors.toList());
//		result = new BeanPropertyBindingResult(AssignVO, "AssignVO");
//		for (FieldError fieldError : errorsListToKeep) {
//			result.addError(fieldError);
//		}
//		return result;
//	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
//	@PostMapping("listAssigns_ByCompositeQuery")
//	public String listAllAssign(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<AssignVO> list = assignSvc.getAll(map); 
//		model.addAttribute("assignListData", list); // for listAllEmp.html 第85行用
//		return "back-end/assign/listAllAssign";
//	}

}