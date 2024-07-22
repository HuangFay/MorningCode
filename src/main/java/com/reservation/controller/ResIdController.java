package com.reservation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Table;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.reservation.model.ResService;
import com.reservationcontrol.model.ResCService;
import com.reservationcontrol.model.ResCVO;
import com.sysargument.model.SysArgService;
import com.sysargument.model.SysArgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
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
	ResCService ResCSvc;
	@Autowired
	MemService MemSvc;
	@Autowired
	ResTimeService ResTimeSvc;
	@Autowired
	TableTypeService TableTypeSvc;
	@Autowired
	SysArgService SysArgSvc;

	@GetMapping("addRes")
	public String addRes(HttpSession session,ModelMap model) {
		// 從 session 中獲取 memVO

		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO == null) {
			return "redirect:/front-end/mem/signup";
		}
		ResVO resVO = new ResVO();
		resVO.setMemVO(memVO);
		model.addAttribute("memName", memVO.getMemName());
	    // 創建 ResVO 對象並設置 memVO


	    // 將 resVO 添加到模型中
	    model.addAttribute("resVO", resVO);

	    return "front-end/res/addRes";
	}


	@PostMapping("insert")
	@ResponseBody
	public Map<String, String> insert(HttpSession session, @Valid @ModelAttribute ResVO resVO, BindingResult result, ModelMap model,
									  @RequestParam(name = "tableTypeVO.tableId") Integer tableId)
			throws IOException {

		Map<String, String> response = new HashMap<>();

		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO == null) {
			response.put("status", "error");
			response.put("message", "未登入，請先註冊或登入。");
			return response;
		}
//		System.out.println("tableid 是"+tableId);
		TableTypeVO tableTypeVO = TableTypeSvc.getOneTableType(tableId);
		resVO.setTableTypeVO(tableTypeVO);
//		System.out.println("ＶＯ是"+ memVO.toString()+"電話是"+memVO.getMemPhone());
		resVO.setMemVO(memVO);
		resVO.setReservationDate(LocalDateTime.now());
		Integer tableuse=resVO.getReservationNum()/tableTypeVO.getTableType();

		if(resVO.getReservationNum()%tableTypeVO.getTableType()!=0) {
			tableuse++;
		}
		resVO.setReservationTable(tableuse);
		System.out.println("計算出的桌數"+tableuse);

//		System.out.println("tabletypeVO 等於 " + resVO.getTableTypeVO().toString());
//		System.out.println("桌型的ＩＤ" + resVO.getTableTypeVO().getTableId().getClass().getName());
//		System.out.println("人數多少" + resVO.getTableTypeVO().getTableType());
//		System.out.println("選的時段ＩＤ" + resVO.getResTimeVO().getReservationTimeId());
//		System.out.println("選的時段" + resVO.getResTimeVO().getReservationTime());

		List<ResCVO> resCVOList = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
		List<SysArgVO> sysArgVOList2 = SysArgSvc.findByColumns("2persontable");
		List<SysArgVO> sysArgVOList4 = SysArgSvc.findByColumns("4persontable");

		if (!resCVOList.isEmpty()) { // 如果有找到符合的資料
			ResCVO resCVO = resCVOList.get(0);
			String argumentValue = resVO.getTableTypeVO().getTableType() == 2 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			String updadesit = ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			);

			if (!updadesit.equals(resCVO.getReservationControlTable())) {
				resCVO.setReservationControlTable(updadesit);
				response.put("status", "success");
				response.put("message", "訂位成功");
			} else {
				response.put("status", "error");
				response.put("message", "座位已滿請選取其他時段！");
			}
		} else {//沒找到要創建新的控制表
			ResCVO resCaddVO = new ResCVO();
			resCaddVO.setReservationControlDate(resVO.getReservationEatdate());
			resCaddVO.setTableTypeVO(resVO.getTableTypeVO());
			ResCSvc.addRes(resCaddVO);
			resCVOList = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
			ResCVO resCVO = resCVOList.get(0);
			String argumentValue = resVO.getTableTypeVO().getTableType() == 2 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			String updadesit = ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			);

			if (!updadesit.equals(resCVO.getReservationControlTable())) {
				resCVO.setReservationControlTable(updadesit);
				response.put("status", "success");
				response.put("message", "訂位成功");
			} else {
				response.put("status", "error");
				response.put("message", "座位已滿請選取其他時段！");
			}
		}

		if ("success".equals(response.get("status"))) {
			ResSvc.addRes(resVO);
		}

		return response;
	}



	//--------------------------------------------------------------------
//	//轉道自己所發的文章的mapping
//    @GetMapping("listMyArticles")
//    public String listMyArticles(HttpSession session, Model model) {
//      MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
//         if (loggedInMember == null) {
//             return "redirect:/front-end/member/loginMem";
//         }
//
//         List<ArticleVO> myArticles = articleSvc.getAllIncludingStatusZero().stream()
//                 .filter(article -> article.getMemberVO().getMemNo().equals(loggedInMember.getMemNo()))
//                 .collect(Collectors.toList());
//
//         model.addAttribute("myArticles", myArticles);
//         return "front-end/article/listMyArticles";
//    }
	//--------------------------------------------------------------------
	
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