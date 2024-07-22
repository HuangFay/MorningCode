package com.morning.forum.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morning.forum.model.ForumPostVO;
import com.morning.forum.model.ForumReplyDTO;
import com.morning.forum.model.ForumReplyVO;
import com.morning.forum.model.ForumReportVO;
import com.morning.forum.model.ForumService;
import com.morning.forum.model.PostStatus;
import com.morning.forum.model.ReportStatus;
import com.morning.mem.model.MemVO;

@Controller
@RequestMapping("forum")
public class ForumController {

	@Autowired
	ForumService forumSvc;

	private final Validator validator;

	@Autowired
	public ForumController(Validator validator) {
		this.validator = validator;
	}
	
	@ModelAttribute("memVO")
	protected MemVO getMemVO(Model model, HttpSession session) {
		MemVO memVO = (MemVO) session.getAttribute("memVO");

		return memVO;
	}
	
	/* ========== 文章操作相關 ========== */
	// 前台 文章列表, 文章內容   /forum?postId=2
	@GetMapping("")
	public String listAllPost(
			@RequestParam(value = "postId", required = false)
			String postIdStr, HttpSession session,
			ModelMap model) {
		
		if (postIdStr != null) {
			Integer postId = Integer.valueOf(postIdStr);
			ForumPostVO forumPostVO = forumSvc.getOnePost(postId);
			model.addAttribute("forumPostVO", forumPostVO);
			return "front-end/forum/detail";
		}
		return "front-end/forum/listAllPost";
	}
	
	// 文章資料
	@ModelAttribute("forumPostListData")
	protected List<ForumPostVO> getForumPostListData(Model model) {

		return forumSvc.getAll();
	}

	// 前台 新增文章
	@GetMapping("addPost")
	public String addPost(ModelMap model) {
		ForumPostVO forumPostVO = new ForumPostVO();
		model.addAttribute("forumPostVO", forumPostVO);
		return "front-end/forum/addPost";
	}

	// 前台 新增文章
	@PostMapping("insert")
	public String insert(@Valid ForumPostVO forumPostVO, BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes,
			HttpSession session) throws IOException {

		result = removeFieldError(forumPostVO, result, "postTime");

		// 儲存現在的日期
		//Date sqlDate = new Date(System.currentTimeMillis());
		Timestamp now = new Timestamp(System.currentTimeMillis());
		forumPostVO.setPostTime(now);

		// 如果有錯誤，回到上一頁
		if (result.hasErrors()) {
			return "front-end/forum/addPost";
		}
		
		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO != null) {
			forumPostVO.setMemVO(memVO);
			forumPostVO.setPostStatus(PostStatus.SHOW.toInt());
			forumSvc.addPost(forumPostVO);

			List<ForumPostVO> list = forumSvc.getAll();
			model.addAttribute("forumPostListData", list);
			redirectAttributes.addFlashAttribute("successResult", "新增成功");
		} else {
			redirectAttributes.addFlashAttribute("successResult", "請先登入");
		}
		
