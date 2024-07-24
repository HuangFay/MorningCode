package com.morning.cart.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.morning.cart.model.CartService;
import com.morning.cart.model.CartVO;
import com.morning.mem.model.MemVO;
import com.morning.order.model.OrderService;

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
    public String checkout(@RequestParam String paymentMethod, @RequestParam String selectedItems) {
        String[] selectedItemIds = selectedItems.split(",");
        for (String itemId : selectedItemIds) {
            cartService.deleteCartItem(Integer.valueOf(itemId));
        }

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
    public ResponseEntity<String> addmeals(HttpSession session) {
        // 获取用户信息
        MemVO memvo = (MemVO) session.getAttribute("memVO");

        // 检查用户是否登录
        if (memvo == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("请先登录"); // 返回403状态码和错误消息
        }

        // 获取餐点ID
        Integer mealsId = (Integer) session.getAttribute("mealsId");

        // 处理添加到购物车的逻辑
        cartService.addCartItem(memvo.getMemNo(), mealsId);

        return ResponseEntity.ok("添加成功"); // 返回成功消息
    }

    @GetMapping("/getTotalCount")
    @ResponseBody
    public Integer getTotalCount(HttpSession session) {
        MemVO memvo =(MemVO)session.getAttribute("memVO");
        return cartService.calculateTotalAmount(memvo.getMemNo());
    }
    
    
    


}
