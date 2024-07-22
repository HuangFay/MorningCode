package com.morning.order.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.morning.order.model.OrderService;
import com.morning.order.model.OrderVO;
import com.morning.ordd.model.OrddService;
import com.morning.ordd.model.OrddVO;
import com.morning.cart.model.CartService;
import com.morning.cart.model.CartVO;
import com.morning.meals.model.MealsVO;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderSvc;

    @Autowired
    MemService memSvc;

    @Autowired
    CartService cartSvc;
    
    @Autowired
    OrddService orddSvc;

    @GetMapping("addOrder")
    public String addOrder(ModelMap model) {
        OrderVO orderVO = new OrderVO();
        model.addAttribute("orderVO", orderVO);
        return "back-end/order/addOrder";
    }

    @PostMapping("insert")
    public String insert(@Valid OrderVO orderVO, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "back-end/order/addOrder";
        }

        orderSvc.addOrder(orderVO);

        List<OrderVO> list = orderSvc.getAll();
        model.addAttribute("orderListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/order/listAllOrder";
    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("ordId") String ordId, ModelMap model) {
        OrderVO orderVO = orderSvc.getOneOrder(Integer.valueOf(ordId));
        model.addAttribute("orderVO", orderVO);
        return "back-end/order/update_order_input";
      
    }

    @PostMapping("update")
    public String update(@Valid OrderVO orderVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                System.out.println(e.toString());
            }
            return "back-end/order/update_order_input";
        }

        orderSvc.updateOrder(orderVO);

        model.addAttribute("success", "- (修改成功)");
        orderVO = orderSvc.getOneOrder(orderVO.getOrdId());
        model.addAttribute("orderVO", orderVO);
        return "back-end/order/listOneOrder";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("ordId") String ordId, ModelMap model) {
        orderSvc.deleteOrder(Integer.valueOf(ordId));

        List<OrderVO> list = orderSvc.getAll();
        model.addAttribute("orderListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/order/listAllOrder";
    }

    @ModelAttribute("memListData")
    protected List<MemVO> referenceListData() {
        return memSvc.getAll();
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

    public BindingResult removeFieldError(OrderVO orderVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(orderVO, "orderVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    @PostMapping("listOrders_ByCompositeQuery")
    public String listAllOrder(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        map.forEach((key, value) -> System.out.println(key + " : " + Arrays.toString(value))); // 打印调试信息
        List<OrderVO> list = orderSvc.getAll(map);
        model.addAttribute("orderListData", list);
        return "back-end/order/listAllOrder";
    }


   
    @GetMapping("/order_status")
    public String showOrderStatusPage() {
        return "back-end/order/order_status";
    }

    
    @PostMapping("/updateOrderStatus")
    @ResponseBody
    public String updateOrderStatus(@RequestParam("ordId") Integer ordId, @RequestParam("ordStatus") Byte ordStatus) {
        OrderVO order = orderSvc.getOneOrder(ordId);
        if (order != null) {
            order.setOrdStatus(ordStatus);
            orderSvc.updateOrder(order);
            return "success";
        }
        return "error";
    }

    @PostMapping("/deleteOrder")
    @ResponseBody
    public String deleteOrder(@RequestParam("ordId") Integer ordId) {
        orderSvc.deleteOrder(ordId);
        return "success";
    }

    @PostMapping("/addOrder")
    @ResponseBody
    public String addOrder(@RequestParam("memName") String memName,
                           @RequestParam("memPhone") String memPhone,
                           @RequestParam("reserveTime") String reserveTime,
                           @RequestParam("mealsId[]") List<Integer> mealsIds,
                           @RequestParam("quantity[]") List<Integer> quantities) {
        // 构建OrderVO对象
        OrderVO orderVO = new OrderVO();
        MemVO memVO = new MemVO();
        memVO.setMemName(memName);
        memVO.setMemPhone(memPhone);
        orderVO.setMemVO(memVO);
        orderVO.setOrdReserveTime(Timestamp.valueOf(reserveTime));
        orderVO.setOrdStatus((byte) 0);

        // 构建订单详情
        List<OrddVO> orderDetails = new ArrayList<>();
        for (int i = 0; i < mealsIds.size(); i++) {
            OrddVO orddVO = new OrddVO();
            MealsVO mealsVO = new MealsVO();
            mealsVO.setMealsId(mealsIds.get(i));
            orddVO.setMealsVO(mealsVO);
            orddVO.setOrddMealsQuantity(quantities.get(i));
            // 你可以根据实际需要设置其他字段
            orderDetails.add(orddVO);
        }
        orderVO.setOrderDetails(orderDetails);

        orderSvc.addOrder(orderVO);
        return "success";
    }

    // 查看歷史訂單
    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        MemVO member = (MemVO) session.getAttribute("memVO");
        if (member == null) {
            return "redirect:/login";  // 重定向到登錄頁面
        }

        List<OrderVO> orderHistory = orderSvc.getOrdersByMemNo(member.getMemNo());
        model.addAttribute("orderHistory", orderHistory);
        return "front-end/order/orderHistory";  // 返回前台歷史訂單頁面
    }

    // 訂單詳情
    @GetMapping("/detail/{ordId}")
    public String getOrderDetail(@PathVariable Integer ordId, Model model) {
        OrderVO order = orderSvc.getOrderDetail(ordId);
        model.addAttribute("order", order);
        return "front-end/order/order_detail";
    }

    // 再買一次
    @PostMapping("/reorder/{ordId}")
    @ResponseBody
    public ResponseEntity<String> reorder(@PathVariable Integer ordId) {
        try {
            // 获取订单详情
            OrderVO order = orderSvc.getOrderDetail(ordId);
            
            // 遍历订单详情中的餐点，并添加到购物车
            for (OrddVO detail : order.getOrderDetails()) {
                CartVO cartItem = new CartVO();
                cartItem.setMealsId(detail.getMealsVO().getMealsId());
                cartItem.setQuantity(detail.getOrddMealsQuantity());
                // 假设你有获取当前用户 memNo 的方法
                Integer memNo = order.getMemVO().getMemNo();
                cartItem.setMemNo(memNo);
                cartSvc.addCartItem(memNo, detail.getMealsVO().getMealsId());
            }
            
            return ResponseEntity.ok("success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }
    
}
