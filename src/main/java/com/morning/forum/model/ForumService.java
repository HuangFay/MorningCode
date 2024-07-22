package com.morning.forum.model;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("forumService")
public class ForumService {

	@Autowired
	ForumPostRepository postRepository;
	
	@Autowired
	ForumReplyRepository replyRepository;
	
	@Autowired
	ForumReportRepository reportRepository;
	
	/* ========== 文章相關 ========== */

	public void addPost(ForumPostVO forumPostVO) {
		postRepository.save(forumPostVO);
	}

	public void updatePost(ForumPostVO forumPostVO) {
		postRepository.save(forumPostVO);
	}

	public void deletePost(Integer postId) {
		if (postRepository.existsById(postId))
			postRepository.deleteById(postId);
	}

	public ForumPostVO getOnePost(Integer postId) {
		Optional<ForumPostVO> optional = postRepository.findById(postId);

		return optional.orElse(null);
	}

	public List<ForumPostVO> getAll() {
		
		// 取得文章列表 (過濾可顯示的文章, 根據時間排序)
		return postRepository.findByPostStatusOrderByPostTimeDesc(1);
		//return postRepository.findAll(Sort.by(Sort.Direction.DESC, "postTime"));
	}
	

	/* ========== 回覆相關 ========== */
	
	public ForumReplyVO getOneReply(Integer replyId) {
		Optional<ForumReplyVO> optional = replyRepository.findById(replyId);

		return optional.orElse(null);
	}
	
	public ForumReplyVO addReply(ForumReplyVO replyVO) {
		return replyRepository.save(replyVO);
	}
	
	@Transactional
	public boolean deleteReply(Integer replyId) {
		if (replyRepository.existsById(replyId)) {
			replyRepository.deleteByReplyId(replyId);
			
			return !replyRepository.existsById(replyId);
		}
		return false;
	}
	
	/* ========== 檢舉相關 ========== */
	public boolean addPostReport(ForumReportVO forumReportVO) {
		
		ForumReportVO srVO = reportRepository.save(forumReportVO);
		
		return reportRepository.existsById(srVO.getReportId());
	}
	
	public void updatePostReport(ForumReportVO forumReportVO) {
		reportRepository.save(forumReportVO);
	}
	
	public List<ForumReportVO> getPendingReviewReports() {
		return reportRepository.findByReportStatus(0);
	}
	
	public ForumReportVO getOneReport(Integer reportId) {
		Optional<ForumReportVO> optional = reportRepository.findById(reportId);

		return optional.orElse(null);
	}
	 public ForumPostVO getLatestPost() {
	        return postRepository.findTopByOrderByPostTimeDesc();
	    }
	
}









