package com.practise.employeeManagementSystem.repository;

import com.practise.employeeManagementSystem.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query("SELECT name FROM Employee name WHERE name.firstName LIKE CONCAT('%',:query,'%')")
    List<Employee> searchEmployees(String query);
}
