package com.lizard.mybatis.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.ibatis.type.Alias;

//@Alias("empl")
public class Employee implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String lastName;
	private String email;
	private Double salary;
	private Date hiredate;
	
	private Department department;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Employee() {
		super();

	}

	public Employee(String lastName, String email, Double salary) {
		super();
		this.lastName = lastName;
		this.email = email;
		this.salary = salary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

	public Date getHiredate() {
		return hiredate;
	}

	public void setHiredate(Date hiredate) {
		this.hiredate = hiredate;
	}

	@Override
	public String toString() {
		return "Empolyee [id=" + id + ", lastName=" + lastName + ", email=" + email + ", salary=" + salary
				+ ", hiredate=" + hiredate + "]";
	}

}
