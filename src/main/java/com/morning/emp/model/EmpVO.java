package com.morning.emp.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morning.func.FuncVO;


@Entity
@Table(name ="employee")
public class EmpVO  implements Serializable{
	private static final long serialVersionUID =1L;
	
	/*
	 
	 Table: employee
Columns:
emp_id int AI PK 
emp_name varchar(20) 
emp_account varchar(10) 
emp_password varchar(15) 
emp_phone varchar(20) 
emp_address varchar(100) 
emp_email varchar(100) 
emp_hiredate date 
emp_status tinyint
	 
	 */
	
	
	@Id
	@Column(name = "emp_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】
	private Integer empId;
	
	
	@NotEmpty(message="員工姓名: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,20}$", message = "員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到20之間")
	@Column(name ="emp_name")
	private String empName;
	
	@NotEmpty(message="員工帳號: 請勿空白")
	@Pattern(regexp = "^[(\u4e00-\u9fa5)(a-zA-Z0-9_)]{2,10}$", message = "員工姓名: 只能是中、英文字母、數字和_ , 且長度必需在2到10之間")
	@Column(name ="emp_account")
	private String empAccount;
	
	@NotEmpty(message="員工密碼: 請勿空白")
	@Pattern(regexp = "^[(a-zA-Z0-9_)]{2,15}$", message = "員工密碼:英文字母、數字和_ , 且長度必需在2到15之間")
	@Column(name ="emp_password")
	private String empPassword;
	
	@NotEmpty(message="員工電話: 請勿空白")
	@Pattern(regexp = "\\d{10}", message = "員工電話:長度必需在10")
	@Column(name ="emp_phone")
	private String empPhone;
	
	@NotEmpty(message="員工地址: 請勿空白")
	@Column(name ="emp_address")
	private String empAddress;
	
	@NotEmpty(message="員工email: 請勿空白")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "員工email:必需@gmail.com結尾")
	@Column(name ="emp_email")
	private String empEmail;
	
	@NotNull(message="員工日期: 請勿空白")
	@Column(name ="emp_hiredate")
	private Date empHiredate;
	
	@Column(name ="emp_status")
	private Byte empStatus = 0;
	
//	@Lob
	@Column(name ="emp_photo")
//	@NotEmpty(message="員工照片: 請上傳照片") --> 由EmpController.java 第60行處理錯誤信息
	private byte[] upFiles;
	
	
	
	//==============================================

	@ManyToMany
    @JoinTable(
        name = "permissions",
        joinColumns = @JoinColumn(name = "emp_id"),
        inverseJoinColumns = @JoinColumn(name = "function_id")
    )
	//遞迴放在父類別（訊息多的那邊）。
//	  @JsonManagedReference
	@JsonIgnore
    private Set<FuncVO> functions;

    // Getters and setters
    public Set<FuncVO> getFunctions() {
        return functions;
    }

    public void setFunctions(Set<FuncVO> functions) {
        this.functions = functions;
    }
    
    //
    public Set<FuncVO> getPermissions() {
        return functions; // Assuming functions represent permissions
    }
	//==============================================
	
	
	
	
	
	public byte[] getUpFiles() {
		return upFiles;
	}
	public void setUpFiles(byte[] upFiles) {
		this.upFiles = upFiles;
	}
	public String getEmpAccount() {
		return empAccount;
	}
	public void setEmpAccount(String empAccount) {
		this.empAccount = empAccount;
	}
	
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpPassword() {
		return empPassword;
	}
	public void setEmpPassword(String empPassword) {
		this.empPassword = empPassword;
	}
	public String getEmpPhone() {
		return empPhone;
	}
	public void setEmpPhone(String empPhone) {
		this.empPhone = empPhone;
	}
	public String getEmpAddress() {
		return empAddress;
	}
	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}
	public String getEmpEmail() {
		return empEmail;
	}
	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}
	public Date getEmpHiredate() {
		return empHiredate;
	}
	public void setEmpHiredate(Date empHiredate) {
		this.empHiredate = empHiredate;
	}
	public Byte getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(Byte empStatus) {
		this.empStatus = empStatus;
	}
	
	
}
