package com.morning.cust.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.morning.cust.model.CustService;
import com.morning.cust.model.CustVO;

@Controller
@RequestMapping("/cust")
public class CustController {

    @Autowired
    CustService custSvc;

    @GetMapping("addCust")
    public String addCust(ModelMap model) {
        CustVO custVO = new CustVO();
        model.addAttribute("custVO", custVO);
        return "back-end/cust/addCust";
    }

    @PostMapping("insert")
    public String insert(@Valid CustVO custVO, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "back-end/cust/addCust";
        }
        
        custSvc.addCust(custVO);

        List<CustVO> list = custSvc.getAll();
        model.addAttribute("custListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/cust/listAllCust";
    }

    @PostMapping("getOneForDisplay")
    public String getOneForDisplay(@RequestParam("custId") String custId, ModelMap model) {
        CustVO custVO = custSvc.getOneCust(Integer.valueOf(custId));
        model.addAttribute("custVO", custVO);
        model.addAttribute("getOneForDisplay", true);
        return "back-end/cust/selectPage";
    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("custId") String custId, ModelMap model) {
        CustVO custVO = custSvc.getOneCust(Integer.valueOf(custId));
        model.addAttribute("custVO", custVO);
        return "back-end/cust/update_cust_input";
    }

    @PostMapping("update")
    public String update(@Valid CustVO custVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                System.out.println(e.toString());
            }
            return "back-end/cust/update_cust_input";
        }
        custSvc.updateCust(custVO);
        model.addAttribute("success", "- (修改成功)");
        custVO = custSvc.getOneCust(custVO.getCustId());
        model.addAttribute("custVO", custVO);
        return "back-end/cust/listOneCust";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("custId") String custId, ModelMap model) {
        custSvc.deleteCust(Integer.valueOf(custId));
        List<CustVO> list = custSvc.getAll();
        model.addAttribute("custListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/cust/listAllCust";
    }

    public BindingResult removeFieldError(CustVO custVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(custVO, "custVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    @PostMapping("listCusts_ByCompositeQuery")
    public String listAllCust(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<CustVO> list = custSvc.getAll(map);
        model.addAttribute("custListData", list);
        return "back-end/cust/listAllCust";
    }
}
