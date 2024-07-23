package com.morning.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.morning.cart.model.CartService;
import com.morning.cart.model.CartVO;
import com.morning.order.model.OrderService;
import com.morning.mem.model.MemVO;

import java.util.List;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderService orderService;

    @GetMapping
    public String showCartPage(Model model) {
        List<CartVO> cartItems = cartService.getAllCartItems();
        model.addAttribute("cartItems", cartItems);
        return "back-end/cart/listAllItems";
    }

    @PutMapping("/api/update/{cartItemId}")
    @ResponseBody
    public CartVO updateCartItemQuantity(@PathVariable Integer cartItemId, @RequestParam Integer quantity) {
        CartVO cartVO = cartService.getOneCartItem(cartItemId);
        if (cartVO != null) {
            cartVO.setQuantity(quantity);
            cartService.updateCartItem(cartVO);
        }
        return cartVO;
    }

    @DeleteMapping("/api/remove/{cartItemId}")
    @ResponseBody
    public void removeCartItem(@PathVariable Integer cartItemId) {
        cartService.deleteCartItem(cartItemId);
    }

    @PostMapping("/api/clear")
    @ResponseBody
    public void clearCart() {
        cartService.clearCart();
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String paymentMethod) {
        // 清空購物車
        cartService.clearCart();

        // 根據支付方式跳轉不同頁面
        if ("linePay".equals(paymentMethod)) {
            return "redirect:/cart/line_pay";
        } else {
            return "redirect:/cart/order_success";
        }
    }

    @GetMapping("/line_pay")
    public String showLinePayPage() {
        return "back-end/cart/line_pay";
    }

    @PostMapping("/line_pay_process")
    public String processLinePay(@RequestParam String lineId, @RequestParam String linePassword) {
        // 假設付款成功，跳轉到訂單成功頁面
        return "redirect:/cart/order_success";
    }

    @GetMapping("/order_success")
    public String showOrderSuccessPage() {
        return "back-end/cart/order_success";
    }
    
    @PostMapping("/add")
    @ResponseBody
    public void addmeals(HttpSession session) {
    Integer mealsId= (Integer)session.getAttribute("mealsId");
       MemVO memvo =(MemVO)session.getAttribute("memVO");
       System.out.println(memvo);  
       cartService.addCartItem(memvo.getMemNo(), mealsId);
    }  
    

}
