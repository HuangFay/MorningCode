package com.morning.mem.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morning.mem.model.EmailService;
import com.morning.mem.model.MemService;

@RestController
@RequestMapping("/api")
public class PasswordResetController {

    @Autowired
    private MemService memService;

    @Autowired
    private EmailService emailService;
    
    
 // 驗證信地址是動態 ,記得要在application.properties修改
    @Value("${app.url}")
    private String appUrl;
    
    @Autowired
	private RedisTemplate<String, String> redisTemplate;

//    @PostMapping("/forgot-password")
//    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
//        String email = request.get("email").trim();
//        Map<String, String> response = new HashMap<>();
//
//        try {
//            if (memService.emailExists(email)) {
//                String currentPassword = memService.getCurrentPassword(email);
//                emailService.sendCurrentPasswordEmail(email, currentPassword);
//                response.put("message", "您的當前密碼已發送到您的Email。");
//                return ResponseEntity.ok(response);
//            } else {
//                response.put("message", "該Email不存在。");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.put("message", "處理請求時發生錯誤：" + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//        }
//    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email").trim();
        Map<String, String> response = new HashMap<>();

        try {
            if (memService.emailExists(email)) {
                String token = UUID.randomUUID().toString();
                redisTemplate.opsForValue().set("reset:" + token, email, 1, TimeUnit.HOURS);
                String resetLink = appUrl + "/reset-password.html?token=" + token;
                emailService.sendResetPasswordEmail(email, resetLink);
                
                response.put("message", "重置密碼的URL已發送到您的Email。");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "該Email不存在。");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "處理請求時發生錯誤：" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
  
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        Map<String, String> response = new HashMap<>();

        String email = redisTemplate.opsForValue().get("reset:" + token);
        if (email == null) {
            response.put("message", "驗證失敗：無效或過期的令牌");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            memService.updatePassword(email, newPassword);
            redisTemplate.delete("reset:" + token);
            response.put("message", "密碼重置成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "密碼重置失敗: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}