package com.bill.user.dao;

import com.bill.domain.SalaryInfo;
import com.bill.user.domain.User;

import java.util.List;

public interface UserDao
{
	//注册用户
	boolean regist(String emp_id, String emp_password, String emp_name, String emp_status);

	//通过身份证与密码查询
	User queryUserByIdAndPassword(String emp_id, String emp_password);

	//通过身份证查询用户
	User queryUserById(String emp_id);

	//通过id查询工资信息条数
	int querySalaryInfoCount(String emp_id);

	//通过id查询工资信息
	List<SalaryInfo> querySalaryByPage(String emp_id, int start, int rows);

	//通过id修改用户密码
	boolean modifyUserPasswordById(String emp_id, String emp_password) ;
}
