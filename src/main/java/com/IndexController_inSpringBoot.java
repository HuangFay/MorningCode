package com;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.morning.cart.model.CartService;
import com.morning.cart.model.CartVO;
import com.morning.cust.model.CustService;
import com.morning.cust.model.CustVO;
import com.morning.emp.model.EmpService;
import com.morning.emp.model.EmpVO;
import com.morning.forum.model.ForumPostVO;
import com.morning.forum.model.ForumService;
import com.morning.leave.model.LeaveService;
import com.morning.leave.model.LeaveVO;
import com.morning.meal.model.MealService;
import com.morning.meal.model.MealVO;
import com.morning.meals.model.MealsService;
import com.morning.meals.model.MealsVO;
import com.morning.mealspic.model.MealsPicService;
import com.morning.mealspic.model.MealsPicVO;
import com.morning.mealstypes.model.MealsTypesService;
import com.morning.mealstypes.model.MealsTypesVO;
import com.morning.mem.model.MemService;
import com.morning.mem.model.MemVO;
import com.morning.news.model.NewsService;
import com.morning.news.model.NewsVO;
import com.morning.ordd.model.OrddService;
import com.morning.ordd.model.OrddVO;
import com.morning.order.model.OrderService;
import com.morning.order.model.OrderVO;
import com.reservation.model.ResService;
import com.reservation.model.ResVO;
import com.reservationcontrol.model.ResCService;
import com.reservationcontrol.model.ResCVO;
import com.restime.model.ResTimeService;
import com.restime.model.ResTimeVO;
import com.tabletype.model.TableTypeService;
import com.tabletype.model.TableTypeVO;



//@PropertySource("classpath:application.properties") // 於https://start.spring.io建立Spring Boot專案時, application.properties文件預設已經放在我們的src/main/resources 目錄中，它會被自動檢測到
@Controller
public class IndexController_inSpringBoot   {
	
	// @Autowired (●自動裝配)(Spring ORM 課程)
	// 目前自動裝配了EmpService --> 供第66使用
	@Autowired
	EmpService empSvc;
	
	@Autowired
	MemService memSvc;
	
	@Autowired
	MealsService mealsSvc;
	
	@Autowired
	MealsPicService mealspicSvc;
	
	@Autowired
	MealsTypesService mealstypesSvc;
	
	@Autowired
	LeaveService leaveSvc;	
	
	@Autowired
	OrderService orderSvc;	
	
	@Autowired
    OrddService orddSvc;
    
    @Autowired
    CustService custSvc;
    
    @Autowired
    MealService mealSvc;
    
    @Autowired
    CartService cartSvc;
    
    @Autowired
    NewsService newsSvc;
    
//訂位autowired
	@Autowired
	ResTimeService resTimeSvc;
	@Autowired
	ResCService resCSvc;
	@Autowired
	TableTypeService tableSvc;
	@Autowired
	ResService resSvc;
	@Autowired
	ForumService forumSvc;

	
    // inject(注入資料) via application.properties
    @Value("${welcome.message}")
    private String message;
    
  //================================================================================
    
    @ModelAttribute
    public void addAttributes(HttpSession session, Model model) {
    	OrddVO latestOrdd = orddSvc.getLatestOrdd();
        model.addAttribute("latestOrdd", latestOrdd);
        NewsVO latestNews = newsSvc.getLatestNews();
        model.addAttribute("latestNews", latestNews);
        ForumPostVO latestPost = forumSvc.getLatestPost();
        model.addAttribute("latestPost", latestPost);
        MemVO memVO = (MemVO) session.getAttribute("memVO");
        if (memVO != null) {
            model.addAttribute("memVO", memVO);
        }
    }
    
  //================================================================================
	
