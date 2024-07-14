package com.forum.model;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("forumService")
public class ForumService {

	@Autowired
	ForumPostRepository postRepository;
	
	@Autowired
	ForumReplyRepository replyRepository;

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
		return postRepository.findAll(Sort.by(Sort.Direction.DESC, "postTime"));
		// 新的文章在最前面
	}
	
	// TODO: 文章過濾前N個字
	

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
	
}
