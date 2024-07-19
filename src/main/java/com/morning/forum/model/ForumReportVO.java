package com.morning.forum.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.morning.mem.model.MemVO;

@Entity
@Table(name = "post_reports")
public class ForumReportVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;
    
    @ManyToOne
	@JoinColumn(name="mem_no")
    private MemVO memVO;
    
    @Column(name = "emp_id")
    private Integer empId;
    
    @ManyToOne
	@JoinColumn(name="postId")
    ForumPostVO forumPostVO;

    @NotNull
    @NotEmpty(message="請填寫檢舉原因。")
    @Size(max = 200)
    @Column(name = "report_reason")
    private String reportReason;

    @NotNull
    @Column(name = "report_time")
    private Date reportTime;

    @NotNull
    @Column(name = "report_status")
    private Integer reportStatus;
    

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public Integer getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(Integer reportStatus) {
        this.reportStatus = reportStatus;
    }
    
    public ForumPostVO getForumPostVO() {
		return forumPostVO;
	}

	public void setForumPostVO(ForumPostVO forumPostVO) {
		this.forumPostVO = forumPostVO;
	}
    
	public MemVO getMemVO() {
		return memVO;
	}
	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}

    @Override
    public String toString() {
        return "ReportVO{" +
                "reportId=" + reportId +
                ", empId=" + empId +
                ", reportReason='" + reportReason + '\'' +
                ", reportTime=" + reportTime +
                ", reportStatus=" + reportStatus +
                '}';
    }
}
