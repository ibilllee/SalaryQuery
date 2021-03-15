package com.bill.user.service;

import com.bill.domain.SalaryInfo;
import com.bill.domain.PageBean;
import com.bill.user.domain.User;

public interface UserService
{
	//注册用户
	boolean regist(User registUser);

	//登录服务
	User login(User loginUser);

	//检查用户名是否存在
	User checkIfIdExist(User registUser);

	//分页查询
	PageBean<SalaryInfo> querySalaryInfoByPage(String _currentPage, String _rows, String emp_id);

	//通过id修改用户密码
	boolean modifyUserPasswordById(String emp_id, String emp_password);
}
