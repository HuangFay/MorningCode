package com.morning.shop.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

@Entity
@Table(name = "shopping_cart")
public class ShopVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer itemId;
    private Integer mealsId;
    private Integer memNo;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

    public ShopVO() {
        // 必須有一個不傳參數建構子
    }

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Column(name = "meals_id")
    @NotNull(message = "餐點編號不能為空")
    public Integer getMealsId() {
        return mealsId;
    }

    public void setMealsId(Integer mealsId) {
        this.mealsId = mealsId;
    }

    @Column(name = "mem_no")
    @NotNull(message = "會員編號不能為空")
    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    @Column(name = "quantity")
    @NotNull(message = "數量不能為空")
    @Min(value = 1, message = "數量不能小於1")
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Column(name = "unit_price")
    @NotNull(message = "單價不能為空")
    @DecimalMin(value = "0.0", inclusive = false, message = "單價必須大於0")
    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Column(name = "total_price")
    @NotNull(message = "總價不能為空")
    @DecimalMin(value = "0.0", inclusive = false, message = "總價必須大於0")
    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
