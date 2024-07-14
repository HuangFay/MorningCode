package com.morning.ordd.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.Column;
import java.sql.Timestamp; // 导入Timestamp类

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
}
