package com.filter;

import java.io.IOException;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.morning.emp.model.EmpVO;
import com.morning.func.FuncVO;

@Component
public class EmpAuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        boolean isLoggedIn = (session != null && session.getAttribute("empVO") != null);
        String loginURI = httpRequest.getContextPath() + "/back-end/emplogin";
        String logoutURI = httpRequest.getContextPath() + "/back-end/emplogout";
        String noAccessURI = httpRequest.getContextPath() + "/no-access"; // 沒權限的轉跳
        String requestURI = httpRequest.getRequestURI();

        if (isLoggedIn || requestURI.equals(loginURI) || requestURI.equals(logoutURI) || requestURI.endsWith("emplogin") || requestURI.contains("/api/")) {
            
            if (isLoggedIn) {
                EmpVO empVO = (EmpVO) session.getAttribute("empVO");

                if (requestURI.equals(logoutURI) || (empVO.getEmpStatus() == 0 && hasPermission(session, requestURI))) {
                    chain.doFilter(request, response);
                    return;
                }

                if (empVO.getEmpStatus() != 0 || !hasPermission(session, requestURI)) {
                    httpResponse.sendRedirect(noAccessURI);
                    return;
                }
            }
            chain.doFilter(request, response);
        } 
    else {
    	if (session == null) {
    		//取得session
            session = httpRequest.getSession(true);
            
        }
    	//存session 到變數
        session.setAttribute("location", requestURI);
        //傳球
        httpResponse.sendRedirect(loginURI);
    }
    }

    private boolean hasPermission(HttpSession session, String requestURI) {
    	
    	 if (requestURI.contains("/back-end/leave/listAllLeaveforEmp") || requestURI.contains("/back-end/assign/listAllAssign")||
    			 requestURI.contains("/back-end/assign/api/")||requestURI.contains("/back-end/leave/addLeave")||requestURI.contains("/back-end/leave/insert")||
    			 requestURI.contains("/back-end/leave/delete")) {
             return true;
         }
    	
        @SuppressWarnings("unchecked")
        Set<FuncVO> permissions = (Set<FuncVO>) session.getAttribute("empPermissions");

        if (permissions != null) {
            for (FuncVO permission : permissions) {
                // URI和權限ID判斷
            	//員工管理
                if (requestURI.contains("/back-end/emp") && permission.getFunctionId() == 1 ) {
                    return true;
                }

                if (requestURI.contains("/back-end/leave") && permission.getFunctionId() == 1) {
                	return true;
                }
                if (requestURI.contains("/back-end/assign") && permission.getFunctionId() == 1) {
                	return true;
                }
                //前台客戶相關管理
                if (requestURI.contains("/back-end/mem") && permission.getFunctionId() == 2) {
                    return true;
                }
                if (requestURI.contains("/order") && permission.getFunctionId() == 2) {
                    return true;
                }
              
                if (requestURI.contains("/ordd") && permission.getFunctionId() == 2) {
                	return true;
                }
                
                //最新消息管理
                if (requestURI.contains("/news/all") && permission.getFunctionId() == 3) {
                	return true;
                }
                //客服相關管理
                if (requestURI.contains("/forum/reports") && permission.getFunctionId() == 4) {
                	return true;
                }
                if (requestURI.contains("/back-end/customer-service") && permission.getFunctionId() == 4) {
                	return true;
                }
                //菜單相關設定
                if (requestURI.contains("/back-end/meals") && permission.getFunctionId() == 5) {
                    return true;
                }
                if (requestURI.contains("/back-end/mealstypes") && permission.getFunctionId() == 5) {
                	return true;
                }

                //訂位相關管理
                if (requestURI.contains("/back-end/res") && permission.getFunctionId() == 6) {

                    return true;
                }
                if (requestURI.contains("/back-end/tabletype") && permission.getFunctionId() == 6) {
                	
                	return true;
                }
                if (requestURI.contains("/back-end/restime") && permission.getFunctionId() == 6) {
                	
                	return true;
                }
            }
        }
        return false;
    }
}
