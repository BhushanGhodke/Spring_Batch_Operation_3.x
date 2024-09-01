package com.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="emp_table")
public class Employee {

	@Id
	@Column(name= "id")
	private Integer id;
	@Column(name= "name")
	private String name;
	@Column(name= "salary")
	private String salary;
	@Column(name= "city")
	private String city;
	@Column(name= "address")
	private String address;
	@Column(name= "country")
	private String country;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Employee(Integer id, String name, String salary, String city, String address, String country) {
	
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.city = city;
		this.address = address;
		this.country = country;
	}

	public Employee() {
		
	}

	
	
}
