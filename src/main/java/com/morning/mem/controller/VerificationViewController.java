package com.morning.mem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;

@Controller
public class VerificationViewController {

    @Autowired
    private MemService memSvc;
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private HttpSession session;

    @GetMapping("/verify.html")
    public String showVerificationResult(@RequestParam String token, Model model) {
        // 驗證邏輯
        String email = redisTemplate.opsForValue().get("verify:" + token);
        if (email != null) {
            MemVO memVO = memSvc.getMemberByEmail(email);
            if (memVO != null) {
                memVO.setMemVerified((byte) 1);
                memSvc.updateMem(memVO);
                redisTemplate.delete("verify:" + token);
                
                session.setAttribute("memVO", memVO);
                model.addAttribute("message", "信箱驗證成功！");
                return "front-end/mem/verificationSuccess";
            } else {
                model.addAttribute("message", "驗證失敗：未找到用戶");
                return "verificationFailure";
            }
        } else {
            model.addAttribute("message", "驗證失敗：無效或過期的令牌");
            return "verificationFailure";
        }
    }
}