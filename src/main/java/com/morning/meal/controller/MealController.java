package com.morning.meal.controller;

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

import com.morning.meal.model.MealService;
import com.morning.meal.model.MealVO;

@Controller
@RequestMapping("/meal")
public class MealController {

    @Autowired
    MealService mealSvc;

    @GetMapping("addMeal")
    public String addMeal(ModelMap model) {
        MealVO mealVO = new MealVO();
        model.addAttribute("mealVO", mealVO);
        return "back-end/meal/addMeal";
    }

    @PostMapping("insert")
    public String insert(@Valid MealVO mealVO, BindingResult result, ModelMap model) {

        if (result.hasErrors()) {
            return "back-end/meal/addMeal";
        }
        
        mealSvc.addMeal(mealVO);

        List<MealVO> list = mealSvc.getAll();
        model.addAttribute("mealListData", list);
        model.addAttribute("success", "- (新增成功)");
        return "redirect:/meal/listAllMeal";
    }

    @PostMapping("getOneForDisplay")
    public String getOneForDisplay(@RequestParam("customId") String customId, ModelMap model) {
        MealVO mealVO = mealSvc.getOneMeal(Integer.valueOf(customId));
        model.addAttribute("mealVO", mealVO);
        model.addAttribute("getOneForDisplay", true);
        return "back-end/meal/selectPage";
    }

    @PostMapping("getOne_For_Update")
    public String getOne_For_Update(@RequestParam("customId") String customId, ModelMap model) {
        MealVO mealVO = mealSvc.getOneMeal(Integer.valueOf(customId));
        model.addAttribute("mealVO", mealVO);
        return "back-end/meal/update_meal_input";
    }

    @PostMapping("update")
    public String update(@Valid MealVO mealVO, BindingResult result, ModelMap model) {
        if (result.hasErrors()) {
            for (ObjectError e : result.getAllErrors()) {
                System.out.println(e.toString());
            }
            return "back-end/meal/update_meal_input";
        }
        mealSvc.updateMeal(mealVO);
        model.addAttribute("success", "- (修改成功)");
        mealVO = mealSvc.getOneMeal(mealVO.getCustomId());
        model.addAttribute("mealVO", mealVO);
        return "back-end/meal/listOneMeal";
    }

    @PostMapping("delete")
    public String delete(@RequestParam("customId") String customId, ModelMap model) {
        mealSvc.deleteMeal(Integer.valueOf(customId));
        List<MealVO> list = mealSvc.getAll();
        model.addAttribute("mealListData", list);
        model.addAttribute("success", "- (刪除成功)");
        return "back-end/meal/listAllMeal";
    }

    public BindingResult removeFieldError(MealVO mealVO, BindingResult result, String removedFieldname) {
        List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
                .filter(fieldname -> !fieldname.getField().equals(removedFieldname))
                .collect(Collectors.toList());
        result = new BeanPropertyBindingResult(mealVO, "mealVO");
        for (FieldError fieldError : errorsListToKeep) {
            result.addError(fieldError);
        }
        return result;
    }

    @PostMapping("listMeals_ByCompositeQuery")
    public String listAllMeal(HttpServletRequest req, Model model) {
        Map<String, String[]> map = req.getParameterMap();
        List<MealVO> list = mealSvc.getAll(map);
        model.addAttribute("mealListData", list);
        return "back-end/meal/listAllMeal";
    }
}
