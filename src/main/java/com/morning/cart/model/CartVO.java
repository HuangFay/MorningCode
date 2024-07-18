package com.morning.cart.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.morning.meals.model.MealsVO;
import com.morning.mem.model.MemVO;

@Entity
@Table(name = "cart_items")
public class CartVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Integer cartItemId;

    @NotNull
    @Column(name = "mem_no")
    private Integer memNo;

    @NotNull
    @Column(name = "meals_id")
    private Integer mealsId;

    @NotNull
    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meals_id", insertable = false, updatable = false)
    private MealsVO mealsVO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_no", insertable = false, updatable = false)
    private MemVO memVO;

    public CartVO() {}

    public Integer getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Integer cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Integer getMemNo() {
        return memNo;
    }

    public void setMemNo(Integer memNo) {
        this.memNo = memNo;
    }

    public Integer getMealsId() {
        return mealsId;
    }

    public void setMealsId(Integer mealsId) {
        this.mealsId = mealsId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MealsVO getMealsVO() {
        return mealsVO;
    }

    public void setMealsVO(MealsVO mealsVO) {
        this.mealsVO = mealsVO;
    }

    public MemVO getMemVO() {
        return memVO;
    }

    public void setMemVO(MemVO memVO) {
        this.memVO = memVO;
    }
}
