package com.bill.web.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class MainPageFilter implements Filter
{
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request= (HttpServletRequest) servletRequest;
		HttpServletResponse response= (HttpServletResponse) servletResponse;

		//解决乱码
		String method = request.getMethod();
		//解决post请求中文数据乱码问题
		if(method.equalsIgnoreCase("post")){
			request.setCharacterEncoding("utf-8");
		}

		//保证静态文件被放行
		String uri = request.getRequestURI();
		if (uri.contains(".css") || uri.contains(".js") || uri.contains(".png")) {
			filterChain.doFilter(request, response);
			return ;
		}

		//处理响应乱码
		response.setContentType("text/html;charset=utf-8");

		//重定向到用户登陆页
		if("/salary/".equals(uri)){
			response.setStatus(response.SC_MOVED_TEMPORARILY);
			response.setHeader("Location", "user/login.html");
		}else{
			filterChain.doFilter(servletRequest,servletResponse);
		}

	}
	@Override
	public void destroy() {

	}
}