		// 操作後重導至文章列表
		return "redirect:/forum/";
	}
	
	// 前台 編輯文章
	@PostMapping("editPost")
	public String editPost(@RequestParam("postId") String postId, ModelMap model) {

		ForumPostVO forumPostVO = forumSvc.getOnePost(Integer.valueOf(postId));
		model.addAttribute("forumPostVO", forumPostVO);

		return "front-end/forum/editPost";
	}

	// 編輯文章 操作 (api)
	@PostMapping("update")
	public String update(@Valid ForumPostVO forumPostVO, BindingResult result,
						RedirectAttributes redirectAttributes,
						ModelMap model){
		
		result = removeFieldError(forumPostVO, result, "postTime");

		// 如果有輸入內容有錯誤，回到上一頁
		if (result.hasErrors()) {
			return "front-end/forum/editPost";
		}
		
		forumSvc.updatePost(forumPostVO);

		forumPostVO = forumSvc.getOnePost(Integer.valueOf(forumPostVO.getPostId()));

		model.addAttribute("forumPostVO", forumPostVO);

		redirectAttributes.addFlashAttribute("successResult", "修改成功");
		return "redirect:/forum?postId=" + forumPostVO.getPostId();
	}

	// 刪除文章 操作 (api)
	@PostMapping("delete")
	public String delete(@RequestParam("postId") String postId,
						RedirectAttributes redirectAttributes,
						ModelMap model) {
		forumSvc.deletePost(Integer.valueOf(postId));

		model.addAttribute("forumPostListData", forumSvc.getAll());
		redirectAttributes.addFlashAttribute("successResult", "刪除成功");

		return "redirect:/forum/"; // 導回文章列表
	}

	
	/* ========== 回覆相關 ========== */
	// 新增留言 操作 (api)
	@PostMapping("addReply")
	public ResponseEntity<?> addReply(
			@RequestParam("postId") String postIdStr,
			@RequestParam("replyContent") String replyContent,
			HttpSession session) {

		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO == null) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}

		Integer postId = Integer.valueOf(postIdStr);

		// 回覆VO 設定文章編號
		ForumReplyVO forumReplyVO = new ForumReplyVO();
		forumReplyVO.setForumPostVO(new ForumPostVO());
		forumReplyVO.getForumPostVO().setPostId(postId);

		forumReplyVO.setMemVO(memVO);
		forumReplyVO.setReplyContent(replyContent);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		forumReplyVO.setReplyTime(now);

		ForumReplyVO savedForumReplyVO = forumSvc.addReply(forumReplyVO);

		if (savedForumReplyVO != null) {
			ForumReplyDTO forumReplyDTO = new ForumReplyDTO(savedForumReplyVO);
			forumReplyDTO.setMemName(savedForumReplyVO.getMemVO().getMemName());
			
			return ResponseEntity.status(HttpStatus.OK).body(forumReplyDTO);
			// 不能直接回傳savedForumReplyVO，不然連密碼都會傳到前端..
			// ( Spring Boot預設用 Jackson 將物件轉為json )
		}

		return ResponseEntity.status(HttpStatus.OK).body("fail");
	}

	// 刪除留言 操作 (api)
	@PostMapping("deleteReply")
	public ResponseEntity<?> deleteReply(
			@RequestParam("replyId") String replyIdStr) {

		Integer replyId = Integer.valueOf(replyIdStr);
		if (forumSvc.deleteReply(replyId)) {
			return ResponseEntity.status(HttpStatus.OK).body("success");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("fail");
		}
	}

	/* ========== 檢舉文章相關 ========== */
	// 送出檢舉文章 操作 (api)   給文章內容listAllPost.html使用
	@PostMapping("sendPostReport")
	public ResponseEntity<?> sendPostReport(
			@RequestParam("reportId") String postIdStr,
			@RequestParam("reportReason") String reportReason,
			HttpSession session) {

		Integer postId = Integer.valueOf(postIdStr);

		MemVO memVO = (MemVO) session.getAttribute("memVO");
		if (memVO == null) {
			return ResponseEntity.status(HttpStatus.OK).body("登入後才能繼續操作");
		}

		ForumReportVO forumReportVO = new ForumReportVO();
		forumReportVO.setForumPostVO(new ForumPostVO());
		forumReportVO.getForumPostVO().setPostId(postId);
		forumReportVO.setReportReason(reportReason);
		
		forumReportVO.setMemVO(memVO);
		forumReportVO.setReportStatus(1);

		// 儲存現在的日期
		Date sqlDate = new Date(System.currentTimeMillis());
		forumReportVO.setReportTime(sqlDate);

		// 驗證送出內容VO資料
		Set<ConstraintViolation<ForumReportVO>> violations = 
				validator.validate(forumReportVO);
		if (!violations.isEmpty()) {

			String errorMessage = violations.stream().map(violation -> {
				return violation.getMessage();
			}).collect(Collectors.joining("\n"));

			return ResponseEntity.status(HttpStatus.OK).
					body(errorMessage.toString());
		}

		boolean isSaved = forumSvc.addPostReport(forumReportVO);

		return ResponseEntity.status(HttpStatus.OK).
				body(isSaved ? "success" : "fail");
	}
	
	// 後台 檢舉文章列表 操作 (api)
	@RequestMapping("reports")
	public String forumReportList(ModelMap model) {
		
		model.addAttribute("forumPostReportListData", forumSvc.getPendingReviewReports());

		return "back-end/forum/allPostReport";
	}
	
	// 後台 審核檢舉
	@PostMapping("review")
	public String reviewReportPost(ModelMap model,
			@RequestParam("reportId") String reportIdStr) {
		
		Integer reportId = Integer.valueOf(reportIdStr);
		
		model.addAttribute("reportVO", forumSvc.getOneReport(reportId));

		return "back-end/forum/reviewReport";
	}
	
	// 後台 審核檢舉 送出
	@PostMapping("review/submit")
	public String delete(ModelMap model,
			@RequestParam("reportId") String reportIdStr,
			@RequestParam("result") String result,
			RedirectAttributes redirectAttributes) {
		
		Integer reportId = Integer.valueOf(reportIdStr);
		ForumReportVO forumReportVO = forumSvc.getOneReport(reportId);
		if ( result.equals("approve") ) {
			forumReportVO.setReportStatus(ReportStatus.ACCEPT.toInt());  // 通過
			forumReportVO.getForumPostVO().setPostStatus(PostStatus.HIDE.toInt()); // 文章需要被隱藏
		}
		else if ( result.equals("reject") ) {
			forumReportVO.setReportStatus(ReportStatus.REJECT.toInt());  // 未通過
		}
		
		forumSvc.updatePostReport(forumReportVO);
		
		redirectAttributes.addFlashAttribute("forumPostReportListData", 
										      forumSvc.getPendingReviewReports());
		return "redirect:/forum/reports";
	}

	
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ForumPostVO postVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(postVO, "forumPostVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
}

