package com.bill.domain;

public class SalaryInfo
{
	Integer data_id;//工资流水
	String emp_id;//员工ID
	String emp_name;//员工姓名
	String data_date;//工资日期
	String salary_number;//实发数额
	String data_cont;//工资内容

	public Integer getData_id() {
		return data_id;
	}

	public void setData_id(Integer data_id) {
		this.data_id = data_id;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	public String getData_date() {
		return data_date;
	}

	public void setData_date(String data_date) {
		this.data_date = data_date;
	}

	public String getSalary_number() {
		return salary_number;
	}

	public void setSalary_number(String salary_number) {
		this.salary_number = salary_number;
	}

	public String getData_cont() {
		return data_cont;
	}

	public void setData_cont(String data_cont) {
		this.data_cont = data_cont;
	}

	@Override
	public String toString() {
		return "SalaryInfo{" +
				"data_id=" + data_id +
				", emp_id='" + emp_id + '\'' +
				", emp_name='" + emp_name + '\'' +
				", data_date='" + data_date + '\'' +
				", salary_number='" + salary_number + '\'' +
				", data_cont='" + data_cont + '\'' +
				'}';
	}
}
