package com.demo.config;

import org.springframework.batch.item.ItemProcessor;

import com.demo.entity.Employee;

public class EmployeeProcessor  implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
	
		return item;
	}

}
