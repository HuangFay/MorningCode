package com.morning.forum.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.morning.mem.model.MemVO;

@Entity
@Table(name = "forum_reply")
public class ForumReplyVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Integer replyId;
	private String replyContent;
	private Date replyTime;
	
	private MemVO memVO;
	private ForumPostVO forumPostVO;
	

	@Id
	@Column(name = "reply_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getReplyId() {
		return replyId;
	}
	public void setReplyId(Integer replyId) {
		this.replyId = replyId;
	}


	@Column(name = "reply_content")
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}


	@Column(name = "reply_time")
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	
	@ManyToOne
	@JoinColumn(name="mem_no")
	public MemVO getMemVO() {
		return memVO;
	}
	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}

	@ManyToOne
	@JoinColumn(name = "post_id", referencedColumnName = "post_id")
	public ForumPostVO getForumPostVO() {
		return forumPostVO;
	}

	public void setForumPostVO(ForumPostVO forumPostVO) {
		this.forumPostVO = forumPostVO;
	}
	

	@Override
	public String toString() {
		return "ForumReplyVO{" +
                "replyId=" + replyId +
                ", replyContent='" + replyContent + '\'' +
                ", replyTime=" + replyTime +
                ", memName=" + (forumPostVO.getMemVO() != null ? forumPostVO.getMemVO().getMemName() : null) +
                '}';
	}
}
