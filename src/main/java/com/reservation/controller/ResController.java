package com.reservation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.reservation.model.ResService;
import com.reservationcontrol.model.ResCService;
import com.reservationcontrol.model.ResCVO;
import com.sysargument.model.SysArgService;
import com.sysargument.model.SysArgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.reservation.model.ResVO;
import com.restime.model.ResTimeService;
import com.restime.model.ResTimeVO;
import com.tabletype.model.TableTypeService;
import com.tabletype.model.TableTypeVO;

@Controller
@Validated
@RequestMapping("/res")
public class ResController {
	@Autowired
	ResService ResSvc;
	@Autowired
	MemService MemSvc;
	@Autowired
	ResTimeService ResTimeSvc;
	@Autowired
	TableTypeService TableTypeSvc;
	@Autowired
	SysArgService SysArgSvc;
	@Autowired
	ResCService ResCSvc;

	@GetMapping("/all")
	public String getAllReservations(HttpSession session, Model model) {
		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO != null) {
			List<ResVO> resListData = ResSvc.getMemRes(memVO);
			model.addAttribute("resListData", resListData);
			System.out.println(resListData);
		} else {
			return "front-end/mem/signup";
		}

		return "front-end/res/listMemRes"; // 返回對應的 Thymeleaf 模板名稱
	}
	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
			/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
			
			@RequestParam("reservationId") String reservationId,
			ModelMap model) {
			
			/***************************2.開始查詢資料*********************************************/
//			EmpService empSvc = new EmpService();
			ResVO resVO = ResSvc.getOneRes(Integer.valueOf(reservationId));
			
			List<ResVO> list = ResSvc.getAll();
			model.addAttribute("resListData", list); // for select_page.html 第97 109行用
			
			if (resVO == null) {
				model.addAttribute("errorMessage", "查無資料");
				return "back-end/res/select_page";
			}
			
			/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
			model.addAttribute("resVO", resVO);
			model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第126行 -->
			
//			
			return "back-end/res/select_page"; // 查詢完成後轉交select_page.html由其第128行insert listOneEmp.html內的th:fragment="listOneEmp-div
		}
	
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(HttpSession session,@RequestParam("reservationId") Integer reservationId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/


		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService(); //autowired
		ResVO resVO = ResSvc.getOneRes(Integer.valueOf(reservationId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("resVO", resVO);
		session.setAttribute("resVO", resVO);
		return "front-end/res/update_res_input"; // 查詢完成後轉交update_emp_input.html
	}


	@PostMapping("update")
	public String update(HttpSession session,@Valid ResVO resVO, BindingResult result, ModelMap model

			) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		resVO.setReservationDate(LocalDateTime.now());
		List<SysArgVO> sysArgVOList2 = SysArgSvc.findByColumns("2persontable");
		List<SysArgVO> sysArgVOList4 = SysArgSvc.findByColumns("4persontable");
		ResVO oldResVO = session.getAttribute("resVO") != null ? (ResVO) session.getAttribute("resVO") : new ResVO();

		List<ResCVO> resCVOList = ResCSvc.findByColumns(oldResVO.getReservationEatdate(), oldResVO.getTableTypeVO());
		ResCVO resCVO = resCVOList.get(0);
		System.out.println("吃飯時段"+oldResVO.getResTimeVO().getReservationTimeId());
		resCVO.setReservationControlTable(ResSvc.restoreset(
				resCVO.getReservationControlTable(),
				oldResVO.getReservationTable(),
				oldResVO.getResTimeVO().getReservationTimeId()));//恢復座位數量
		System.out.println("resC數量" +resCVO.getReservationControlTable());
		List<ResCVO>resCVOList2 = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());

		ResCVO resCVO2 = resCVOList2.get(0); // Assuming you want the first element
		System.out.println("吃飯時段"+resVO.getResTimeVO().getReservationTimeId());
		String argumentValue = resCVO2.getTableTypeVO().getTableId() == 1 ?
				sysArgVOList2.get(0).getSysArgumentValue() :
				sysArgVOList4.get(0).getSysArgumentValue();

		resCVO.setReservationControlTable(ResSvc.compareLastTwoDigits(
				argumentValue,
				resCVO2.getReservationControlTable(),
				resVO.getReservationTable(),
				resVO.getResTimeVO().getReservationTimeId()
		));

		//測試列印出來
		System.out.println("日期"+resVO.getReservationEatdate());
		System.out.println("數量" +resVO.getReservationTable());


		System.out.println("resC編號"+resCVOList.get(0).getReservationControlId());
		System.out.println("resC日期"+resCVOList.get(0).getReservationControlDate());
		System.out.println("resC數量" +resCVOList.get(0).getReservationControlTable());

		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ResSvc.updateRes(resVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		resVO = ResSvc.getOneRes(Integer.valueOf(resVO.getReservationId()));
		model.addAttribute("resVO", resVO);
		return "front-end/res/listOneRes"; // 修改成功後轉交listOneEmp.html
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