    private List<String> myList = Arrays.asList("Spring Boot Quickstart 官網 : https://start.spring.io", "IDE 開發工具", "直接使用(匯入)官方的 Maven Spring-Boot-demo Project + pom.xml", "直接使用官方現成的 @SpringBootApplication + SpringBootServletInitializer 組態檔", "依賴注入(DI) HikariDataSource (官方建議的連線池)", "Thymeleaf", "Java WebApp (<font color=red>快速完成 Spring Boot Web MVC</font>)");
    @GetMapping("/")
    public String index(Model model) {
    	model.addAttribute("message", message);
        model.addAttribute("myList", myList);
        System.out.println("index");
        return "index2"; //view
    }
    
    
    
//    session返回登入 
    @GetMapping("/index2")
    public String index2( ) {
//    	MemVO memVO = (MemVO) session.getAttribute("memVO");
//        if (memVO != null) {
//            model.addAttribute("memVO", memVO);
//        }
        return "index2";
    }
    
 
    
//    登出 RedirectView 是Spring Framework的類 ,用來實現重新導向的功能
//    tomcat使用這個方法不行
//    @GetMapping("/logout")
//    public RedirectView logout(HttpSession session) {
//        // 清除session資料
//        session.invalidate(); // 將當前session的紀錄刪除
//
//        // 轉
//        return new RedirectView("/index2"); 
//    }
    
    
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // 清除session資料
        session.invalidate(); // 將當前的session紀錄刪除

        // 提示訊息
        redirectAttributes.addFlashAttribute("message", "成功登出");

        // 重新導向/index2
        return "redirect:/index2";
    }
    
    
    
//  個人資料管理
    @GetMapping("/customSettings")
    public String customSettings() {
        return "front-end/mem/customSettings";
    }
//    
//  修改密碼
    @GetMapping("/restPassword")
    public String forgetPassword() {
    	
        return "front-end/mem/restPassword";
    }
   
 
//  
//    //Emp=========== 以下第63~75行是提供給 /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html 要使用的資料 ===================   
    @GetMapping("/emp/select_page")
	public String select_page(Model model) {
		return "back-end/emp/select_page";
	}

    
    
    @GetMapping("/back-end/emp/listAllEmp")
    public String listAllEmp(HttpSession session, Model model) {
        EmpVO empVO = (EmpVO) session.getAttribute("empVO");
        if (empVO != null) {
            model.addAttribute("empVO", empVO);
        }
        return "back-end/emp/listAllEmp";
    }
    
//    @GetMapping("/back-end/emp/addEmp")
//    public String addEmp(HttpSession session,Model model) {
//    	 EmpVO empVO = (EmpVO) session.getAttribute("empVO");
//         if (empVO != null) {
//             model.addAttribute("empVO", empVO);
//         }
//    	
//		return "back-end/emp/addEmp";
//	}
    
//   預先載入資源  避免手動載入資源
    @ModelAttribute("empListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<EmpVO> referenceListData(Model model) {
		
    	List<EmpVO> list = empSvc.getAll();
		return list;
	}
    
    
    //Mem==============================================================================================================
    
    @GetMapping("/mem/select_page")
	public String select_page1(Model model) {
		return "back-end/mem/select_page";
	}
    @GetMapping("/back-end/mem/listAllMem")
  	public String listAllMem(HttpSession session, Model model) {
    	EmpVO empVO = (EmpVO) session.getAttribute("empVO");
        if (empVO != null) {
            model.addAttribute("empVO", empVO);
            model.addAttribute("memVO", new MemVO());
        }
  		return "back-end/mem/listAllMem";
  	}
    
    
//    @GetMapping("/back-end/mem/addMem")
//    public String addMem(HttpSession session, Model model) {
//        EmpVO empVO = (EmpVO) session.getAttribute("empVO");
//        if (empVO != null) {
//            model.addAttribute("empVO", empVO);
//        }
//        model.addAttribute("memVO", new MemVO());  
//        return "back-end/mem/addMem";
//    }
    
    
//  預先載入資源  避免手動載入資源
   @ModelAttribute("memListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<MemVO> referenceListData1(Model model) {
		
   	List<MemVO> list = memSvc.getAll();
		return list;
	}
   //首頁按登入跳到signup==============================================================================================================
   
   @GetMapping("/front-end/mem/signup")
   public String showSignUpPage() {
       
       return "front-end/mem/signup"; 
   }
   //後台首頁==============================================================================================================
   @GetMapping("/index3")
   public String index3(HttpSession session, Model model) {
       EmpVO empVO = (EmpVO) session.getAttribute("empVO");
       if (empVO != null) {
           model.addAttribute("empVO", empVO);
       }
       return "index3";
   }
   
   //員工登入
   @PostMapping("/back-end/emplogin")
   public String emplogin() {
	   return "back-end/emplogin";
   }
   
   //員工登入
   @GetMapping("/back-end/emplogin")
   public String showEmpLoginPage() {
       return "back-end/emplogin";
   }
   
   //員工登出Get版
