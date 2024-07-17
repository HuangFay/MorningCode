package com.morning.collect.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.morning.meals.model.MealsVO;
import com.morning.mem.model.MemVO;

@Entity
@Table(name = "collect")
public class CollectVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id",updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private Integer	id;


	@ManyToOne
	@JoinColumn(name = "mem_no",referencedColumnName="mem_no")
	private MemVO memVO;

	@ManyToOne
	@JoinColumn(name = "meals_id",referencedColumnName="meals_id")
	private MealsVO mealsVO;

	
	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public MemVO getMemVO() {
		return memVO;
	}


	public void setMemVO(MemVO memVO) {
		this.memVO = memVO;
	}


	public MealsVO getMealsVO() {
		return mealsVO;
	}


	public void setMealsVO(MealsVO mealsVO) {
		this.mealsVO = mealsVO;
	}




}
