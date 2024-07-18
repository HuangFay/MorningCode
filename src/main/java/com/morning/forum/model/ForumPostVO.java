package com.morning.forum.model;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.morning.mem.model.MemVO;

@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "forum_posts") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2
public class ForumPostVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer postId;
	
	private MemVO memVO;
	
	private String postTitle;
	private String postContent;
	private Date postTime;
	private Integer postStatus;
	
	private Set<ForumReplyVO> replies;
	
	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}
	
	@ManyToOne
	@JoinColumn(name="mem_no")
	public MemVO getMemVO() {
		return memVO;
	}
	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}
	
	@Column(name = "post_title")
	@NotEmpty(message="文章標題: 請勿空白")
	public String getPostTitle() {
		return postTitle;
	}
	
	public void setPostTitle(String postTitle) {
		this.postTitle = postTitle;
	}
	
	@Column(name = "post_content")
	@NotEmpty(message="文章內容: 請勿空白")
	public String getPostContent() {
		return postContent;
	}
	
	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}
	
	// 文章內容擷斷 (閱讀更多...)
	@Transient
    public String getShortenedContent() {
        if (postContent == null) {
            return null;
        }
        int maxLength = 150;
        if (postContent.length() > maxLength) {
            return postContent.substring(0, maxLength) + "...";
        } else {
            return postContent;
        }
    }
	
	@Column(name = "post_time")
	public Date getPostTime() {
		return postTime;
	}
	
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	
	@Column(name = "post_status")
	public Integer getPostStatus() {
		return postStatus;
	}
	
	public void setPostStatus(Integer postStatus) {
		this.postStatus = postStatus;
	}

	@OneToMany(mappedBy = "forumPostVO", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	public Set<ForumReplyVO> getReplies() {
		return replies;
	}

	public void setReplies(Set<ForumReplyVO> replies) {
		this.replies = replies;
	}
	
	
	@Override
	public String toString() {
	    return "Post{" +
	            "postId=" + postId +
	            ", postTitle='" + postTitle + '\'' +
	            ", postContent='" + postContent + '\'' +
	            ", postTime=" + postTime +
	            ", postStatus=" + postStatus +
	            '}';
	}
}
