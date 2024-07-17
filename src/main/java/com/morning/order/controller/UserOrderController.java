//package com.morning.order.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.morning.ordd.model.OrddService;
//import com.morning.ordd.model.OrddVO;
//import com.morning.order.model.OrderService;
//import com.morning.order.model.OrderVO;
//import com.morning.mem.model.MemVO;
//
//@Controller
//@RequestMapping("/user")
//public class UserOrderController {
//
//    @Autowired
//    private OrderService orderService;
//
//    @Autowired
//    private OrddService orddService;
//
//    @GetMapping("/userorder")
//    public String listAllOrders(Model model) {
//        // 獲取當前使用者資訊
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        MemVO currentUser = (MemVO) authentication.getPrincipal();
//
//        // 根據使用者ID獲取訂單資訊
//        List<OrderVO> orderList = orderService.getOrdersByMemNo(currentUser.getMemNo());
//        for (OrderVO order : orderList) {
//            List<OrddVO> orderDetails = orddService.findByOrdId(order.getOrdId());
//            order.setOrderDetails(orderDetails);
//        }
//        model.addAttribute("orders", orderList);
//        return "user/listAllOrder";  
//    }
//
//    @GetMapping("/order/detail/{ordId}")
//    public String getOrderDetail(@PathVariable("ordId") Integer ordId, Model model) {
//        OrderVO order = orderService.getOneOrder(ordId);
//        if (order != null) {
//            List<OrddVO> orderDetails = orddService.findByOrdId(ordId);
//            order.setOrderDetails(orderDetails);
//            model.addAttribute("order", order);
//            model.addAttribute("orderDetails", orderDetails);
//        }
//        return "back-end/user/order_detail";  
//    }
//
//    @GetMapping("/order_detail")
//    public String orderDetailPage(@RequestParam("ordId") Integer ordId, Model model) {
//        OrderVO order = orderService.getOneOrder(ordId);
//        if (order != null) {
//            List<OrddVO> orderDetails = orddService.findByOrdId(ordId);
//            order.setOrderDetails(orderDetails);
//            model.addAttribute("order", order);
//            model.addAttribute("orderDetails", orderDetails);
//        }
//        return "back-end/user/order_detail"; 
//    }
//}
