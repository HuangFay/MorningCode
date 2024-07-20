package com.morning.cart.model;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.morning.meals.model.MealsService;

@Service("cartService")
public class CartService {

    @Autowired
    private CartRepository repository;

    @Autowired
    private MealsService mealsService; // 引入 MealsService 以獲取餐點價格

    public void addCartItem(Integer memNo, Integer mealsId) {
        List<CartVO> existingCartItems = repository.findByMemNo(memNo);
        boolean itemExists = false;

        for (CartVO cart : existingCartItems) {
            if (cart.getMealsId().equals(mealsId)) {
                cart.setQuantity(cart.getQuantity() + 1);
                repository.save(cart);
                itemExists = true;
                break;  // 找到匹配項目後跳出循環
            }
        }

        if (!itemExists) {
            CartVO newCart = new CartVO();
            newCart.setMealsId(mealsId);
            newCart.setMemNo(memNo);
            newCart.setQuantity(1);
            repository.save(newCart);
        }
    }


    public void updateCartItem(CartVO cartVO) {
        repository.save(cartVO);
    }

    public void deleteCartItem(Integer cartItemId) {
        if (repository.existsById(cartItemId)) {
            repository.deleteById(cartItemId);
        }
    }

    public CartVO getOneCartItem(Integer cartItemId) {
        Optional<CartVO> optional = repository.findById(cartItemId);
        return optional.orElse(null);
    }

    public List<CartVO> getAllCartItems() {
        return repository.findAll();
    }

    public List<CartVO> getCartItemsByMem(Integer memNo) {
        return repository.findByMemNo(memNo);
    }

    public List<CartVO> getAll(Map<String, String[]> map) {
        // 根據需要實現複合查詢
        return null;
    }

    public void clearCart() {
        repository.deleteAll();
    }

    // 新增 clearCartByMemNo 方法
    public void clearCartByMemNo(Integer memNo) {
        List<CartVO> cartItems = repository.findByMemNo(memNo);
        repository.deleteAll(cartItems);
    }

    // 新增 calculateTotalAmount 方法
    public int calculateTotalAmount(Integer memNo) {
        List<CartVO> cartItems = getCartItemsByMem(memNo);
        int totalAmount = 0;
        for (CartVO cartItem : cartItems) {
            int price = mealsService.getOneMeals(cartItem.getMealsId()).getMealsPrice();
            totalAmount += price * cartItem.getQuantity();
        }
        return totalAmount;
    }
}
