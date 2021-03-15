package com.bill.web.servlet;

import com.bill.domain.PageBean;
import com.bill.domain.SalaryInfo;
import com.bill.op.domain.Op;
import com.bill.domain.ResultInfo;
import com.bill.op.service.OpService;
import com.bill.op.service.OpServiceImpl;
import com.bill.user.domain.User;
import com.bill.utils.DeleteFileUtil;
import com.bill.utils.FileEncodingUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@WebServlet("/opServlet/*")
public class OpServlet extends BaseServlet
{
	private OpService opService = new OpServiceImpl();

	//登陆
	public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();
		//验证验证码
		String[] captchas = dataMap.get("CAPTCHA");

		//验证码如果错误
		if (!checkCAPTCHA(captchas[0], request)) {
			//记录错误信息
			ResultInfo resultInfo = new ResultInfo();
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("验证码错误");
			//错误信息序列化为json,写回界面
			ObjectMapper objectMapper = new ObjectMapper();
			String json = objectMapper.writeValueAsString(resultInfo);
			response.setContentType("application/json;charset=utf-8");
			response.getWriter().write(json);
			return;
		}

		//创建用户进行验证
		Op loginOp = new Op();
		try {
			BeanUtils.populate(loginOp, dataMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		//查询用户信息
		Op op = opService.login(loginOp);
		ResultInfo resultInfo = new ResultInfo();

		//没有查询到用户
		if (op == null) {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("用户名或密码错误");
		}
		else {
			request.getSession().setAttribute("op", op);
			resultInfo.setFlag(true);
		}
		//结果写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//退出登陆
	public void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//清空session
		request.getSession().invalidate();
		response.sendRedirect("/salary/op/login.html");
	}

	//检查管理员密码
	public void checkOpPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();
		//验证验证码
		String[] op_passwords = dataMap.get("op_password");

		ResultInfo resultInfo = new ResultInfo();
		Op op = (Op) request.getSession().getAttribute("op");

		//旧密码与数据库中不同
		if (!op.getOp_password().equals(op_passwords[0])) {
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

	//修改管理员密码
	public void modifyOpPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取登录信息
		Map<String, String[]> dataMap = request.getParameterMap();
		//验证验证码
		String[] op_passwords = dataMap.get("op_password");

		ResultInfo resultInfo = new ResultInfo();
		Op op = (Op) request.getSession().getAttribute("op");
		//用op_id修改管理员密码
		boolean result = opService.modifyOpPasswordById(op.getOp_id(), op_passwords[0]);

		//修改失败
		if (!result) {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("修改密码失败！");
		}
		//修改成功
		else {
			resultInfo.setFlag(true);
			resultInfo.setErrorMsg("修改密码成功！");
			//更改Session中的op
			request.getSession().removeAttribute("op");
			op.setOp_password(op_passwords[0]);
			request.getSession().setAttribute("op", op);
		}
		//结果写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//上传文件
	public void uploadSalaryFile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getSession().removeAttribute("checkSalaryInfo");
		request.getSession().removeAttribute("insertSalaryList");

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		DiskFileItemFactory sf = new DiskFileItemFactory();//实例化磁盘被文件列表工厂
		String path = request.getRealPath("/upload");//得到上传文件的存放目录
		//若没有目录，创建目录
		File dirCreate = new File(path);
		if(!dirCreate.exists()){
			dirCreate.mkdir();
		}

		sf.setRepository(new File(path));//设置文件存放目录
		sf.setSizeThreshold(1024 * 1024);//设置文件上传小于1M放在内存中
		String rename = "";//文件新生成的文件名
		String fileName = "";//文件原名称
		String name = "";//普通field字段
		//从工厂得到servletupload文件上传类
		ServletFileUpload sfu = new ServletFileUpload(sf);

		try {
			List<FileItem> lst = sfu.parseRequest(request);//得到request中所有的元素
			for (FileItem fileItem : lst) {
				if (fileItem.isFormField()) {
					if ("name".equals(fileItem.getFieldName())) {
						name = fileItem.getString("UTF-8");
					}
				}
				else {
					//获得文件名称
					fileName = fileItem.getName();
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
					String houzhui = fileName.substring(fileName.lastIndexOf("."));
					rename = new Date().getTime() + houzhui;//随机名称
					fileItem.write(new File(path, rename));
				}
			}

		} catch (Exception e) {
			//有异常，上传文件失败
			e.printStackTrace();
			//删除服务器中的文件
			DeleteFileUtil.deleteFile(path + "/" + rename);
			response.sendRedirect("../op/add.jsp");
			out.flush();
			out.close();
			return;
		}

		//上传成功，获取工资对象集合，存入Session，重定向至jsp
		List<SalaryInfo> salaryList = null;
		try {
			//获取工资列表
			salaryList = opService.getSalaryList(path + "/" + rename);
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			//删除服务器中的文件
			DeleteFileUtil.deleteFile(path + "/" + rename);
			//验证上传文件中的姓名与身份证号
			String checkInfo = opService.checkSalaryList(salaryList);
			if (checkInfo != null || !"".equals(checkInfo))
				request.getSession().setAttribute("checkSalaryInfo", checkInfo);
			request.getSession().setAttribute("insertSalaryList", salaryList);
			response.sendRedirect("/salary/op/add.jsp");
			out.flush();
			out.close();
		}
	}

	//插入上传文件数据
	public void insertSalaryInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//获取session中的工资列表
		List<SalaryInfo> salaryList = (List<SalaryInfo>) request.getSession().getAttribute("insertSalaryList");
		int result = opService.insertSalaryInfo(salaryList);
		ResultInfo resultInfo = new ResultInfo();
		//有成功录入的记录
		if (result > 0) {
			resultInfo.setFlag(true);
			resultInfo.setErrorMsg("成功录入 " + result + " 条记录！");
		}
		//没有成功录入的记录
		else {
			resultInfo.setFlag(false);
			resultInfo.setErrorMsg("录入失败！");
		}
		//去除session中的工资列表,避免重复录入
		request.getSession().removeAttribute("checkSalaryInfo");
		request.getSession().removeAttribute("insertSalaryList");
		//结果写回页面
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(resultInfo);
		response.setContentType("application/json;charset=utf-8");
		response.getWriter().write(json);
	}

