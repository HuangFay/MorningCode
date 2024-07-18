package com.morning.assign.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.morning.emp.model.EmpVO;

/*
 * 註1: classpath必須有javax.persistence-api-x.x.jar 
 * 註2: Annotation可以添加在屬性上，也可以添加在getXxx()方法之上
 */


@Entity  //要加上@Entity才能成為JPA的一個Entity類別
@Table(name = "assign") //代表這個class是對應到資料庫的實體table，目前對應的table是EMP2 
public class AssignVO implements java.io.Serializable {



	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="assign_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer assignId;	
	
	
	@Column(name="assign_date")
	private Date assignDate;
	
	@ManyToOne
	@JoinColumn(name="emp_id")	
	private EmpVO empVO;
	
	@ManyToOne
	@JoinColumn(name="emp_id2")	
	private EmpVO empVO1;



	public Integer getAssignId() {
		return assignId;
	}


	public void setAssignId(Integer assignId) {
		this.assignId = assignId;
	}


	public Date getAssignDate() {
		return assignDate;
	}


	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}
	public EmpVO getEmpVO() {
		return this.empVO;
	}


	public void setEmpVO(EmpVO empVO) {
		this.empVO = empVO;
	}

	public EmpVO getEmpVO1() {
		return empVO1;
	}


	public void setEmpVO1(EmpVO empVO1) {
		this.empVO1 = empVO1;
	}

}
