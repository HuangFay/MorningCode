package com.morning.cust.controller;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

import com.morning.cust.model.CustService;
import com.morning.cust.model.CustVO;

@Controller
@Validated
@RequestMapping("/cust")
public class CustnoController {
        
        @Autowired
        CustService custSvc;

        @PostMapping("getOne_For_Display")
        public String getOne_For_Display(
                /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
                @NotEmpty(message="客製化選項編號: 請勿空白")
                @Digits(integer = 4, fraction = 0, message = "客製化選項編號: 請填數字-請勿超過{integer}位數")
                @Min(value = 1, message = "客製化選項編號: 不能小於{value}")
                @Max(value = 1000, message = "客製化選項編號: 不能超過{value}")
                @RequestParam("custId") String custId,
                ModelMap model) {
                
                /***************************2.開始查詢資料*********************************************/
                CustVO custVO = custSvc.getOneCust(Integer.valueOf(custId));
                
                List<CustVO> list = custSvc.getAll();
                model.addAttribute("custListData", list);  // for select_page.html 第97 109行用
                
                if (custVO == null) {
                        model.addAttribute("errorMessage", "查無資料");
                        return "back-end/cust/select_page";
                }
                
                /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
                model.addAttribute("custVO", custVO);
                model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
                
                return "back-end/cust/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneCust.html內的th:fragment="listOneCust-div
        }

        @ExceptionHandler(value = { ConstraintViolationException.class })
        //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ModelAndView handleError(HttpServletRequest req, ConstraintViolationException e, Model model) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            StringBuilder strBuilder = new StringBuilder();
            for (ConstraintViolation<?> violation : violations ) {
                  strBuilder.append(violation.getMessage() + "<br>");
            }

            List<CustVO> list = custSvc.getAll();
            model.addAttribute("custListData", list);  // for select_page.html 第97 109行用
            String message = strBuilder.toString();
            return new ModelAndView("back-end/cust/select_page", "errorMessage", "請修正以下錯誤:<br>" + message);
        }
}
