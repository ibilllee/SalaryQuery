package com.bill.op.service;

import com.bill.domain.PageBean;
import com.bill.domain.SalaryInfo;
import com.bill.op.domain.Op;
import com.bill.user.domain.User;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface OpService
{
	//通过数据id删除工资数据
	void delSalaryByDataId(String[] data_ids);

	//用ID激活账户
	boolean activateById(String emp_id);

	//用ID恢复密码
	boolean recoverPasswordById(String emp_id);

	//用ID删除用户
	boolean delUserById(String emp_id);

	//用ID修改管理员密码
	boolean modifyOpPasswordById(String op_id, String op_passwords);

	//插入工资数据进入数据库
	int insertSalaryInfo(List<SalaryInfo>  salaryList);

	//员工登陆
	Op login(Op op);

	//通过文件路径获得工资数据表
	List<SalaryInfo> getSalaryList(String savePath) throws IOException;

	//获取用户信息表,带关键字查询
	List<User> queryUserListInfoByKey(String emp_name);

	//分页获取工资信息
	PageBean<SalaryInfo> querySalaryInfoByPage(String _currentPage, String _rows);

	//分页获取工资信息,带关键字查询
	PageBean<SalaryInfo> querySalaryInfoByPageByKey(String _currentPage, String _rows, Map<String, String[]> conditionMap);

	//验证工资单中身份证与姓名的对应
	String checkSalaryList(List<SalaryInfo> salaryList);
}
