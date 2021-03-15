package com.bill.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/op/*")
public class OpLoginFilter implements Filter
{
	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request= (HttpServletRequest) servletRequest;
		HttpServletResponse response= (HttpServletResponse) servletResponse;
		String uri = request.getRequestURI();
		if(uri.contains("/login.html")){
			filterChain.doFilter(servletRequest,servletResponse);
		}else {
			Object user = request.getSession().getAttribute("op");
			if(user!=null){
				filterChain.doFilter(servletRequest,servletResponse);
			}else {
				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/salary/op/login.html");
			}
		}
	}

	public void init(FilterConfig config) throws ServletException {

	}

}
