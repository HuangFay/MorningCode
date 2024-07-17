package com.morning.shop.controller;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import com.morning.shop.model.ShopVO;
import com.morning.shop.model.ShopService;

@Controller
@Validated
@RequestMapping("/shop")
public class ShopController {

    @Autowired
    ShopService shopSvc;

    @GetMapping("/select_page")
    public String showSelectPage(Model model) {
        return "back-end/shop/select_page";
    }

    @GetMapping("/addItem")
    public String addItem(ModelMap model) {
        ShopVO shopVO = new ShopVO();
        model.addAttribute("shopVO", shopVO);
        return "back-end/shop/addItem";
    }

    @PostMapping("/insert")
    public String insert(@Valid ShopVO shopVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            return "back-end/shop/addItem";
        }

        shopSvc.addItem(shopVO);

        List<ShopVO> list = shopSvc.getAllItems();
        model.addAttribute("shopListData", list);
        model.addAttribute("success", "新增成功");
        return "redirect:/shop/listAllItems";
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ShopVO updateQuantity(@RequestParam("itemId") Integer itemId, @RequestParam("action") String action) {
        ShopVO shopVO = shopSvc.getOneItem(itemId);
        if (shopVO != null) {
            if ("increase".equals(action)) {
                shopVO.setQuantity(shopVO.getQuantity() + 1);
            } else if ("decrease".equals(action)) {
                shopVO.setQuantity(shopVO.getQuantity() - 1);
                if (shopVO.getQuantity() <= 0) {
                    shopSvc.deleteItem(itemId);
                    return null;
                }
            }
            shopVO.setTotalPrice(shopVO.getUnitPrice() * shopVO.getQuantity());
            shopSvc.updateItem(shopVO);
        }
        return shopVO;
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(@RequestParam("itemId") Integer itemId) {
        shopSvc.deleteItem(itemId);
    }

    @GetMapping("/listAllItems")
    public String listAllItems(Model model) {
        List<ShopVO> list = shopSvc.getAllItems();
        model.addAttribute("shopListData", list);
        return "back-end/shop/listAllItems";
    }

    @PostMapping("/getOne_For_Display")
    public String getOne_For_Display(
        @NotEmpty(message="項目編號: 請勿空白")
        @Digits(integer = 4, fraction = 0, message = "項目編號: 請填數字-請勿超過{integer}位數")
        @Min(value = 1, message = "項目編號: 不能小於{value}")
        @Max(value = 9999, message = "項目編號: 不能超過{value}")
        @RequestParam("itemId") String itemId,
        ModelMap model) {

        ShopVO shopVO = shopSvc.getOneItem(Integer.valueOf(itemId));

        if (shopVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/shop/select_page";
        }

        model.addAttribute("shopVO", shopVO);
        model.addAttribute("getOne_For_Display", "true");

        return "back-end/shop/select_page";
    }

    @GetMapping("/cart")
    public String showCartPage(Model model) {
        List<ShopVO> cartItems = shopSvc.getAllItems();
        model.addAttribute("cartItems", cartItems);
        return "back-end/user/cart";
    }

    @PostMapping("/checkout")
    public String handleCheckout(@RequestParam("paymentMethod") String paymentMethod) {
        if ("linePay".equals(paymentMethod)) {
            return "redirect:/shop/line_pay";
        } else {
            return "redirect:/shop/order_success";
        }
    }

    @GetMapping("/line_pay")
    public String showLinePayPage() {
        return "back-end/shop/line_pay";
    }

    @PostMapping("/line_pay_process")
    public String processLinePay(@RequestParam("lineId") String lineId, @RequestParam("linePassword") String linePassword) {
        // 模擬處理 Line Pay 付款邏輯
        boolean paymentSuccess = simulateLinePay(lineId, linePassword);

        if (paymentSuccess) {
            // 假設付款成功，跳轉到訂單成功頁面
            return "redirect:/shop/order_success";
        } else {
            // 假設付款失敗，跳轉到付款失敗頁面
            return "redirect:/shop/payment_failure";
        }
    }

    // 模擬 Line Pay 付款邏輯的方法
    private boolean simulateLinePay(String lineId, String linePassword) {
        // 在這裡添加您的付款邏輯，例如調用 API 或檢查憑證
        // 這裡我們簡單模擬付款失敗的情況
        return false;
    }

    @GetMapping("/payment_failure")
    public String showPaymentFailurePage() {
        return "back-end/shop/payment_failure";
    }
    
    @PostMapping("/addToCart")
    public String addToCart(@RequestParam("mealsId") Integer mealsId, ModelMap model) {
        shopSvc.addItemToCart(mealsId);
        List<ShopVO> cartItems = shopSvc.getAllItems();
        model.addAttribute("cartItems", cartItems);
        return "back-end/user/cart";
    }
    
    

}
