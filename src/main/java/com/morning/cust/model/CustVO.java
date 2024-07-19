package com.morning.cust.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;

@Entity
@Table(name = "customization_options")
public class CustVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer custId;
    private String custName;
    private Integer custPrice;
    private Byte custStatus;
    private String custNote;

    public CustVO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    public Integer getCustId() {
        return this.custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    @Column(name = "cust_name")
    @NotNull(message = "客製化選項名稱: 請勿空白")
    @Size(max = 50, message = "客製化選項名稱: 長度必需在{max}之內")
    public String getCustName() {
        return this.custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Column(name = "cust_price")
    @NotNull(message = "客製化選項加價: 請勿空白")
    @DecimalMin(value = "0", message = "客製化選項加價: 不能小於{value}")
    @DecimalMax(value = "999999", message = "客製化選項加價: 不能超過{value}")
    public Integer getCustPrice() {
        return this.custPrice;
    }

    public void setCustPrice(Integer custPrice) {
        this.custPrice = custPrice;
    }

    @Column(name = "cust_status")
    @NotNull(message = "客製化選項狀態: 請勿空白")
    public Byte getCustStatus() {
        return this.custStatus;
    }

    public void setCustStatus(Byte custStatus) {
        this.custStatus = custStatus;
    }

    @Column(name = "cust_note")
    @Size(max = 50, message = "客製化選項備註內容: 長度必需在{max}之內")
    public String getCustNote() {
        return this.custNote;
    }

    public void setCustNote(String custNote) {
        this.custNote = custNote;
    }
}