	//取消录入数据
	public void cancelInsertSalaryInfo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		//情况session中的缓存
		request.getSession().removeAttribute("checkSalaryInfo");
		request.getSession().removeAttribute("insertSalaryList");
	}

	//分页查询
	public void querySalaryInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取当前页 行数的参数
		String currentPage = request.getParameter("currentPage");
		String rows = request.getParameter("rows");
		//参数为空,设置默认参数
		if (currentPage == null || "".equals(currentPage))
			currentPage = "1";
		if (rows == null || "".equals(rows))
			rows = "8";

		//查询工资信息
		PageBean<SalaryInfo> pb = opService.querySalaryInfoByPage(currentPage, rows);

		//写入request域,转发回页面
		if (pb.getTotalCount() != 0)
			request.setAttribute("pb", pb);
		request.getRequestDispatcher("/op/main.jsp").forward(request, response);
	}

	//分页关键字查询
	public void querySalaryInfoByKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取当前页 行数的参数
		String currentPage = request.getParameter("currentPage");
		String rows = request.getParameter("rows");
		Map<String, String[]> condition = new HashMap<String, String[]>(request.getParameterMap());

		//转换iso8859-1至utf8
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			Set<String> conditionSet = condition.keySet();
			for (String key : conditionSet) {
				String[] value = condition.get(key);
				if (value.length == 0 || "".equals(value[0])) continue;

				//condition.remove(key);
				String[] tmpStrings = new String[1];
				tmpStrings[0] = FileEncodingUtils.ISOtoUtf8(value[0]);
				condition.replace(key, tmpStrings);
			}
		}

		//设置默认参数
		if (currentPage == null || "".equals(currentPage))
			currentPage = "1";
		if (rows == null || "".equals(rows))
			rows = "8";

		//查询工资列表
		PageBean<SalaryInfo> pb = opService.querySalaryInfoByPageByKey(currentPage, rows, condition);

		//写入request域,转发回页面
		if (pb.getTotalCount() != 0) {
			request.setAttribute("pb", pb);
			request.setAttribute("rows", rows);
			request.setAttribute("condition", condition);
		}
		request.getRequestDispatcher("../op/main.jsp").forward(request, response);
	}

	//删除选中工资
	public void delSalaryInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] data_ids = null;
		//请求中有id参数
		if (request.getParameter("data_id") != null)
			data_ids = request.getParameterValues("data_id");

		//删除工资信息
		if (data_ids != null)
			opService.delSalaryByDataId(data_ids);

		response.sendRedirect("querySalaryInfo");
	}

	//改变一页显示条数(8/300)
	public void changeRows(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//判断一页显示数
		String rows = request.getParameter("rows");
		//只有8条
		if (rows == null || "".equals(rows) || Integer.parseInt(rows) <= 10) {
			request.removeAttribute("rows");
			request.getRequestDispatcher("querySalaryInfoByKey?rows=300").forward(request, response);
		}//有300条
		else {
			request.removeAttribute("rows");
			request.getRequestDispatcher("querySalaryInfoByKey?rows=8").forward(request, response);
		}
	}

	//获取员工账号信息
	public void queryUserInfoByKey(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emp_name = request.getParameter("emp_name");
		if (emp_name == null || "".equals(emp_name))
			emp_name = "";

		//转换iso8859-1至utf8
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			emp_name = FileEncodingUtils.ISOtoUtf8(emp_name);
		}

		//获取运功账号列表
		List<User> list = opService.queryUserListInfoByKey(emp_name);
		System.out.println(list);
		//写入request域,写回页面
		request.setAttribute("userList", list);
		request.getRequestDispatcher("/op/userManage.jsp").forward(request, response);
	}

	//激活账号
	public void activate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//从request获取ID
		String emp_id = getEmpIdFromRequest(request, response);
		String opResult;
		//激活 成功
		if (opService.activateById(emp_id)) {
			opResult = "成功激活账号";
		}//失败
		else {
			opResult = "激活账号失败";
		}
		//写入request域,写回页面
		request.setAttribute("opResult", opResult);
		queryUserInfoByKey(request, response);
	}

	//恢复密码
	public void recoverPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//从request获取ID
		String emp_id = getEmpIdFromRequest(request, response);
		String opResult = "";
		//恢复 成功
		if (opService.recoverPasswordById(emp_id)) {
			opResult = "成功恢复密码为身份证后6位:" + emp_id.substring(12);
		}//失败
		else {
			opResult = "恢复密码失败";
		}
		//写入request域,写回页面
		request.setAttribute("opResult", opResult);
		queryUserInfoByKey(request, response);

	}

	//删除用户
	public void delUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//从request获取ID
		String emp_id = getEmpIdFromRequest(request, response);
		String opResult = "";
		//删除 成功
		if (opService.delUserById(emp_id)) {
			opResult = "成功删除用户";
		}//失败
		else {
			opResult = "删除用户失败";
		}
		//写入request域,写回页面
		request.setAttribute("opResult", opResult);
		queryUserInfoByKey(request, response);
	}

	//从request获取ID
	private String getEmpIdFromRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String emp_id = request.getParameter("emp_id");
		//id为空,转发回页面
		if (emp_id == null || "".equals(emp_id))
			request.getRequestDispatcher("/op/userManage.jsp").forward(request, response);
		//判断是非需要编码转换
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			emp_id = FileEncodingUtils.ISOtoUtf8(emp_id);
		}
		return emp_id;
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

}