//     @GetMapping("/emplogout")
//   public RedirectView emplogout(HttpSession session) {
//       // 清除session資料
//       session.invalidate(); // 將當前session的紀錄刪除
//
//       // 轉
//       return new RedirectView("/index3"); 
//   
   //員工登出 Post版
   @PostMapping("/back-end/emplogout")
   public String emplogout(HttpServletRequest request, HttpServletResponse response) {
       // 清除session資料
       HttpSession session = request.getSession(false);
       if (session != null) {
           session.invalidate();
       }
       
       // 清除相關的cookie
       Cookie[] cookies = request.getCookies();
       if (cookies != null) {
           for (Cookie cookie : cookies) {
               if (cookie.getName().equals("JSESSIONID")) {
                   cookie.setMaxAge(0);
                   response.addCookie(cookie);
               }
           }
       }

       // 轉址到首頁
       return "redirect:/index3";
   }
   
   
   //權限不足的錯誤頁面
   @GetMapping("/no-access")
   public String noaccess(Model model){
	   return "no-access";
   }
   
   
   //其他功能轉跳網頁==============================================================================================================
   @GetMapping("/test1")
   public String test1(Model model) {
	   
	   return "test1";
   }

   @GetMapping("/back-end/test2")
   public String test2(Model model) {
	   
	   return "test2";
   }
   
   //==meals============================================================
   @GetMapping("/back-end/meals/listAllMeals")
   public String listAllMeals(HttpSession session, Model model) {
	   EmpVO empVO = (EmpVO) session.getAttribute("empVO");
       if (empVO != null) {
           model.addAttribute("empVO", empVO);
       }
       
	   return "back-end/meals/listAllMeals";
   }
   @ModelAttribute("mealsListData")
   protected List<MealsVO> referenceListData_meals(Model model) {
		
   List<MealsVO> list = mealsSvc.getAll();
		return list;
	}
   //==mealspic========================================================
   @GetMapping("/back-end/mealspic/listAllMealsPic")
 	public String listAllMealsPic(HttpSession session, Model model) {
	   EmpVO empVO = (EmpVO) session.getAttribute("empVO");
       if (empVO != null) {
           model.addAttribute("empVO", empVO);
       }
 		return "back-end/mealspic/listAllMealsPic";
 	}
     
   @ModelAttribute("mealspicListData")
 	protected List<MealsPicVO> referenceListData_mealspic(Model model) {
 		
     	List<MealsPicVO> list = mealspicSvc.getAll();
 		return list;
 	}
   //==mealstypes=======================================================
   @GetMapping("/back-end/mealstypes/listAllMealsTypes")
   public String listAllMealsTypes(HttpSession session, Model model) {
	   EmpVO empVO = (EmpVO) session.getAttribute("empVO");
       if (empVO != null) {
           model.addAttribute("empVO", empVO);
       }
 		return "back-end/mealstypes/listAllMealsTypes";
   }
   
   @ModelAttribute("mealstypesListData")
	protected List<MealsTypesVO> referenceListData_mealstypes(Model model) {
		
    	List<MealsTypesVO> list = mealstypesSvc.getAll();
		return list;
	}
   //==menu=============================================================
   @GetMapping("/front-end/menu/menu")
   public String menu (Model model) {
	   return "/front-end/menu/menu";
   }
   //==listfavorite=====================================================
   @GetMapping("/front-end/menu/listfavorite")
   public String listfavorite (Model model) {
	   return "/front-end/menu/listfavorite";
   }

   @GetMapping("/leave/select_page")
	public String select_page3(Model model) {
		return "back-end/leave/select_page";
	}
   
   @GetMapping("/back-end/leave/listAllLeave")
	public String listAllLeave(HttpSession session,Model model) {
	   EmpVO empVO = (EmpVO) session.getAttribute("empVO");
       if (empVO != null) {
           model.addAttribute("empVO", empVO);
       }
		return "back-end/leave/listAllLeave";
	}
   
   @GetMapping("/back-end/leave/listAllLeaveforEmp")
   public String listAllLeaveforEmp(HttpSession session, Model model) {
       // 从会话中获取当前登录员工
       EmpVO loggedInEmp = (EmpVO) session.getAttribute("empVO");


       if (loggedInEmp != null) {
           Integer empId = loggedInEmp.getEmpId();
           List<LeaveVO> leaveListData = leaveSvc.getLeavesByEmpId(empId);
           List<LeaveVO> leaveListForAssignee = leaveSvc.getLeaveAssigneeId(empId);
           model.addAttribute("leaveListData", leaveListData);
           model.addAttribute("loggedInEmp", loggedInEmp); // 增加這一行來傳遞登入員工資料到前端
           model.addAttribute("leaveListForAssignee", leaveListForAssignee);

       }

       return "back-end/leave/listAllLeaveforEmp";
   }
  
   
   @ModelAttribute("leaveListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
  	protected List<LeaveVO> referenceListData3(Model model) {
  		
      	List<LeaveVO> list = leaveSvc.getAll();
      	
  		return list;
  	}
   
   //排班=======================================================================
   @GetMapping("/back-end/assign/listAllAssign")
	public String listAllAssign(Model model) {
		return "back-end/assign/listAllAssign";
	}
   

   //訂位=============================
	   //訂位時段資料
	   @ModelAttribute("resTimeListData") // for select_page.html 第135行用
		protected List<ResTimeVO> referenceListData_ResTime(Model model) {
			model.addAttribute("resTImeVO", new ResTimeVO()); // for select_page.html 第133行用
			List<ResTimeVO> list = resTimeSvc.getAll();
			return list;
		}
	 //訂位時段選擇畫面
	   @GetMapping("/restime/select_page")
		public String select_pageResTime(Model model) {
			return "back-end/restime/select_page";
		}
	 //訂位控制選擇畫面
	    @GetMapping("/resc/select_page")
		public String select_pageResc(Model model) {
			return "back-end/resc/select_page";
		}
	    //訂位控制顯示全部畫面
	    @GetMapping("/resc/listAllResC")
		public String listAllResc(Model model) {
			return "back-end/resc/listAllResC";
		}
	    
	    @ModelAttribute("resCListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第117 133行用
		protected List<ResCVO> resListData(Model model) {
			
	    	List<ResCVO> list = resCSvc.getAll();
			return list;
		}
	    
	    @GetMapping("/back-end/res/listAllRes")
	  	public String listAllRes(HttpSession session,Model model) {
	    	EmpVO empVO = (EmpVO) session.getAttribute("empVO");
	        if (empVO != null) {
	            model.addAttribute("empVO", empVO);
	            
	        }
	  		return "back-end/res/listAllRes";
	  	}
	    //Res selectall 的listDate
	    @ModelAttribute("resListData") // for select_page.html 第135行用
		protected List<ResVO> referenceListData_Res(Model model) {
			model.addAttribute("resVO", new ResVO()); // for select_page.html 第133行用
			List<ResVO> list = resSvc.getAll();
			return list;
		}

	    @ModelAttribute("tableTypeListData") // for select_page.html 第135行用
		protected List<TableTypeVO> referenceListData_TableType(Model model) {
			model.addAttribute("tableTypeVO", new TableTypeVO()); // for select_page.html 第133行用
			List<TableTypeVO> list = tableSvc.getAll();
			return list;
		}
	   
	    
	    //訂位按鈕跳轉mapping
	    @GetMapping("/reservation")
	    public String home() {
	        return "back-end/res/addRes"; // 對應的Thymeleaf模板名稱
	    }
	    //訂位時段顯示全部畫面
	    @GetMapping("/back-end/restime/listAllResTime")
		public String listAllResTime(Model model) {
			return "back-end/restime/listAllResTime";
		}
	    //桌型選擇畫面
	    @GetMapping("/tabletype/select_page")
		public String select_tabletype_page(Model model) {
			return "back-end/tabletype/select_page";
		}
	    //桌型顯示全部畫面
	    @GetMapping("back-end/tabletype/listAllTableType")
		public String listAllTableType(Model model) {
			return "back-end/tabletype/listAllTableType2";
		}
	    
	    

	  //後台測試首頁畫面
	    @GetMapping("/backIndex")
		public String backIndex(Model model) {
			return "backIndex";
		}
	    
	   
	    
	    
	    //訂位功能選擇畫面
	    @GetMapping("/res/select_page")
		public String resselect_page(Model model) {
			return "back-end/res/select_page";
		}
	    
	    //後台查看訂單
	    @GetMapping("/order/select_page")
	    public String select_page4(Model model) {
	        return "back-end/order/select_page";
	    }

	    @GetMapping("/order/listAllOrder")
	    public String listAllOrder(Model model) {
	        return "back-end/order/listAllOrder";
	    }

	    @ModelAttribute("orderListData")
	    protected List<OrderVO> referenceListData4(Model model) {
	        List<OrderVO> list = orderSvc.getAll();
	        return list;
	    }
	    
	    //訂單明細畫面
	    @GetMapping("/ordd/select_page")
	    public String select_page5(Model model) {
	        return "back-end/ordd/select_page";
	    }

	    @GetMapping("/ordd/listAllOrdd")
	    public String listAllOrdd(Model model) {
	        return "back-end/ordd/listAllOrdd";
	    }

	    @ModelAttribute("orddListData")
	    protected List<OrddVO> referenceListData5(Model model) {
	        List<OrddVO> list = orddSvc.getAll();
	        return list;
	    }
	    
	    //客製化餐點畫面
	    @GetMapping("/cust/select_page")
	    public String select_page6(Model model) {
	        return "back-end/cust/select_page";
	    }

	    @GetMapping("/cust/listAllCust")
	    public String listAllCust(Model model) {
	        return "back-end/cust/listAllCust";
	    }

	    @ModelAttribute("custListData")
	    protected List<CustVO> referenceListData6(Model model) {
	        List<CustVO> list = custSvc.getAll();
	        return list;
	    }
	    
	    //餐點客製化明細畫面
	    @GetMapping("/meal/select_page")
	    public String select_page7(Model model) {
	        return "back-end/meal/select_page";
	    }

	    @GetMapping("/meal/listAllMeal")
	    public String listAllMeal(Model model) {
	        return "back-end/meal/listAllMeal";
	    }

	    @ModelAttribute("mealListData")
	    protected List<MealVO> referenceListData7(Model model) {
	        List<MealVO> list = mealSvc.getAll();
	        return list;
	    }
	    
	    //前台購物車畫面
	    @GetMapping("/user/cart")
	    public String showCartPage(HttpSession session, Model model) {
	    	MemVO memVO = (MemVO) session.getAttribute("memVO");
	        List<CartVO> cartItems = cartSvc.getCartItemsByMem(memVO.getMemNo());
	        model.addAttribute("cartItems", cartItems);
	        model.addAttribute("memNo", (memVO.getMemNo()));
	        return "back-end/user/cart";
	    }
	    
	    //前台查看歷史訂單畫面
	    @GetMapping("/front-end/order/orderHistory")
	    public String orderHistory(HttpSession session, Model model) {
	        MemVO memVO = (MemVO) session.getAttribute("memVO");
	        if (memVO != null) {
	            model.addAttribute("memVO", memVO);
	            List<OrderVO> orderList = orderSvc.getOrdersByMemNo(memVO.getMemNo());
	            model.addAttribute("orderHistory", orderList);
	        }
	        return "front-end/order/orderHistory";
	    }
	       
	    //後台領餐作業畫面
	    @GetMapping("/meals_status")
	    public String mealsStatus(Model model) {
	        List<OrddVO> orddList = orddSvc.getAll();
	        model.addAttribute("orddList", orddList);
	        return "back-end/ordd/meals_status";
	    }

	    
	  //後台查看訂單
//	    @GetMapping("/all_orders")
//	    public String showAllOrdersPage(Model model) {
//	        List<OrderVO> orders = orderSvc.getAll();
//	        model.addAttribute("orders", orders);
//	        return "back-end/order/all_orders";
//	    }


}