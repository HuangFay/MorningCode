package com.morning.mealstypes.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.morning.meals.model.MealsVO;

@Entity
@Table(name="meals_types")
public class MealsTypesVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;

	private Integer mealsTypesId;
	private String mealsTypesName;
	private Set<MealsVO> mealsVO;
	
	public MealsTypesVO() {
		
	}

	@Id
	@Column(name="meals_types_id", updatable = false, insertable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getMealsTypesId() {
		return mealsTypesId;
	}

	public void setMealsTypesId(Integer mealsTypesId) {
		this.mealsTypesId = mealsTypesId;
	}

	@Column(name="meals_types_name")
	@NotEmpty(message="餐點類別名稱不能空白")
	@Pattern(regexp="^[\u4e00-\u9fa5a-zA-Z0-9]{1,10}$", message="餐點類別名稱:只能是中英文數字，且長度必須小於10")
	public String getMealsTypesName() {
		return mealsTypesName;
	}

	public void setMealsTypesName(String mealsTypesName) {
		this.mealsTypesName = mealsTypesName;
	}
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="mealstypesVO")
	@OrderBy("meals_types_id asc")
	public Set<MealsVO> getMeals() {
		return mealsVO;
	}

	public void setMeals(Set<MealsVO> meals) {
		this.mealsVO = mealsVO;
	}

}
