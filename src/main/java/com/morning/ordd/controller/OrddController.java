package com.morning.ordd.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.morning.ordd.model.OrddService;
import com.morning.ordd.model.OrddVO;
import com.morning.order.model.OrderVO;

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
        if (result.hasErrors()) {
            return "back-end/ordd/addOrdd";
        }

        orddSvc.addOrdd(orddVO);

        List<OrddVO> list = orddSvc.getAll();
        model.addAttribute("orddListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/ordd/listAllOrdd";
    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("orddId") String orddId, ModelMap model) {
        OrddVO orddVO = orddSvc.getOneOrdd(Integer.valueOf(orddId));
        model.addAttribute("orddVO", orddVO);
        return "back-end/ordd/update_ordd_input";
    }

    @PostMapping("update")
    public String update(@Valid OrddVO orddVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                System.out.println(e.toString());
            }
            return "back-end/ordd/update_ordd_input";
        }

        orddSvc.updateOrdd(orddVO);

        model.addAttribute("success", "- (修改成功)");
        orddVO = orddSvc.getOneOrdd(orddVO.getOrddId());
        model.addAttribute("orddVO", orddVO);
        return "back-end/ordd/listOneOrdd";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("orddId") String orddId, ModelMap model) {
        orddSvc.deleteOrdd(Integer.valueOf(orddId));

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
    

    @GetMapping("/latest-ordd")
    public String getLatestOrdd(Model model) {
        OrddVO latestOrdd = orddSvc.getLatestOrdd();
        model.addAttribute("latestOrdd", latestOrdd);
        return "index2";
    }

    @GetMapping("/meals_status")
    public String mealsStatus(Model model) {
        List<OrddVO> orddList = orddSvc.getAll();
        model.addAttribute("orddList", orddList);
        
        //預定時間
        model.addAttribute("ordReserveTimeMap", orddList.stream().collect(Collectors.toMap(
            OrddVO::getOrddId, orddVO -> orddVO.getOrderVO().getOrdReserveTime()
        )));
        return "back-end/ordd/meals_status";
    }

    @PostMapping("/update_meals_status")
    @ResponseBody
    public String updateOrderStatus(@RequestParam("orddId") Integer orddId, @RequestParam("orddMealsStatus") Integer status) {
        OrddVO orddVO = orddSvc.getOneOrdd(orddId);
        if (orddVO != null) {
            orddVO.setOrddMealsStatus(status);
            orddSvc.updateOrdd(orddVO);
            return "success";
        }
        return "error";

    }
}
