package com.morning.meal.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.morning.ordd.model.OrddVO;
import com.morning.cust.model.CustVO;

@Entity
@Table(name = "meal_customization_details")
public class MealVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer customId;
    private OrddVO orddVO;
    private CustVO custVO;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordd_id", nullable = false)
    @NotNull
    public OrddVO getOrddVO() {
        return orddVO;
    }

    public void setOrddVO(OrddVO orddVO) {
        this.orddVO = orddVO;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cust_id", nullable = false)
    @NotNull
    public CustVO getCustVO() {
        return custVO;
    }

    public void setCustVO(CustVO custVO) {
        this.custVO = custVO;
    }
}
