package com.bill.op.domain;

public class Op
{
	String op_id;//管理员ID
	String op_password;//管理员密码

	public String getOp_id() {
		return op_id;
	}

	public void setOp_id(String op_id) {
		this.op_id = op_id;
	}

	public String getOp_password() {
		return op_password;
	}

	public void setOp_password(String op_password) {
		this.op_password = op_password;
	}

	@Override
	public String toString() {
		return "Op{" +
				"op_id='" + op_id + '\'' +
				", op_password='" + op_password + '\'' +
				'}';
	}
}
