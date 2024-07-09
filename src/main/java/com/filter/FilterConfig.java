package com.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<EmpAuthenticationFilter> employeeAuthFilter() {
        FilterRegistrationBean<EmpAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new EmpAuthenticationFilter());
        registrationBean.addUrlPatterns("/back-end/*");
        return registrationBean;
    }
}