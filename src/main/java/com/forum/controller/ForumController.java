package com.forum.controller;

import java.io.IOException;
import java.sql.Date;
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

import com.forum.model.ForumPostVO;
import com.forum.model.ForumReplyVO;
import com.forum.model.ForumReportVO;
import com.forum.model.ForumService;
import com.morning.mem.model.MemVO;

@Controller
@RequestMapping("forum")
public class ForumController {
	
	@Autowired
	ForumService forumSvc;

	/***** 畫面設定 *****/

	@GetMapping("")
    public String listAllPost(
    	@RequestParam(value = "postId", required = false) String postId,
    	ModelMap model) {
		if ( postId != null ) {
			ForumPostVO forumPostVO = forumSvc.getOnePost(Integer.valueOf(postId));
			model.addAttribute("forumPostVO", forumPostVO);
			
			ForumReplyVO forumReplyVO = new ForumReplyVO();
			forumReplyVO.setForumPostVO(new ForumPostVO());
			forumReplyVO.getForumPostVO().setPostId(Integer.valueOf(postId));
			
			
			model.addAttribute("forumReplyVO", forumReplyVO);
			
			return "front-end/forum/detail";
		}
		return "front-end/forum/listAllPost";
    }
	
	@GetMapping("addPost")
	public String addPost(ModelMap model) {
		ForumPostVO forumPostVO = new ForumPostVO();
		model.addAttribute("forumPostVO", forumPostVO);
		return "front-end/forum/addPost";
	}
	
	@PostMapping("editPost")
	public String editPost(
			@RequestParam("postId") String postId,
			ModelMap model) {
		
		ForumPostVO forumPostVO = forumSvc.getOnePost(Integer.valueOf(postId));
		model.addAttribute("forumPostVO", forumPostVO);
		
		return "front-end/forum/editPost";
	}
	
	/***** 畫面請求操作 *****/
	
	/* ========== 文章操作相關 ========== */
	
	@PostMapping("insert")
	public String insert(@Valid ForumPostVO forumPostVO, 
			BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) throws IOException {
		
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(forumPostVO, result, "postImg");
		result = removeFieldError(forumPostVO, result, "postTime");
		
		// 儲存現在的日期
		Date sqlDate = new Date(System.currentTimeMillis());
        forumPostVO.setPostTime(sqlDate);
        
        System.out.println(forumPostVO);
		
		// 如果有錯誤，回到上一頁
		if (result.hasErrors()) {
			return "front-end/forum/addPost";
		}
		
		forumSvc.addPost(forumPostVO);
		
		List<ForumPostVO> list = forumSvc.getAll();
		model.addAttribute("forumPostListData", list);
		
		redirectAttributes.addFlashAttribute("success", "- (新增成功)");
		
		// 新增成功後重導至文章列表
		return "redirect:/forum/";
	}
	
	@PostMapping("update")
	public String update(@Valid ForumPostVO forumPostVO, 
			BindingResult result, ModelMap model,
			RedirectAttributes redirectAttributes) throws IOException {
		
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(forumPostVO, result, "postImg");
		result = removeFieldError(forumPostVO, result, "postTime");
		
		// 如果有輸入內容有錯誤，回到上一頁
		if (result.hasErrors()) {
			return "front-end/forum/editPost";
		}
		
		// 更新文章資料
		forumSvc.updatePost(forumPostVO);

		forumPostVO = forumSvc.getOnePost(Integer.valueOf(forumPostVO.getPostId()));
		
		model.addAttribute("forumPostVO", forumPostVO);
		
		redirectAttributes.addFlashAttribute("success", "- (修改成功)");
		return "redirect:/forum?postId=" + forumPostVO.getPostId();
	}

	@PostMapping("delete")
	public String delete(@RequestParam("postId") String postId,
			ModelMap model, RedirectAttributes redirectAttributes) {
		forumSvc.deletePost(Integer.valueOf(postId));
		
		model.addAttribute("forumPostListData", forumSvc.getAll());
		redirectAttributes.addFlashAttribute("success", "- (刪除成功)");
		
		return "redirect:/forum/";  // 導回文章列表
	}
	
