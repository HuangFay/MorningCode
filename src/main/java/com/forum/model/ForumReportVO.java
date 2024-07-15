package com.forum.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "post_reports")
public class ForumReportVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Integer reportId;

    @NotNull
    @Column(name = "mem_no")
    private Integer memNo;
    
    @Column(name = "emp_id")
    private Integer empId;

    @NotNull
    @Column(name = "post_id")
    private Integer postId;

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

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
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

    @Override
    public String toString() {
        return "ReportVO{" +
                "reportId=" + reportId +
                ", empId=" + empId +
                ", memNo=" + memNo +
                ", postId=" + postId +
                ", reportReason='" + reportReason + '\'' +
                ", reportTime=" + reportTime +
                ", reportStatus=" + reportStatus +
                '}';
    }
}
