package com.bill.user.service;

import com.bill.domain.PageBean;
import com.bill.domain.SalaryInfo;
import com.bill.user.dao.UserDao;
import com.bill.user.dao.UserDaoImpl;
import com.bill.user.domain.User;

import java.util.List;

public class UserServiceImpl implements UserService
{
	private UserDao userDao = new UserDaoImpl();

	@Override
	public User login(User loginUser) {
		return userDao.queryUserByIdAndPassword(loginUser.getEmp_id(), loginUser.getEmp_password());
	}

	@Override
	public User checkIfIdExist(User registUser) {
		return userDao.queryUserById(registUser.getEmp_id());
	}

	@Override
	public PageBean<SalaryInfo> querySalaryInfoByPage(String _currentPage, String _rows, String emp_id) {
		//类型转换
		int currentPage = Integer.parseInt(_currentPage);
		int rows = Integer.parseInt(_rows);

		//设置PageBean
		PageBean<SalaryInfo> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setRows(rows);

		//查询总页数
		int infoCount = userDao.querySalaryInfoCount(emp_id);
		pageBean.setTotalCount(infoCount);

		//获得集合
		int start = (currentPage - 1) * rows;
		List<SalaryInfo> list = userDao.querySalaryByPage(emp_id,start, rows);
		pageBean.setList(list);

		//计算总页码
		int totalPage = (infoCount - 1) / rows + 1;
		pageBean.setTotalPage(totalPage);

		return pageBean;
	}
	@Override
	public boolean modifyUserPasswordById(String emp_id, String emp_password) {
		return userDao.modifyUserPasswordById(emp_id, emp_password);
	}

	@Override
	public boolean regist(User registUser) {
		return userDao.regist(registUser.getEmp_id(), registUser.getEmp_password(), registUser.getEmp_name(), "N");
	}


}
