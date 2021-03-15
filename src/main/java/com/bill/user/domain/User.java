package com.bill.user.domain;

public class User
{
	String emp_id;
	String emp_password;
	String emp_name;
	String emp_status;

	public String getEmp_status() {
		return emp_status;
	}

	public void setEmp_status(String emp_status) {
		this.emp_status = emp_status;
	}

	public String getEmp_id() {
		return emp_id;
	}

	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}

	public String getEmp_password() {
		return emp_password;
	}

	public void setEmp_password(String emp_password) {
		this.emp_password = emp_password;
	}

	public String getEmp_name() {
		return emp_name;
	}

	public void setEmp_name(String emp_name) {
		this.emp_name = emp_name;
	}

	@Override
	public String toString() {
		return "User{" +
				"emp_id='" + emp_id + '\'' +
				", emp_password='" + emp_password + '\'' +
				", emp_name='" + emp_name + '\'' +
				", emp_status='" + emp_status + '\'' +
				'}';
	}
}
