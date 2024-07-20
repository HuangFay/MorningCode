package com.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

	
	//後台員工filter
    @Bean
    public FilterRegistrationBean<EmpAuthenticationFilter> employeeAuthFilter() {
        FilterRegistrationBean<EmpAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EmpAuthenticationFilter());
        registrationBean.addUrlPatterns("/back-end/*");
        return registrationBean;
    }
    
    //前台會員filter
    @Bean
    public FilterRegistrationBean<MemFrontFilter> memberAuthFilter() {
        FilterRegistrationBean<MemFrontFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new MemFrontFilter());
        registrationBean.addUrlPatterns("/front-end/res/*");
        registrationBean.addUrlPatterns("/user/*");
        return registrationBean;
    }
}