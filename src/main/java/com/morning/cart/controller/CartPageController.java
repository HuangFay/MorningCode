package com.morning.cart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.morning.cart.model.CartService;
import com.morning.cart.model.CartVO;
import com.morning.meals.model.MealsService;
import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;

import javax.validation.Valid;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Controller
@RequestMapping("/cart")
public class CartPageController {

    @Autowired
    MealsService mealsService;

    @Autowired
    MemService memService;

    @Autowired
    CartService cartService;

    @GetMapping("/addItem")
    public String addItem(ModelMap model) {
        CartVO cartVO = new CartVO();
        model.addAttribute("cartVO", cartVO);
        model.addAttribute("mealsList", mealsService.getAll());
        model.addAttribute("membersList", memService.getAll());
        return "back-end/cart/addItem";
    }

//    @PostMapping("/addItem")
//    public String addItem(@Valid @ModelAttribute("cartVO") CartVO cartVO, BindingResult result, ModelMap model) {
//        if (result.hasErrors()) {
//            model.addAttribute("mealsList", mealsService.getAll());
//            model.addAttribute("membersList", memService.getAll());
//            return "back-end/cart/addItem";
//        }
//        cartService.addCartItem(cartVO);
//        return "redirect:/cart/listAllItems";
//    }

    @GetMapping("/getOne_For_Update")
    public String getOne_For_Update(@RequestParam("cartItemId") Integer cartItemId, ModelMap model) {
        CartVO cartVO = cartService.getOneCartItem(cartItemId);
        model.addAttribute("cartVO", cartVO);
        model.addAttribute("mealsList", mealsService.getAll());
        model.addAttribute("membersList", memService.getAll());
        return "back-end/cart/update_cart_item";
    }

    @PostMapping("/update")
    public String update(@Valid @ModelAttribute("cartVO") CartVO cartVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            model.addAttribute("mealsList", mealsService.getAll());
            model.addAttribute("membersList", memService.getAll());
            return "back-end/cart/update_cart_item";
        }
        cartService.updateCartItem(cartVO);
        return "redirect:/cart/listAllItems";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("cartItemId") Integer cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return "redirect:/cart/listAllItems";
    }

    @GetMapping("/listAllItems")
    public String listAllItems(ModelMap model) {
        List<CartVO> list = cartService.getAllCartItems();
        model.addAttribute("cartListData", list);
        return "back-end/cart/listAllItems";
    }

    @ModelAttribute("memListData")
    protected List<MemVO> referenceListData() {
        return memService.getAll();
    }

    @ModelAttribute("memMapData")
    protected Map<Integer, String> referenceMapData() {
        Map<Integer, String> map = new LinkedHashMap<>();
        map.put(1, "周子瑜");
        map.put(2, "峮峮");
        map.put(3, "張鈞甯");
        map.put(4, "王淨");
        map.put(5, "許瑋甯");
        return map;
    }
}
