package com.morning.cust.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.morning.cust.model.CustService;
import com.morning.cust.model.CustVO;

@RestController
@RequestMapping("/cust")
public class CustController {

    @Autowired
    CustService custSvc;

    @GetMapping("/getOptionsByMealId")
    public List<CustVO> getOptionsByMealId(@RequestParam("mealsId") Integer mealsId, HttpSession session) {
        session.setAttribute("mealsId", mealsId);
    	// 假設每個餐點都有固定的一組客製化選項，這裡可以根據具體邏輯調整
        return custSvc.getAllActiveOptions();
    }
}
