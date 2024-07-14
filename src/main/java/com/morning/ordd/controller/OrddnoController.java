package com.morning.ordd.controller;

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

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.morning.ordd.model.OrddService;
import com.morning.ordd.model.OrddVO;


@Controller
@Validated
@RequestMapping("/ordd")
public class OrddnoController {
        
        @Autowired
        OrddService orddSvc;
        
        @Autowired
        MemService memSvc;

        @PostMapping("getOne_For_Display")
        public String getOne_For_Display(
                /***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
                @NotEmpty(message="訂單編號: 請勿空白")
                @Digits(integer = 4, fraction = 0, message = "訂單編號: 請填數字-請勿超過{integer}位數")
                @Min(value = 1, message = "訂單編號: 不能小於{value}")
                @Max(value = 1000, message = "訂單編號: 不能超過{value}")
                @RequestParam("orddId") String orddId,
                ModelMap model) {
                
                /***************************2.開始查詢資料*********************************************/
//                OrddService orddSvc = new OrddService();
                OrddVO orddVO = orddSvc.getOneOrdd(Integer.valueOf(orddId));
                
                List<OrddVO> list = orddSvc.getAll();
                model.addAttribute("orddListData", list);     // for select_page.html 第97 109行用
                model.addAttribute("memVO", new MemVO());  // for select_page.html 第133行用
                List<MemVO> list2 = memSvc.getAll();
            model.addAttribute("memListData",list2);    // for select_page.html 第135行用
                
                if (orddVO == null) {
                        model.addAttribute("errorMessage", "查無資料");
                        return "back-end/ordd/select_page";
                }
                
                /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
                model.addAttribute("orddVO", orddVO);
                model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
                
                return "back-end/ordd/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneOrdd.html內的th:fragment="listOneOrdd-div
        }

        
        @ExceptionHandler(value = { ConstraintViolationException.class })
        //@ResponseStatus(value = HttpStatus.BAD_REQUEST)
        public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            StringBuilder strBuilder = new StringBuilder();
            for (ConstraintViolation<?> violation : violations ) {
                  strBuilder.append(violation.getMessage() + "<br>");
            }

                List<OrddVO> list = orddSvc.getAll();
                model.addAttribute("orddListData", list);     // for select_page.html 第97 109行用
                model.addAttribute("memVO", new MemVO());  // for select_page.html 第133行用
                List<MemVO> list2 = memSvc.getAll();
            model.addAttribute("memListData",list2);    // for select_page.html 第135行用
                String message = strBuilder.toString();
            return new ModelAndView("back-end/ordd/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
        }
        
}