package com.morning.func;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.morning.emp.model.EmpVO;

@Entity
@Table(name = "afunction")
public class FuncVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/*
	 Table: afunction
	Columns:
	function_id int AI PK 
	function_name varchar(10)
	 */
	
	@Id
	@Column(name="function_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue的generator屬性指定要用哪個generator //【strategy的GenerationType, 有四種值: AUTO, IDENTITY, SEQUENCE, TABLE】 
	private Integer functionId;
	
	@Column(name ="function_name")
	private String functionName;
	
	
//=====================================================
 @ManyToMany(mappedBy = "functions")
    private Set<EmpVO> employees;

    // Getters and setters
 //遞迴 子類別(放在引用父類別的那邊)
// @JsonBackReference
 @JsonIgnore
    public Set<EmpVO> getEmployees() {
        return employees;
    }

    public void setEmployees(Set<EmpVO> employees) {
        this.employees = employees;
    }	

//=====================================================

 

	
	
	public Integer getFunctionId() {
		return functionId;
	}
	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}
	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}
	
}
