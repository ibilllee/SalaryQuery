package com.bill.user.dao;

import com.bill.domain.SalaryInfo;
import com.bill.user.domain.User;
import com.bill.utils.JDBCUtils;
import com.bill.utils.Md5Util;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class UserDaoImpl implements UserDao
{
	private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

	@Override
	public User queryUserByIdAndPassword(String emp_id, String emp_password) {
		User user = null;
		emp_password = Md5Util.encodeByMd5(emp_password);
		try {
			//通过用户id和密码查询用户,封装为对象
			String sql = "select * from tab_user_info where emp_id = ? and emp_password = ?";
			user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), emp_id, emp_password);
		} catch (Exception e) {
		}
		return user;
	}

	@Override
	public User queryUserById(String emp_id) {
		User user = null;
		try {
			//通过用户id查询用户,封装为对象
			String sql = "select * from tab_user_info where emp_id = ?";
			user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), emp_id);
		} catch (Exception e) {
		}
		return user;
	}
	@Override
	public int querySalaryInfoCount(String emp_id) {
		int count = 0;
		String sql = "select count(*) from tab_salary_data where emp_id = ?";
		try {
			//返回工资记录条数
			count = template.queryForObject(sql, Integer.class, emp_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	@Override
	public List<SalaryInfo> querySalaryByPage(String emp_id, int start, int rows) {
		//定义sql 工资id倒序查找 确定start起始数和rows行数
		String sql = "select * from tab_salary_data where emp_id = ? order by data_id desc limit ? , ?";
		List<SalaryInfo> infoList = null;
		try {
			//进行查询,封装进list
			infoList = template.query(sql, new BeanPropertyRowMapper<SalaryInfo>(SalaryInfo.class), emp_id, start, rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}
	@Override
	public boolean modifyUserPasswordById(String emp_id, String emp_password) {
		//用用户id修改用户密码

		emp_password = Md5Util.encodeByMd5(emp_password);
		String sql = "update tab_user_info set emp_password = ? where emp_id = ?";
		int result = 0;
		try {
			result = template.update(sql, emp_password, emp_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//验证操作结果是否为1
		return result == 1;
	}

	@Override
	public boolean regist(String emp_id, String emp_password, String emp_name, String emp_status) {
		emp_password = Md5Util.encodeByMd5(emp_password);

		int result = 0;
		try {
			//输入数据至数据库完成注册
			String sql = "insert into tab_user_info(emp_id,emp_password,emp_name,emp_status) values(?,?,?,?)";
			result = template.update(sql, emp_id, emp_password, emp_name, emp_status);
		} catch (Exception e) {
		}
		return result > 0;
	}
}
