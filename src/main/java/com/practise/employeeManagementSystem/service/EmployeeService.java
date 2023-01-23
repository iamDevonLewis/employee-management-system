package com.practise.employeeManagementSystem.service;

import com.practise.employeeManagementSystem.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    Employee findEmployeeById(long id);
    List<Employee> getAllEmployees();
    Employee updateEmployee(Employee employee);
    List<Employee> searchEmployees(String query);
    void deleteEmployee(long id);


}
