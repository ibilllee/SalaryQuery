package com.bill.op.dao;

import com.bill.domain.SalaryInfo;
import com.bill.op.domain.Op;
import com.bill.user.domain.User;
import com.bill.utils.JDBCUtils;
import com.bill.utils.Md5Util;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OpDaoImpl implements OpDao
{
	private final JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

	@Override
	public Op queryOpByIdAndPassword(String op_id, String op_password) {
		Op op = null;
		try {
			//在 管理员表 按 ID和密码 查询
			String sql = "select * from tab_op_info where op_id = ? and op_password = ?";
			op = template.queryForObject(sql, new BeanPropertyRowMapper<>(Op.class), op_id, op_password);
		} catch (Exception e) {
		}
		return op;
	}

	@Override
	public int insertSalaryInfo(List<SalaryInfo> salaryList) {
		int result = 0;
		for (SalaryInfo info : salaryList) {
			try {
				//向工资表插入数据,返回成功操作数
				String sql = "insert into tab_salary_data(data_id,emp_id,emp_name,data_date,salary_number,data_cont) " +
						"values(?,?,?,?,?,?)";
				result += template.update(sql, null, info.getEmp_id(), info.getEmp_name(), info.getData_date(),
						info.getSalary_number(), info.getData_cont());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@Override
	public int querySalaryInfoCount() {
		int count = 0;
		String sql = "select count(*) from tab_salary_data";
		try {
			//返回工资记录条数
			count = template.queryForObject(sql, Integer.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public int querySalaryInfoCount(Map<String, String[]> conditionMap) {
		int count = 0;
		String sql = "select count(*) from tab_salary_data where 1 = 1  ";

		//sql参数表
		ArrayList<Object> values = new ArrayList<>();

		//拼接查询语句
		StringBuilder stringBuilder = new StringBuilder(sql);
		Set<String> keySet = conditionMap.keySet();
		//遍历条件参数
		for (String key : keySet) {
			if ("currentPage".equals(key) || "rows".equals(key)) {
				continue;
			}
			//非页码与行数的参数,加入sql语句
			String value = conditionMap.get(key)[0];
			if (value != null && !"".equals(value)) {
				stringBuilder.append(" and " + key + " like ? ");
				//加条件参数
				values.add("%" + value + "%");
			}
		}
		//转换格式存入sql语句
		sql = stringBuilder.toString();

		try {
			//按sql语句查询
			count = template.queryForObject(sql, Integer.class, values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
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
	public List<SalaryInfo> querySalaryByPage(int start, int rows) {
		//定义sql 工资id倒序查找 确定start起始数和rows行数
		String sql = "select * from tab_salary_data order by data_id desc limit ? , ?";
		List<SalaryInfo> infoList = null;
		try {
			//进行查询,封装进list
			infoList = template.query(sql, new BeanPropertyRowMapper<SalaryInfo>(SalaryInfo.class), start, rows);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}

	@Override
	public List<SalaryInfo> querySalaryByPage(int start, int rows, Map<String, String[]> conditionMap) {
		//初始sql
		String sql = "select * from tab_salary_data where 1 = 1 ";
		List<SalaryInfo> infoList = null;

		//sql参数表
		ArrayList<Object> values = new ArrayList<>();
		//拼接查询语句
		StringBuilder stringBuilder = new StringBuilder(sql);
		Set<String> keySet = conditionMap.keySet();
		for (String key : keySet) {
			if ("currentPage".equals(key) || "rows".equals(key)) {
				continue;
			}
			//非当前页码和行数的参数,拼接到sql中
			String value = conditionMap.get(key)[0];
			if (value != null && !"".equals(value)) {
				stringBuilder.append(" and " + key + " like ? ");
				//加条件参数
				values.add("%" + value + "%");
			}
		}
		//加入数据id倒序约束 和 起始条,行数 约束
		stringBuilder.append(" order by data_id desc limit ?,? ");
		values.add(start);
		values.add(rows);
		//改变格式存入sql
		sql = stringBuilder.toString();

		try {
			infoList = template.query(sql, new BeanPropertyRowMapper<SalaryInfo>(SalaryInfo.class), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return infoList;
	}

	@Override
	public void delSalaryByDataId(int id) {
		//用数据id删除工资
		String sql = "delete from tab_salary_data where data_id = ?";
		try {
			template.update(sql, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> queryUserListInfoByKey(String emp_name) {
		String sql = null;
		List<User> result = null;

		//按1.激活状态 2.姓名拼音排序
		try {
			//没有约束
			if ("".equals(emp_name) || emp_name == null) {
				sql = "SELECT * FROM tab_user_info ORDER BY emp_status,CONVERT(emp_name USING gbk)";
				result = template.query(sql, new BeanPropertyRowMapper<>(User.class));
			}
			//添加姓名查询约束
			else {
				sql = "SELECT * FROM tab_user_info WHERE emp_name LIKE ? ORDER BY emp_status,CONVERT(emp_name USING gbk)";
				result = template.query(sql, new BeanPropertyRowMapper<>(User.class), "%" + emp_name + "%");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//验证操作结果是否为1
		return result;
	}

	@Override
	public boolean activateById(String emp_id) {
		//用用户id激活用户
		String sql = "update tab_user_info set emp_status = ? where emp_id = ?";
		int result = 0;
		try {
			//设置状态为Y表示激活
			result = template.update(sql, "Y", emp_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//验证操作结果是否为1
		return result == 1;
	}

	@Override
	public boolean modifyUserPasswordById(String emp_id, String newPassword) {
		//用用户id修改用户密码
			newPassword= Md5Util.encodeByMd5(newPassword);

		String sql = "update tab_user_info set emp_password = ? where emp_id = ?";
		int result = 0;
		try {
			result = template.update(sql, newPassword, emp_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//验证操作结果是否为1
		return result == 1;
	}

	@Override
	public boolean delUserById(String emp_id) {
		//用用户id删除用户
		String sql = "DELETE FROM tab_user_info WHERE emp_id = ?";
		int result = 0;
		try {
			result = template.update(sql, emp_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//验证操作结果是否为1
		return result == 1;
	}

	@Override
	public boolean modifyOpPasswordById(String op_id, String op_passwords) {
		//用管理员id删除管理员
		String sql = "update tab_op_info set op_password = ? where op_id = ?";
		int result = 0;
		try {
			result = template.update(sql, op_passwords, op_id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//验证操作结果是否为1
		return result == 1;
	}
}
