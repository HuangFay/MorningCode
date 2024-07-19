package com.morning.order.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
        List<OrderVO> list = orderSvc.getAll(map);
        model.addAttribute("orderListData", list);
        return "back-end/order/listAllOrder";
    }

    @GetMapping("/order_success")
    public String showOrderSuccessPage(@RequestParam("ordId") Integer ordId, Model model) {
        OrderVO order = orderSvc.getOneOrder(ordId);
        if (order == null) {
            return "redirect:/shop/select_page";
        }
        model.addAttribute("order", order);
        return "back-end/shop/order_success";
    }

    @GetMapping("/order_status")
    public String showOrderStatusPage() {
        return "back-end/order/order_status";
    }

    @GetMapping("/all_orders")
    public String showAllOrdersPage(Model model) {
        List<OrderVO> orders = orderSvc.getAll();
        model.addAttribute("orders", orders);
        return "back-end/order/all_orders";
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
    
    //查看歷史訂單
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
    
    //再買一次
    @PostMapping("/reorder/{ordId}")
    @ResponseBody
    public String reorder(@PathVariable("ordId") Integer ordId, HttpSession session) {
        MemVO member = (MemVO) session.getAttribute("memVO");
        if (member == null) {
            return "未登入";  // 返回錯誤信息
        }

        OrderVO order = orderSvc.getOneOrder(ordId);
        if (order == null) {
            return "訂單不存在";
        }

        List<OrddVO> orderDetails = order.getOrderDetails();
        for (OrddVO item : orderDetails) {
            CartVO cartVO = new CartVO();
            cartVO.setMemNo(member.getMemNo());
            cartVO.setMealsId(item.getMealsVO().getMealsId());
            cartVO.setQuantity(item.getOrddMealsQuantity());
            cartSvc.addCartItem(cartVO);
        }

        return "success";
    }
    
    //訂單詳情
    @GetMapping("/detail/{ordId}")
    public String getOrderDetail(@PathVariable("ordId") Integer ordId, Model model) {
        OrderVO order = orderSvc.getOneOrder(ordId);
        model.addAttribute("order", order);
        return "order_detail";
    }
   
    
}
