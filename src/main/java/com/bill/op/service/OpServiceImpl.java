package com.bill.op.service;

import com.alibaba.druid.util.StringUtils;
import com.bill.domain.PageBean;
import com.bill.domain.SalaryInfo;
import com.bill.op.dao.OpDao;
import com.bill.op.dao.OpDaoImpl;
import com.bill.op.domain.Op;
import com.bill.user.domain.User;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpServiceImpl implements OpService
{
	private OpDao opDao = new OpDaoImpl();
	private static FormulaEvaluator evaluator;

	@Override
	public Op login(Op op) {
		return opDao.queryOpByIdAndPassword(op.getOp_id(), op.getOp_password());
	}

	//获取工资列表（从上传的文件）
	@Override
	public List<SalaryInfo> getSalaryList(String savePath) throws IOException, NullPointerException {
		ArrayList<SalaryInfo> salaryInfos = new ArrayList<>();
		//通过保存目录打开excel文件
		File file = new File(savePath);

		//新建Excel工作本对象
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(file);
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
		}

		try {
			//获取第一页
			Sheet sheet = workbook.getSheetAt(0);

			//获取行列
			int row = sheet.getLastRowNum() + 1;
			Row tmp = sheet.getRow(0);
			if (tmp == null)
				return null;
			int col = tmp.getPhysicalNumberOfCells();

			//遍历每行工资信息
			for (int ri = 1; ri < row; ri++) {
				Row r = sheet.getRow(ri);
				SalaryInfo info = new SalaryInfo();
				//设置信息
				info.setEmp_name(getCellValueByCell(r.getCell(0)));
				info.setEmp_id(getCellValueByCell(r.getCell(1)));
				info.setData_date(getCellValueByCell(r.getCell(2)));
				info.setSalary_number(getCellValueByCell(r.getCell(col - 1)));

				//设置详情,包装成html格式,每5行进行一次换行

				//获取应发的个数(pCount)
				int pCount = 0;
				for (int ci = 3; ci < col; ci++) {
					pCount++;
					if ("应发合计".equals(getCellValueByCell(sheet.getRow(0).getCell(ci))))
						break; ;
				}
				//获取应扣的个数(mCount)
				int mCount = (col - 3) - pCount - 1;
				int maxCount = Math.max(mCount, pCount);

				String data_cont = "<table border='1' class='table table-bordered table-hover'>";

				for (int pi = 3, mi = 3 + pCount, count = 0; count < maxCount; pi++, mi++, count++) {
					data_cont=data_cont.concat("<tr>");

					data_cont=data_cont.concat("<td>");
					if (pi==3+pCount-1)
						data_cont=data_cont.concat("<b>");
					if (pi<3+pCount)
						data_cont=data_cont.concat(getCellValueByCell(sheet.getRow(0).getCell(pi)));
					if (pi==3+pCount-1)
						data_cont=data_cont.concat("</b>");
					data_cont=data_cont.concat("</td>");

					data_cont=data_cont.concat("<td>");
					if (pi==3+pCount-1)
						data_cont=data_cont.concat("<b>");
					if (pi<3+pCount)
						data_cont=data_cont.concat(getCellValueByCell(r.getCell(pi)));
					if (pi==3+pCount-1)
						data_cont=data_cont.concat("</b>");
					data_cont=data_cont.concat("</td>");

					data_cont=data_cont.concat("<td>");
					if (mi==col-1-1)
						data_cont=data_cont.concat("<b>");
					if(mi<col-1)
						data_cont=data_cont.concat(getCellValueByCell(sheet.getRow(0).getCell(mi)));
					if (mi==col-1-1)
						data_cont=data_cont.concat("</b>");
					data_cont=data_cont.concat("</td>");

					data_cont=data_cont.concat("<td>");
					if (mi==col-1-1)
						data_cont=data_cont.concat("<b>");
					if(mi<col-1)
						data_cont=data_cont.concat(getCellValueByCell(r.getCell(mi)));
					if (mi==col-1-1)
						data_cont=data_cont.concat("</b>");
					data_cont=data_cont.concat("</td>");

					data_cont=data_cont.concat("</tr>");
				}
				data_cont=data_cont.concat("<tr><td></td><td></td>");
				data_cont=data_cont.concat("<td><b>");
				data_cont=data_cont.concat(getCellValueByCell(sheet.getRow(0).getCell(col-1)));
				data_cont=data_cont.concat("</b></td>");
				data_cont=data_cont.concat("<td><b>");
				data_cont=data_cont.concat(getCellValueByCell(r.getCell(col-1)));
				data_cont=data_cont.concat("</b></td>");
				data_cont=data_cont.concat("</tr>");

				//添加表的后标签
				data_cont += "</table>";
				info.setData_cont(data_cont);
				//存进集合
				salaryInfos.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				//关闭工资表,解除对文件的占用
				workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return salaryInfos;
	}

	//插入工资信息
	@Override
	public int insertSalaryInfo(List<SalaryInfo> salaryList) {
		return opDao.insertSalaryInfo(salaryList);
	}

	//分页查询
	@Override
	public PageBean<SalaryInfo> querySalaryInfoByPage(String _currentPage, String _rows) {
		//类型转换
		int currentPage = Integer.parseInt(_currentPage);
		int rows = Integer.parseInt(_rows);

		//设置PageBean
		PageBean<SalaryInfo> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setRows(rows);

		//查询总页数
		int infoCount = opDao.querySalaryInfoCount();
		pageBean.setTotalCount(infoCount);

		//获得集合
		int start = (currentPage - 1) * rows;
		List<SalaryInfo> list = opDao.querySalaryByPage(start, rows);
		pageBean.setList(list);

		//计算总页码
		int totalPage = (infoCount - 1) / rows + 1;
		pageBean.setTotalPage(totalPage);

		return pageBean;
	}

	//批量删除
	@Override
	public void delSalaryByDataId(String[] data_ids) {
		//遍历每一个对工资删除
		for (String id : data_ids) {
			opDao.delSalaryByDataId(Integer.parseInt(id));
		}
	}

	@Override
	public PageBean<SalaryInfo> querySalaryInfoByPageByKey(String _currentPage, String _rows, Map<String, String[]> conditionMap) {
		//类型转换
		int currentPage = Integer.parseInt(_currentPage);
		int rows = Integer.parseInt(_rows);

		//设置PageBean
		PageBean<SalaryInfo> pageBean = new PageBean<>();
		pageBean.setCurrentPage(currentPage);
		pageBean.setRows(rows);

		//查询总页数
		int infoCount = opDao.querySalaryInfoCount(conditionMap);
		pageBean.setTotalCount(infoCount);

		//获得集合
		int start = (currentPage - 1) * rows;
		List<SalaryInfo> list = opDao.querySalaryByPage(start, rows, conditionMap);
		pageBean.setList(list);

		//计算总页码
		int totalPage = (infoCount - 1) / rows + 1;
		pageBean.setTotalPage(totalPage);

		return pageBean;
	}
	@Override
	public String checkSalaryList(List<SalaryInfo> salaryList) {
		if(salaryList==null)	return null;
		StringBuilder result = new StringBuilder("");
		//记录是第count条信息
		int counter = 0;
		for (SalaryInfo info : salaryList) {
			++counter;
			String emp_id = info.getEmp_id();
			String emp_name = info.getEmp_name();
			User user = opDao.queryUserById(emp_id);
			if (user == null)
				result.append("工资编号" + counter + ":身份证号为:<b>" + emp_id + "</b>的用户未被系统后台表找到<br>");
			else if (!user.getEmp_name().equals(emp_name))
				result.append("工资编号" + counter + ":身份证号为:<b>" + emp_id + "</b>的数据中，姓名与系统后台表中的姓名不同<br>");
		}
		String ret = result.toString();
		if ("".equals(ret))
			return null;
		return ret;
	}

	@Override
	public List<User> queryUserListInfoByKey(String emp_name) {
		return opDao.queryUserListInfoByKey(emp_name);
	}

	@Override
	public boolean activateById(String emp_id) {
		return opDao.activateById(emp_id);
	}

	@Override
	public boolean recoverPasswordById(String emp_id) {
		//更改user密码为身份证后6位(18-12=6)
		return opDao.modifyUserPasswordById(emp_id, emp_id.substring(12));
	}

	@Override
	public boolean delUserById(String emp_id) {
		return opDao.delUserById(emp_id);
	}

	@Override
	public boolean modifyOpPasswordById(String op_id, String op_passwords) {
		return opDao.modifyOpPasswordById(op_id, op_passwords);
	}

	//获取单元格各类型值，返回字符串类型
	private static String getCellValueByCell(Cell cell) {
		//判断是否为null或空串
		if (cell == null || cell.toString().trim().equals("")) {
			return "";
		}
		String cellValue = "";
		int cellType = cell.getCellType();
		if (cellType == Cell.CELL_TYPE_FORMULA) {  //表达式类型
			cellType = evaluator.evaluate(cell).getCellType();
		}

		switch (cellType) {
			case Cell.CELL_TYPE_STRING:  //字符串类型
				cellValue = cell.getStringCellValue().trim();
				cellValue = StringUtils.isEmpty(cellValue) ? "" : cellValue;
				break;
			case Cell.CELL_TYPE_BOOLEAN:   //布尔类型
				cellValue = String.valueOf(cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:  //数值类型
				cellValue = new DecimalFormat("#.##").format(cell.getNumericCellValue());
				break;
			default:  //其它类型，取空串吧
				cellValue = "";
				break;
		}
		//保证小数后尾数不会太长
		if (cellType == Cell.CELL_TYPE_STRING) {
			if (cellValue.contains("."))
				cellValue = cellValue.substring(0, cellValue.lastIndexOf(".") + 3);
		}
		return cellValue;
	}


}