package com.bill.web.servlet;

import com.bill.domain.PageBean;
import com.bill.domain.ResultInfo;
import com.bill.domain.SalaryInfo;
import com.bill.op.domain.Op;
import com.bill.user.domain.User;
import com.bill.user.service.UserService;
import com.bill.user.service.UserServiceImpl;
import com.bill.utils.Md5Util;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/userServlet/*")
public class UserServlet extends BaseServlet
{
	private final UserService userService = new UserServiceImpl();

	//登陆
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();
		//验证验证码
		String[] captchas = dataMap.get("CAPTCHA");

		//验证码错误
		if (!checkCAPTCHA(captchas[0], request)) {
			ResultInfo resultInfo = new ResultInfo();
			//设置信息
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("验证码错误");
			//结果序列化为json,写回页面
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(resultInfo);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(json);
			return;
		}

		//创建用户进行验证
		User loginUser = new User();
		try {
			BeanUtils.populate(loginUser, dataMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		//查询用户信息
		User user = userService.login(loginUser);
		ResultInfo resultInfo = new ResultInfo();
		//查找不到用户
		if (user == null) {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("用户名或密码错误");
		}//用户没被激活
		else if (!"Y".equals(user.getEmp_status())) {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("该用户尚未被管理员验证");
		}//成功登陆
		else {
			request.getSession().setAttribute("user", user);
			resultInfo.setFlag(true);
		}
		//结果序列化为json,写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//退出登陆
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//清空session
		request.getSession().invalidate();
		response.sendRedirect("/salary/user/login.html");
	}

	//注册
	public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("?");
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();

		//验证验证码
		String[] captchas = dataMap.get("CAPTCHA");
		System.out.println(captchas[0]);

		//验证码错误
		if (!checkCAPTCHA(captchas[0], request)) {
			ResultInfo resultInfo = new ResultInfo();
			//设置错误信息
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("验证码错误");
			System.out.println("!");
			//结果序列化为json
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(resultInfo);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(json);
			return;
		}

		//创建用户进行验证
		User registUser = new User();
		try {
			BeanUtils.populate(registUser, dataMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		//查询用户信息
		boolean flag = userService.regist(registUser);
		ResultInfo resultInfo = new ResultInfo();
		//注册成功
		if (flag) {
			resultInfo.setFlag(true);
			resultInfo.setErrorMsg("注册成功！请等待管理员验证");
		}
		else {//注册失败
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("注册异常！请联系管理员");
		}
		//结果序列化为json,写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//检查用户名是否已经存在
	public void checkIfIdExist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数列表
		Map<String, String[]> dataMap = request.getParameterMap();

		//创建用户进行验证
		User registUser = new User();
		try {
			BeanUtils.populate(registUser, dataMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		System.out.println(registUser);

		//记录检查信息
		User user = userService.checkIfIdExist(registUser);
		ResultInfo resultInfo = new ResultInfo();
		//数据库中id没有重复
		if (user == null) {
			resultInfo.setFlag(true);
		}
		else {//数据库中重复了
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("身份证已被注册");
		}

		//写回json
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//验证验证码
	private boolean checkCAPTCHA(String loginCAPTCHA, HttpServletRequest request) {
		HttpSession session = request.getSession();
		String captcha_server = (String) session.getAttribute("CAPTCHA_SERVER");

		//删除Session中的验证码，确保只使用一次
		session.removeAttribute("CAPTCHA_SERVER");
		if (captcha_server == null || !captcha_server.equalsIgnoreCase(loginCAPTCHA)) {
			return false;
		}
		return true;
	}

	//分页查询
	public void querySalaryInfoByPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取当前页 行数 用户身份证的参数
		String currentPage = request.getParameter("currentPage");
		String rows = request.getParameter("rows");
		User user = (User) request.getSession().getAttribute("user");
		String emp_id = user.getEmp_id();

		//参数为空,设置默认参数
		if (currentPage == null || "".equals(currentPage))
			currentPage = "1";
		if (rows == null || "".equals(rows))
			rows = "8";

		//查询工资信息
		PageBean<SalaryInfo> pb = userService.querySalaryInfoByPage(currentPage, rows, emp_id);

		//写入request域,转发回页面
		if (pb.getTotalCount() != 0)
			request.setAttribute("pb", pb);
		request.getRequestDispatcher("/user/main.jsp").forward(request, response);
	}

	//检查用户密码
	public void checkUserPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();
		//验证验证码
		String[] user_passwords = dataMap.get("user_password");

		ResultInfo resultInfo = new ResultInfo();
		User user = (User) request.getSession().getAttribute("user");

		//旧密码与数据库中不同
		if (!user.getEmp_password().equals(Md5Util.encodeByMd5(user_passwords[0]))) {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("旧密码错误");
		}
		else {
			resultInfo.setFlag(true);
		}

		//结果写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//修改用户密码
	public void modifyUserPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();
		//验证验证码
		String[] user_passwords = dataMap.get("user_password");

		ResultInfo resultInfo = new ResultInfo();
		User user = (User) request.getSession().getAttribute("user");
		//用user_id修改管理员密码
		boolean result = userService.modifyUserPasswordById(user.getEmp_id(), user_passwords[0]);

		//修改失败
		if (!result) {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("修改密码失败！");
		}
		//修改成功
		else {
			resultInfo.setFlag(true);
			resultInfo.setErrorMsg("修改密码成功！");
			//更改Session中的user
			request.getSession().removeAttribute("user");
			user.setEmp_password(user_passwords[0]);
			request.getSession().setAttribute("user", user);
		}
		//结果写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}


}
