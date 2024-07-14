package com.morning.meal.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meal_customization_details")
public class MealVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer customId;
    private Integer orddId;
    private Integer custId;

    public MealVO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "custom_id")
    public Integer getCustomId() {
        return this.customId;
    }

    public void setCustomId(Integer customId) {
        this.customId = customId;
    }

    @NotNull(message = "訂單明細編號: 請勿空白")
    @Column(name = "ordd_id")
    public Integer getOrddId() {
        return this.orddId;
    }

    public void setOrddId(Integer orddId) {
        this.orddId = orddId;
    }

    @NotNull(message = "客製化選項編號: 請勿空白")
    @Column(name = "cust_id")
    public Integer getCustId() {
        return this.custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }
}
