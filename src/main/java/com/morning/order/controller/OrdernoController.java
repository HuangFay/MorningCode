package com.morning.order.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.morning.order.model.OrderService;
import com.morning.order.model.OrderVO;

@Controller
@Validated
@RequestMapping("/order")
public class OrdernoController {

    @Autowired
    OrderService orderSvc;

    @Autowired
    MemService memSvc;

    @PostMapping("getOne_For_Display")
    public String getOne_For_Display(
            @NotEmpty(message="訂單編號: 請勿空白")
            @Digits(integer = 4, fraction = 0, message = "訂單編號: 請填數字-請勿超過{integer}位數")
            @Min(value = 1, message = "訂單編號: 不能小於{value}")
            @Max(value = 1000, message = "訂單編號: 不能超過{value}")
            @RequestParam("ordId") String ordId,
            ModelMap model) {

        OrderVO orderVO = orderSvc.getOneOrder(Integer.valueOf(ordId));

        List<OrderVO> list = orderSvc.getAll();
        model.addAttribute("orderListData", list);  // for select_page.html 第97 109行用
        model.addAttribute("memVO", new MemVO());  // for select_page.html 第133行用
        List<MemVO> list2 = memSvc.getAll();
        model.addAttribute("memListData", list2);  // for select_page.html 第135行用

        if (orderVO == null) {
            model.addAttribute("errorMessage", "查無資料");
            return "back-end/order/select_page";
        }

        model.addAttribute("orderVO", orderVO);
        model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->

        return "back-end/order/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneOrder.html內的th:fragment="listOneOrder-div
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> errorMessages = new ArrayList<>();
        for (ConstraintViolation<?> violation : violations) {
            errorMessages.add(violation.getMessage());
        }

        List<OrderVO> list = orderSvc.getAll();
        model.addAttribute("orderListData", list);  // for select_page.html 第97 109行用
        model.addAttribute("memVO", new MemVO());  // for select_page.html 第133行用
        List<MemVO> list2 = memSvc.getAll();
        model.addAttribute("memListData", list2);  // for select_page.html 第135行用
        model.addAttribute("errorMessages", errorMessages);

        return new ModelAndView("back-end/order/select_page");
    }
}
