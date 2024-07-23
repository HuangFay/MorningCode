package com.morning.forum.model;

import java.text.SimpleDateFormat;

public class ForumReplyDTO {
	
	private Integer replyId;
	private String memName;
	private String replyContent;
	private String replyTime;
	
	public ForumReplyDTO(ForumReplyVO replyVO) {
		this.replyId = replyVO.getReplyId();
		this.memName = replyVO.getMemVO().getMemName();
		this.replyContent = replyVO.getReplyContent();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String formattedDate = sdf.format(replyVO.getReplyTime());
		this.replyTime = formattedDate;
	}
	
	public Integer getReplyId() {
		return replyId;
	}

	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}
	
	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	
}
