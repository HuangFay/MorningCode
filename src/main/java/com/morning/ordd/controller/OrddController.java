package com.morning.ordd.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.morning.ordd.model.OrddService;
import com.morning.ordd.model.OrddVO;

@Controller
@RequestMapping("/ordd")
public class OrddController {

    @Autowired
    OrddService orddSvc;

    @GetMapping("addOrdd")
    public String addOrdd(ModelMap model) {
        OrddVO orddVO = new OrddVO();
        model.addAttribute("orddVO", orddVO);
        return "back-end/ordd/addOrdd";
    }

    @PostMapping("insert")
    public String insert(@Valid OrddVO orddVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) { //檢查是否有驗證錯誤
            return "back-end/ordd/addOrdd"; //如果有錯誤，返回表單頁面，讓使用者修正錯誤
        }
        
        /*************************** 2.開始新增資料 *****************************************/
        orddSvc.addOrdd(orddVO);//如果沒有錯誤，進行下一步處理，例如保存訂單

        /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
        List<OrddVO> list = orddSvc.getAll();
        model.addAttribute("orddListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/ordd/listAllOrdd"; //返回成功頁面或其他操作
    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("orddId") String orddId, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始查詢資料 *****************************************/
        OrddVO orddVO = orddSvc.getOneOrdd(Integer.valueOf(orddId));

        /*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
        model.addAttribute("orddVO", orddVO);
        return "back-end/ordd/update_ordd_input"; 
    }
    
    
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//        binder.registerCustomEditor(Timestamp.class, new CustomDateEditor(dateFormat, true));
//    }

    @PostMapping("update")
    public String update(@Valid OrddVO orddVO, BindingResult result, ModelMap model) {

        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        if (result.hasErrors()) {
      
                for (ObjectError e : result.getAllErrors()) {
                        System.out.println(e.toString());
                }
            return "back-end/ordd/update_ordd_input";
        }
 
        /*************************** 2.開始修改資料 *****************************************/
        orddSvc.updateOrdd(orddVO);

        /*************************** 3.修改完成,準備轉交(Send the Success view) **************/
        model.addAttribute("success", "- (修改成功)");
        orddVO = orddSvc.getOneOrdd(orddVO.getOrddId());
        model.addAttribute("orddVO", orddVO);
        return "back-end/ordd/listOneOrdd"; 
    }

    @PostMapping("delete")
    public String delete(@RequestParam("orddId") String orddId, ModelMap model) {
        /*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
        /*************************** 2.開始刪除資料 *****************************************/
        orddSvc.deleteOrdd(Integer.valueOf(orddId));
        
        /*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
        List<OrddVO> list = orddSvc.getAll();
        model.addAttribute("orddListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/ordd/listAllOrdd"; 
    }

    public BindingResult removeFieldError(OrddVO orddVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(orddVO, "orddVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }
 
    @PostMapping("listOrdds_ByCompositeQuery")
    public String listAllOrdd(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<OrddVO> list = orddSvc.getAll(map);
        model.addAttribute("orddListData", list); 
        return "back-end/ordd/listAllOrdd";
    }
    
    @GetMapping("/meals_status")
    public String mealsStatus(Model model) {
        List<OrddVO> orddList = orddSvc.getAll();
        model.addAttribute("orddList", orddList);
        return "back-end/ordd/meals_status";
    }

    @PostMapping("/updateStatus")
    @ResponseBody
    public String updateOrderStatus(@RequestParam("orddId") Integer orddId, @RequestParam("status") Integer status) {
        OrddVO orddVO = orddSvc.getOneOrdd(orddId);
        if (orddVO != null) {
            orddVO.setOrddMealsStatus(status);
            orddSvc.updateOrdd(orddVO);
            return "success";
        }
        return "error";
    }
}
