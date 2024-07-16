package com.reservation.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.reservationcontrol.model.ResCService;
import com.reservationcontrol.model.ResCVO;
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
	ResCService ResCSvc;
	@Autowired
	MemService MemSvc;
	@Autowired
	ResTimeService ResTimeSvc;
	@Autowired
	TableTypeService TableTypeSvc;
	
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
		resVO.getTableTypeVO().getTableId();
		resVO.getReservationTable();


		List<ResCVO> resCVOList = ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
		if (!resCVOList.isEmpty()) {
			ResCVO resCVO = resCVOList.get(0); // Assuming you want the first element
			resCVO.setReservationControlTable(resVO.getReservationTable().toString());
			// Additional logic here
		}


//		ResCVO resCVO = (ResCVO) ResCSvc.findByColumns(resVO.getReservationEatdate(), resVO.getTableTypeVO());
//		resCVO.setReservationControlTable(resVO.getReservationTable().toString());


//		if (resVO.getTableTypeVO().getTableId()==resCVO.getTableTypeVO().getTableId()
//				&& resVO.getReservationEatdate() == resCVO.getReservationControlDate())
//		{resCVO.setReservationControlDate(resVO.getReservationEatdate());}
		System.out.println("日期"+resVO.getReservationEatdate());
		System.out.println("數量" +resVO.getReservationTable());


		System.out.println("編號"+resCVOList.get(0).getReservationControlId());
		System.out.println("日期"+resCVOList.get(0).getReservationControlDate());
		System.out.println("數量" +resCVOList.get(0).getReservationControlTable());

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