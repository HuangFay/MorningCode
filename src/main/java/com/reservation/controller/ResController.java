package com.reservation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.reservation.model.ResService;
import com.reservationcontrol.model.ResCService;
import com.reservationcontrol.model.ResCVO;
import com.sysargument.model.SysArgService;
import com.sysargument.model.SysArgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	
	@GetMapping("getOne_For_Display")
	public String getOne_For_Display(
			/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
			HttpSession session,
			@RequestParam("reservationId") String reservationId,
			@RequestParam(name = "memVO.memId") Integer memId,
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
			// EmpService empSvc = new EmpService(); //autowired

		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO == null) {
			return "redirect:/front-end/mem/signup";
		}

		ResVO resVO = new ResVO();
		resVO.setMemVO(memVO);
		model.addAttribute("memName", memVO.getMemName());
		 resVO = ResSvc.getOneRes(Integer.valueOf(reservationId));

		model.addAttribute("resVO", resVO);
		session.setAttribute("resVO", resVO);
		model.addAttribute("memVO", memVO);
		return "front-end/res/update_res_input"; // 查詢完成後轉交update_emp_input.html
	}
	@PostMapping("backendupdate")
	public String back_getOne_For_Update(HttpSession session,@RequestParam("reservationId") Integer reservationId, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/


		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService(); //autowired
		ResVO resVO = ResSvc.getOneRes(Integer.valueOf(reservationId));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("resVO", resVO);
		session.setAttribute("resVO", resVO);
		return "back-end/res/update_res_input"; // 查詢完成後轉交update_emp_input.html
	}


	@PostMapping("backupdate")
	@ResponseBody
	public Map<String, String> backupdate(HttpSession session,@Valid ResVO resVO, BindingResult result, ModelMap model,@RequestParam(name = "tableTypeVO.tableId") Integer tableId
//			,@RequestParam("upFiles") MultipartFile[] parts
	) throws IOException {
		Map<String, String> response = new HashMap<>();
		if (result.hasErrors()) {
			response.put("status", "failure1");
			response.put("message", "輸入格式有誤");
			return response;
		}

		resVO.setReservationDate(LocalDateTime.now());
		List<SysArgVO> sysArgVOList2 = SysArgSvc.findByColumns("2persontable");
		List<SysArgVO> sysArgVOList4 = SysArgSvc.findByColumns("4persontable");
		ResVO oldResVO = session.getAttribute("resVO") != null ? (ResVO) session.getAttribute("resVO") : new ResVO();

		List<ResCVO> resCVOList = ResCSvc.findByColumns(oldResVO.getReservationEatdate(), oldResVO.getTableTypeVO());
		ResCVO resCVO = resCVOList.get(0);
		System.out.println("吃飯時段" + oldResVO.getResTimeVO().getReservationTimeId());
		resCVO.setReservationControlTable(ResSvc.restoreset(
				resCVO.getReservationControlTable(),
				oldResVO.getReservationTable(),
				oldResVO.getResTimeVO().getReservationTimeId())); // 恢復座位數量
		System.out.println("resC數量" + resCVO.getReservationControlTable());
		TableTypeVO tableTypeVO = TableTypeSvc.getOneTableType(tableId);
		resVO.setTableTypeVO(tableTypeVO);
//
		Integer tableuse=resVO.getReservationNum()/tableTypeVO.getTableType();

		if(resVO.getReservationNum()%tableTypeVO.getTableType()!=0) {
			tableuse++;
		}
		resVO.setReservationTable(tableuse);
		System.out.println("計算出的桌數"+tableuse);

		List<ResCVO> resCVOList2 = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
		if (!resCVOList2.isEmpty()){
			ResCVO resCVO2 = resCVOList2.get(0); // Assuming you want the first element
			System.out.println("吃飯時段" + resVO.getResTimeVO().getReservationTimeId());
			System.out.println(resCVO2.getTableTypeVO().toString());

			String argumentValue = resCVO2.getTableTypeVO().getTableType() == 2 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			String updatesit = ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO2.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			);

			if (!updatesit.equals(resCVO2.getReservationControlTable())) {
				resCVO2.setReservationControlTable(updatesit);
				response.put("status", "success");
				response.put("message", "修改成功");
			} else {
				response.put("status", "error");
				response.put("message", "座位已滿請選取其他時段！");
				return response;
			}
		}else{
			ResCVO resCVO2 = new ResCVO();
			resCVO2.setTableTypeVO(resVO.getTableTypeVO());
			resCVO2.setReservationControlDate(resVO.getReservationEatdate());
			ResCSvc.addRes(resCVO2);
			resCVOList2=ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
			resCVO2 = resCVOList2.get(0); // Assuming you want the first element
			String argumentValue = resCVO2.getTableTypeVO().getTableType() == 2 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			String updatesit = ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO2.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			);

			if (!updatesit.equals(resCVO2.getReservationControlTable())) {
				resCVO2.setReservationControlTable(updatesit);
				response.put("status", "success");
				response.put("message", "修改成功");
			} else {
				response.put("status", "error");
				response.put("message", "座位已滿請選取其他時段！");
				return response;
			}
		}

		// 開始修改資料
		ResSvc.updateRes(resVO);
		return response;
	}


	@PostMapping("update")
	@ResponseBody
	public Map<String, String> update(HttpSession session, @Valid ResVO resVO, BindingResult result,@RequestParam(name = "tableTypeVO.tableId") Integer tableId) throws IOException {
		Map<String, String> response = new HashMap<>();

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
			response.put("status", "failure1");
			response.put("message", "輸入格式有誤");
			return response;
		}

		resVO.setReservationDate(LocalDateTime.now());
		List<SysArgVO> sysArgVOList2 = SysArgSvc.findByColumns("2persontable");
		List<SysArgVO> sysArgVOList4 = SysArgSvc.findByColumns("4persontable");
		ResVO oldResVO = session.getAttribute("resVO") != null ? (ResVO) session.getAttribute("resVO") : new ResVO();

		List<ResCVO> resCVOList = ResCSvc.findByColumns(oldResVO.getReservationEatdate(), oldResVO.getTableTypeVO());
		ResCVO resCVO = resCVOList.get(0);
		System.out.println("吃飯時段" + oldResVO.getResTimeVO().getReservationTimeId());
		resCVO.setReservationControlTable(ResSvc.restoreset(
				resCVO.getReservationControlTable(),
				oldResVO.getReservationTable(),
				oldResVO.getResTimeVO().getReservationTimeId())); // 恢復座位數量
		System.out.println("resC數量" + resCVO.getReservationControlTable());
		TableTypeVO tableTypeVO = TableTypeSvc.getOneTableType(tableId);
		resVO.setTableTypeVO(tableTypeVO);
