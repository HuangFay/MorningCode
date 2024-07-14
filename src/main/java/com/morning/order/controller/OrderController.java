package com.morning.order.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.morning.order.model.OrderService;
import com.morning.order.model.OrderVO;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderSvc;

    @Autowired
    MemService memSvc;

    @GetMapping("addOrder")
    public String addOrder(ModelMap model) {
        OrderVO orderVO = new OrderVO();
        model.addAttribute("orderVO", orderVO);
        return "back-end/order/addOrder";
    }

    @PostMapping("insert")
    public String insert(@Valid OrderVO orderVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) { //檢查是否有驗證錯誤
            return "back-end/order/addOrder"; //如果有錯誤，返回表單頁面，讓使用者修正錯誤
        }
        
        /*************************** 2.開始新增資料 *****************************************/
        orderSvc.addOrder(orderVO);//如果沒有錯誤，進行下一步處理，例如保存訂單

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<OrderVO> list = orderSvc.getAll();
        model.addAttribute("orderListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/order/listAllOrder"; //返回成功頁面或其他操作
    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("ordId") String ordId, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        OrderVO orderVO = orderSvc.getOneOrder(Integer.valueOf(ordId));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("orderVO", orderVO);
        return "back-end/order/update_order_input"; 
    }
    
    
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//        binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(dateFormat, true));
//    }

    @PostMapping("update")
    public String update(@Valid OrderVO orderVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
      
        	for (ObjectError e : result.getAllErrors()) {
        		System.out.println(e.toString());
        	}
            return "back-end/order/update_order_input";
        }
 
        /*************************** 2.開始修改資料 *****************************************/
        orderSvc.updateOrder(orderVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        orderVO = orderSvc.getOneOrder(orderVO.getOrdId());
        model.addAttribute("orderVO", orderVO);
        return "back-end/order/listOneOrder"; 
    }

    @PostMapping("delete")
    public String delete(@RequestParam("ordId") String ordId, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        orderSvc.deleteOrder(Integer.valueOf(ordId));
        
        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<OrderVO> list = orderSvc.getAll();
        model.addAttribute("orderListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/order/listAllOrder"; 
    }

    @ModelAttribute("memListData")
    protected List<MemVO> referenceListData() {
        List<MemVO> list = memSvc.getAll();
        return list;
    }
    
    @ModelAttribute("memMapData") 
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
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
    public String showOrderSuccessPage() {
        return "back-end/order/order_status";
    }
}