	/* ========== 回覆相關 ========== */
	
    @PostMapping("addReply")
    public ResponseEntity<?> addReply(
		 @RequestParam("postId") String postIdStr,
		 @RequestParam("replyContent") String replyContent,
		 HttpSession session) {
    	
	    	MemVO memVO = (MemVO) session.getAttribute("memVO");
	    	if ( memVO == null ) {
	    		return ResponseEntity.status(HttpStatus.OK).body(null);
	    	}
		
		Integer postId = Integer.valueOf(postIdStr);
		
		// 回覆VO 設定文章編號
	    	ForumReplyVO forumReplyVO = new ForumReplyVO();
	    	forumReplyVO.setForumPostVO(new ForumPostVO());
	    	forumReplyVO.getForumPostVO().setPostId(postId);
	    	
	    	forumReplyVO.setMemVO(memVO);
	    	forumReplyVO.setReplyContent(replyContent);
	    	
	    	Date sqlDate = new Date(System.currentTimeMillis());
	    	forumReplyVO.setReplyTime(sqlDate);
	    	
	    	ForumReplyVO savedForumReplyVO = forumSvc.addReply(forumReplyVO);
	    	
	    	if ( savedForumReplyVO != null ) {
	    		return ResponseEntity.status(HttpStatus.OK).body(savedForumReplyVO);
	    	}
			
	    	return ResponseEntity.status(HttpStatus.OK).body("fail");
    }
	
	@PostMapping("deleteReply")
    public ResponseEntity<?> deleteReply(@RequestParam("replyId") String replyIdStr) {
		
		Integer replyId = Integer.valueOf(replyIdStr);
		if ( forumSvc.deleteReply(replyId) ) {
			return ResponseEntity.status(HttpStatus.OK).body("success");
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("fail");
		}
    }
	
	/* ========== 檢舉文章相關 ========== */
	@PostMapping("sendPostReport")
	public ResponseEntity<?> sendPostReport(
			@RequestParam("reportId") String postIdStr,
			@RequestParam("reportReason") String reportReason
			, HttpSession session) {
		
		Integer postId = Integer.valueOf(postIdStr);
		
		MemVO memVO = (MemVO) session.getAttribute("memVO");
	    	if ( memVO == null ) {
	    		return ResponseEntity.status(HttpStatus.OK).body("登入後才能繼續操作");
	    	}
	    	
	    	ForumReportVO forumReportVO = new ForumReportVO();
	    	forumReportVO.setPostId(postId);
	    	forumReportVO.setReportReason(reportReason);
	    	forumReportVO.setMemNo(memVO.getMemNo());
	    	forumReportVO.setReportStatus(1);
	    	
	    	// 儲存現在的日期
		Date sqlDate = new Date(System.currentTimeMillis());
		forumReportVO.setReportTime(sqlDate);
		
        Set<ConstraintViolation<ForumReportVO>> violations = validator.validate(forumReportVO);
		if (!violations.isEmpty()) {

            String errorMessage = violations.stream()
            		.map(violation -> {
            			return violation.getMessage();
            		})
            		.collect(Collectors.joining("\n"));
            
            return ResponseEntity.status(HttpStatus.OK).body(errorMessage.toString());
        }
		
		boolean isSaved = forumSvc.addPostReport(forumReportVO);
	    
		return ResponseEntity.status(HttpStatus.OK).body(isSaved ? "success" : "fail");
    }
	
    private final Validator validator;

    @Autowired
    public ForumController(Validator validator) {
        this.validator = validator;
    }
	
	/***** 資料取得 *****/
    @ModelAttribute("forumPostListData")
	protected List<ForumPostVO> getForumPostListData(Model model) {
		
		return forumSvc.getAll();
	}
	
    
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ForumPostVO postVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(postVO, "forumPostVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
}