package com.morning.mem.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name ="member")
public class MemVO implements Serializable{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="mem_no")
	private Integer memNo;
	
	@NotEmpty(message="會員Email: 請勿空白")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "會員email:必需@gmail.com結尾")
	@Column(name ="mem_email" ,unique = true)
	private String memEmail;
	
	@NotEmpty(message="會員姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
	@Column(name ="mem_name")
	private String memName;
	
	@NotEmpty(message="會員密碼: 請勿空白")
//	@Pattern(regexp = "^[(a-zA-Z0-9_)]{2,15}$", message = "密碼: 只能是英文字母、數字和_ , 且長度必需在2到15之間")
	@Column(name ="mem_password")
	private String memPassword;
	
	@NotEmpty(message="會員地址: 請勿空白")
	@Column(name ="mem_address")
	private String memAddress;
	
	@NotEmpty(message="會員電話: 請勿空白")
	@Pattern(regexp = "\\d{10}", message = "員工電話:長度必需在10")
	@Column(name ="mem_phone")
	private String memPhone;
	
	@NotEmpty(message="會員身分證號碼: 請勿空白")
	@Pattern(regexp = "^[A-Z]\\d{9}$", message = "首位英文,後9位為數字")
	@Column(name ="mem_uid")
	private String memUid;
	
	@NotEmpty(message="會員性別: 請勿空白")
	@Pattern(regexp = "^[MmFf]{1}$", message = "性別只能是'M'或'F'")
	@Column(name ="mem_sex")
	private String memSex;
	
	@NotNull(message="會員生日: 請勿空白")
	@Column(name ="mem_dob")
	private Date memDob;
	
	
	@Column(name ="mem_update")
	private Timestamp memUpdate;
	
	
	
	@Column(name ="mem_verified")
	private Byte memVerified ;
	
	
	@Column(name ="mem_photo")
//	@NotEmpty(message="照片: 請上傳照片") --> 由MemController.java 第60行處理錯誤信息
	private byte[] upFiles; //修改 +get.set
	
	
	
	
	
	
	
	 public MemVO() {
	        this.memUpdate = new Timestamp(System.currentTimeMillis());
	    }
	
	
	public Integer getMemNo() {
		return memNo;
	}
	public void setMemNo(Integer memNo) {
		this.memNo = memNo;
	}
	public String getMemEmail() {
		return memEmail;
	}
	public void setMemEmail(String memEmail) {
		this.memEmail = memEmail;
	}
	public String getMemName() {
		return memName;
	}
	public void setMemName(String memName) {
		this.memName = memName;
	}
	public String getMemPassword() {
		return memPassword;
	}
	public void setMemPassword(String memPassword) {
		this.memPassword = memPassword;
	}
	public String getMemAddress() {
		return memAddress;
	}
	public void setMemAddress(String memAddress) {
		this.memAddress = memAddress;
	}
	public String getMemPhone() {
		return memPhone;
	}
	public void setMemPhone(String memPhone) {
		this.memPhone = memPhone;
	}
	public String getMemUid() {
		return memUid;
	}
	public void setMemUid(String memUid) {
		this.memUid = memUid;
	}
	public String getMemSex() {
		return memSex;
	}
	public void setMemSex(String memSex) {
		this.memSex = memSex;
	}
	public Date getMemDob() {
		return memDob;
	}
	public void setMemDob(Date memDob) {
		this.memDob = memDob;
	}
	public Timestamp getMemUpdate() {
		return memUpdate;
	}
	public void setMemUpdate(Timestamp memUpdate) {
		this.memUpdate = memUpdate;
	}
	public byte[] getUpFiles() {
		return upFiles;
	}
	public void setUpFiles(byte[] upFiles) {
		this.upFiles = upFiles;
	}


	public Byte getMemVerified() {
		return memVerified;
	}


	public void setMemVerified(Byte memVerified) {
		this.memVerified = memVerified;
	}
	
	
	
	
	
}
