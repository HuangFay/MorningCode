package com.reservation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

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

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.reservation.model.ResService;
import com.reservation.model.ResVO;
import com.restime.model.ResTimeService;
import com.restime.model.ResTimeVO;
import com.tabletype.model.TableTypeService;
import com.tabletype.model.TableTypeVO;

@Controller
@RequestMapping("/front-end/res")
public class ResIdController {
	@Autowired
	ResService ResSvc;

	@Autowired
	MemService MemSvc;
	@Autowired
	ResTimeService ResTimeSvc;
	@Autowired
	TableTypeService TableTypeSvc;
	
	@GetMapping("addRes")
	public String addEmp(HttpSession session,ModelMap model) {
		// 從 session 中獲取 memVO
	    MemVO memVO = (MemVO) session.getAttribute("memVO");
	    if (memVO == null) {
	        memVO = new MemVO();
	        // 假設您有一個方法來初始化 memVO
	        session.setAttribute("memVO", memVO);
	    }

	    // 創建 ResVO 對象並設置 memVO
	    ResVO resVO = new ResVO();
	    resVO.setMemVO(memVO);

	    // 將 resVO 添加到模型中
	    model.addAttribute("resVO", resVO);

	    return "front-end/res/addRes";
	}
	
	
	@PostMapping("insert")
	public String insert(HttpSession session,@Valid ResVO resVO, BindingResult result, ModelMap model) throws IOException{
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		MemVO memVO = (MemVO) session.getAttribute("memVO");
	    if (memVO == null) {
	        memVO = new MemVO();
	        // 假設您有一個方法來初始化 memVO
	        session.setAttribute("memVO", memVO);
	    }
		resVO.setReservationDate(LocalDateTime.now());
	
		
		
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ResSvc.addRes(resVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ResVO> list = ResSvc.getAll();
		model.addAttribute("resListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/index2"; // 新增成功後重導至
	
		
		
	}
	
	@ModelAttribute("memListData")
	protected List<MemVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<MemVO> list = MemSvc.getAll();
		return list;
	}
	
	@ModelAttribute("resTimeListData")
	protected List<ResTimeVO> resTimeListData() {
		// DeptService deptSvc = new DeptService();
		List<ResTimeVO> list = ResTimeSvc.getAll();
		return list;
	}
	
	@ModelAttribute("tableTypeListData")
	protected List<TableTypeVO> tabelTypeListData() {
		// DeptService deptSvc = new DeptService();
		List<TableTypeVO> list = TableTypeSvc.getAll();
		return list;
	}
	
	
	
	
}