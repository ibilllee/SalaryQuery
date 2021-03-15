package com.bill.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.LogRecord;

@WebFilter("/user/*")
public class UserLoginFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request= (HttpServletRequest) servletRequest;
		HttpServletResponse response= (HttpServletResponse) servletResponse;
		//获得uri
		String uri = request.getRequestURI();
		//登陆页 注册页 予以放行
		if(uri.contains("/login.html")||uri.contains("/regist.html")){
			filterChain.doFilter(servletRequest,servletResponse);
		}else {
			//检测是否有user存在在Session中(已登录?)
			Object user = request.getSession().getAttribute("user");
			if(user!=null){
				filterChain.doFilter(servletRequest,servletResponse);
			}else {
				response.setStatus(response.SC_MOVED_TEMPORARILY);
				response.setHeader("Location", "/salary/user/login.html");
			}
		}
	}
	@Override
	public void destroy() {

	}
}
