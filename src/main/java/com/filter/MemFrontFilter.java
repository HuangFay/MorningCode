package com.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

@Component
public class MemFrontFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        
        boolean isLoggedIn = (session != null && session.getAttribute("memVO") != null);
        String loginURI = httpRequest.getContextPath() + "/front-end/mem/signup";
        String requestURI = httpRequest.getRequestURI();

        if (isLoggedIn || requestURI.equals(loginURI)) {
            chain.doFilter(request, response); 
        } else {
        	if(session == null) {
        		session = httpRequest.getSession(true);
        	}
        	session.setAttribute("location", requestURI);
            httpResponse.sendRedirect(loginURI); 
        }
    }
}