//
		Integer tableuse=resVO.getReservationNum()/tableTypeVO.getTableType();

		if(resVO.getReservationNum()%tableTypeVO.getTableType()!=0) {
			tableuse++;
		}
		resVO.setReservationTable(tableuse);
		System.out.println("計算出的桌數"+tableuse);

		List<ResCVO> resCVOList2 = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
		if (!resCVOList2.isEmpty()){
		ResCVO resCVO2 = resCVOList2.get(0); // Assuming you want the first element
		System.out.println("吃飯時段" + resVO.getResTimeVO().getReservationTimeId());
		System.out.println(resCVO2.getTableTypeVO().toString());

		String argumentValue = resCVO2.getTableTypeVO().getTableType() == 2 ?
				sysArgVOList2.get(0).getSysArgumentValue() :
				sysArgVOList4.get(0).getSysArgumentValue();

		String updatesit = ResSvc.compareLastTwoDigits(
				argumentValue,
				resCVO2.getReservationControlTable(),
				resVO.getReservationTable(),
				resVO.getResTimeVO().getReservationTimeId()
		);

		if (!updatesit.equals(resCVO2.getReservationControlTable())) {
			resCVO2.setReservationControlTable(updatesit);
			response.put("status", "success");
			response.put("message", "修改成功");
		} else {
			response.put("status", "error");
			response.put("message", "座位已滿請選取其他時段！");
			return response;
		}
		}else{
			ResCVO resCVO2 = new ResCVO();
			resCVO2.setTableTypeVO(resVO.getTableTypeVO());
			resCVO2.setReservationControlDate(resVO.getReservationEatdate());
			ResCSvc.addRes(resCVO2);
			resCVOList2=ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
			resCVO2 = resCVOList2.get(0); // Assuming you want the first element
			String argumentValue = resCVO2.getTableTypeVO().getTableType() == 2 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			String updatesit = ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO2.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			);

			if (!updatesit.equals(resCVO2.getReservationControlTable())) {
				resCVO2.setReservationControlTable(updatesit);
				response.put("status", "success");
				response.put("message", "修改成功");
			} else {
				response.put("status", "error");
				response.put("message", "座位已滿請選取其他時段！");
				return response;
			}
		}

		// 開始修改資料
		ResSvc.updateRes(resVO);
		return response;
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

	@PostMapping("/cancel")
	public String cancelReservation(@RequestParam("reservationId") Integer reservationId) {
		ResSvc.cancelReservation(reservationId);
		return "redirect:/res/all";

	}
}
