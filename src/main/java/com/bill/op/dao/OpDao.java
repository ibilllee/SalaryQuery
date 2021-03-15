package com.bill.op.dao;

import com.bill.domain.SalaryInfo;
import com.bill.op.domain.Op;
import com.bill.user.domain.User;

import java.util.List;
import java.util.Map;

public interface OpDao
{
	//用ID删除工资信息
	void delSalaryByDataId(int id);

	//用emp_id激活
	boolean activateById(String emp_id);

	//用emp_id更改用户密码
	boolean modifyUserPasswordById(String emp_id, String newPassword);

	//用emp_id删除用户账号
	boolean delUserById(String emp_id);

	//用op_id修改管理员密码
	boolean modifyOpPasswordById(String op_id, String op_passwords);

	//插入工资信息
	int insertSalaryInfo(List<SalaryInfo> salaryList);

	//获得工资信息总数
	int querySalaryInfoCount();

	//获取页面数据(带关键字)
	int querySalaryInfoCount(Map<String, String[]> conditionMap);

	//通过身份证查询用户
	User queryUserById(String emp_id);

	//按用户名密码查询管理员信息
	Op queryOpByIdAndPassword(String op_id, String op_password);

	//获取页面数据
	List<SalaryInfo> querySalaryByPage(int start, int rows);

	//获取页面数据(带关键字)
	List<SalaryInfo> querySalaryByPage(int start, int rows, Map<String, String[]> conditionMap);

	//获取员工信息列表
	List<User> queryUserListInfoByKey(String emp_name);
}
