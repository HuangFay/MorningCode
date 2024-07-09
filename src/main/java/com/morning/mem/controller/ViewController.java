package com.morning.mem.controller;

import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.ModelAttribute;

import com.morning.mem.model.MemVO;


public abstract class ViewController {

    @ModelAttribute("memVO")
    public MemVO getMemVO(HttpSession session) {
//    	如果 session 中沒有 memVO,則返回 null
        return (MemVO) session.getAttribute("memVO");
    }
    
   
}



//@Controller
////（這裡是 memVO）會被存儲在會話中（HttpSession）,當請求結束時memVO 會自動保存在會話中,之後的請求可以直接訪問它
//@SessionAttributes("memVO")
//public class ViewController {
//
//  @ModelAttribute("memVO")
//  public MemVO getMemVO(HttpSession session) {
////  	如果 session 中沒有 memVO,則返回 null
//      return (MemVO) session.getAttribute("memVO");
//  }
//  
//  @GetMapping("/index2")
//  public String showIndex2(Model model) {
//      return "index2";
//  }
//}