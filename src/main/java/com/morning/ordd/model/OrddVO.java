package com.morning.ordd.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

import com.morning.meals.model.MealsVO;
import com.morning.order.model.OrderVO;
import com.morning.meal.model.MealVO;

@Entity
@Table(name = "orddetails")
public class OrddVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer orddId;
    private Integer ordId;
    private Integer mealsId;
    private Integer orddMealsQuantity;
    private Integer orddMealsAmount;
    private Integer orddMealsStatus;
    private Integer mealsCommentId;
    private String mealsContent;
    private Integer mealsScore;
    private Integer mealsStatus;
    private Timestamp mealsTime;
    private MealsVO mealsVO; // 餐點資訊關聯
    private OrderVO orderVO; // 訂單資訊關聯
    private List<MealVO> mealCustomizationDetailsVOList; // 客製化明細關聯

    public OrddVO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordd_id")
    public Integer getOrddId() {
        return orddId;
    }

    public void setOrddId(Integer orddId) {
        this.orddId = orddId;
    }

    @NotNull
    @Column(name = "ord_id")
    public Integer getOrdId() {
        return ordId;
    }

    public void setOrdId(Integer ordId) {
        this.ordId = ordId;
    }

    @NotNull
    @Column(name = "meals_id")
    public Integer getMealsId() {
        return mealsId;
    }

    public void setMealsId(Integer mealsId) {
        this.mealsId = mealsId;
    }

    @NotNull
    @Column(name = "ordd_meals_quantity")
    public Integer getOrddMealsQuantity() {
        return orddMealsQuantity;
    }

    public void setOrddMealsQuantity(Integer orddMealsQuantity) {
        this.orddMealsQuantity = orddMealsQuantity;
    }

    @NotNull
    @Column(name = "ordd_meals_amount")
    public Integer getOrddMealsAmount() {
        return orddMealsAmount;
    }

    public void setOrddMealsAmount(Integer orddMealsAmount) {
        this.orddMealsAmount = orddMealsAmount;
    }

    @NotNull
    @Column(name = "ordd_meals_status")
    public Integer getOrddMealsStatus() {
        return orddMealsStatus;
    }

    public void setOrddMealsStatus(Integer orddMealsStatus) {
        this.orddMealsStatus = orddMealsStatus;
    }

    @NotNull
    @Column(name = "meals_comment_id")
    public Integer getMealsCommentId() {
        return mealsCommentId;
    }

    public void setMealsCommentId(Integer mealsCommentId) {
        this.mealsCommentId = mealsCommentId;
    }

    @Column(name = "meals_content", length = 300)
    public String getMealsContent() {
        return mealsContent;
    }

    public void setMealsContent(String mealsContent) {
        this.mealsContent = mealsContent;
    }

    @NotNull
    @Column(name = "meals_score")
    public Integer getMealsScore() {
        return mealsScore;
    }

    public void setMealsScore(Integer mealsScore) {
        this.mealsScore = mealsScore;
    }

    @NotNull
    @Column(name = "meals_status")
    public Integer getMealsStatus() {
        return mealsStatus;
    }

    public void setMealsStatus(Integer mealsStatus) {
        this.mealsStatus = mealsStatus;
    }

    @Column(name = "meals_time")
    public Timestamp getMealsTime() {
        return mealsTime;
    }

    public void setMealsTime(Timestamp mealsTime) {
        this.mealsTime = mealsTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meals_id", insertable = false, updatable = false)
    public MealsVO getMealsVO() {
        return mealsVO;
    }

    public void setMealsVO(MealsVO mealsVO) {
        this.mealsVO = mealsVO;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ord_id", insertable = false, updatable = false)
    public OrderVO getOrderVO() {
        return orderVO;
    }

    public void setOrderVO(OrderVO orderVO) {
        this.orderVO = orderVO;
    }

    @OneToMany(mappedBy = "orddVO", cascade = CascadeType.ALL)
    public List<MealVO> getMealCustomizationDetailsVOList() {
        return mealCustomizationDetailsVOList;
    }

    public void setMealCustomizationDetailsVOList(List<MealVO> mealCustomizationDetailsVOList) {
        this.mealCustomizationDetailsVOList = mealCustomizationDetailsVOList;
    }
}
