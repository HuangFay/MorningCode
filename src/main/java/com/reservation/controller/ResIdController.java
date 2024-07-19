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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String insert(@Valid ResVO resVO, BindingResult result, ModelMap model) throws IOException{
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		resVO.setReservationDate(LocalDateTime.now());
//		resVO.getTableTypeVO().getTableId();
//		resVO.getReservationTable();
		/*
		 *前端給 吃飯日期  桌位數量 桌位類型 （ＯＫ）
		 * 用吃飯時間+桌位類型 找出ResCVO的 控制桌位數量  如果無此天資料就創一個（ＯＫ）
		 * 使用service 方法驗證數量 回傳字串儲存回資料庫
		 *
		 *
		 */

		List<ResCVO> resCVOList = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
		List<SysArgVO> sysArgVOList2 = SysArgSvc.findByColumns("2persontable");
		List<SysArgVO> sysArgVOList4 = SysArgSvc.findByColumns("4persontable");
		if (!resCVOList.isEmpty()) {
			ResCVO resCVO = resCVOList.get(0); // Assuming you want the first element
			String argumentValue = resVO.getTableTypeVO().getTableId() == 1 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			resCVO.setReservationControlTable(ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			));



		}else{
			//創建新的控制日期選項
			ResCVO resCaddVO = new ResCVO();
			resCaddVO.setReservationControlDate(resVO.getReservationEatdate());
			resCaddVO.setTableTypeVO(resVO.getTableTypeVO());

			ResCSvc.addRes(resCaddVO);

			resCVOList = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());


			ResCVO resCVO = resCVOList.get(0); // Assuming you want the first element
			String argumentValue = resVO.getTableTypeVO().getTableId() == 1 ?
					sysArgVOList2.get(0).getSysArgumentValue() :
					sysArgVOList4.get(0).getSysArgumentValue();

			resCVO.setReservationControlTable(ResSvc.compareLastTwoDigits(
					argumentValue,
					resCVO.getReservationControlTable(),
					resVO.getReservationTable(),
					resVO.getResTimeVO().getReservationTimeId()
			));
		}
		//取得設定桌位數




		//測試列印出來
//		System.out.println("日期"+resVO.getReservationEatdate());
//		System.out.println("數量" +resVO.getReservationTable());
//		System.out.println("訂位字串"+sysArgVOList2.get(0).getSysArgumentValue());
//
//		System.out.println("resC編號"+resCVOList.get(0).getReservationControlId());
//		System.out.println("resC日期"+resCVOList.get(0).getReservationControlDate());
//		System.out.println("resC數量" +resCVOList.get(0).getReservationControlTable());

		/*************************** 2.開始新增資料 *****************************************/

		// EmpService empSvc = new EmpService();
		ResSvc.addRes(resVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ResVO> list = ResSvc.getAll();
		model.addAttribute("resListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/index2"; // 新增成功後重導至
	
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