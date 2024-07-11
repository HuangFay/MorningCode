package com.morning.mem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email").trim();
        Map<String, String> response = new HashMap<>();

        try {
            if (memService.emailExists(email)) {
                String currentPassword = memService.getCurrentPassword(email);
                emailService.sendCurrentPasswordEmail(email, currentPassword);
                response.put("message", "您的當前密碼已發送到您的Email。");
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
}