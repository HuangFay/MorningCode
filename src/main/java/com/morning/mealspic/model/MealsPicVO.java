package com.morning.mealspic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.morning.meals.model.MealsVO;

@Entity
@Table(name = "meal_picture")
public class MealsPicVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer mealPicId;
	private byte[] mealPic;
	private MealsVO mealsVO;
	
	public MealsPicVO() {
		
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "meal_pic_id", updatable = false ,insertable = false)
	public Integer getMealPicId() {
		return mealPicId;
	}

	public void setMealPicId(Integer mealPicId) {
		this.mealPicId = mealPicId;
	}

	@Column(name="meal_pic")
	public byte[] getMealPic() {
		return mealPic;
	}

	public void setMealPic(byte[] mealPic) {
		this.mealPic = mealPic;
	}


	@ManyToOne
	@JoinColumn(name="meals_id", referencedColumnName = "meals_id")
	public MealsVO getMealsVO() {
		return mealsVO;
	}

	public void setMealsVO(MealsVO mealsVO) {
		this.mealsVO = mealsVO;
	}
	
	

}
