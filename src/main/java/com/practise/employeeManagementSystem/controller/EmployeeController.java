package com.practise.employeeManagementSystem.controller;

import com.practise.employeeManagementSystem.entity.Employee;
import com.practise.employeeManagementSystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EmployeeController {

    private EmployeeService employeeService;
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping("/employee/search")
    public String searchEmployee(@RequestParam("query") String query, Model model){
        List<Employee> employees= employeeService.searchEmployees(query);
        model.addAttribute("employees", employees);
        return "employee-list";
    }


    @GetMapping("/employee/new")
    public String registrationForm(Model model){
        Employee employee = new Employee();
        model.addAttribute("employee", employee);
        return "registration-form";
    }
    @PostMapping("/employee/save")
    public String saveEmployee(@ModelAttribute ("employee") Employee employee){
        employeeService.saveEmployee(employee);
        return "redirect:/employee";
    }

    @GetMapping("/employee")
    public String employeeList(Model model){
        model.addAttribute("employees", employeeService.getAllEmployees());
        return "employee-list";

    }
    @GetMapping("/employee/update/{id}")
    public String editEmployeeForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("employee", employeeService.findEmployeeById(id));
        return "updated-form";
    }

    @PostMapping("/employee/{id}")
    public String updateEmployee(@PathVariable("id") Long id,@ModelAttribute("employee") Employee employee, Model model){
        // get employee from database by id
        Employee existingEmployee = employeeService.findEmployeeById(id);
        existingEmployee.setId(id);
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setCity(employee.getCity());

        // save updated employee object
        employeeService.updateEmployee(existingEmployee);
        return "redirect:/employee";

    }

    @GetMapping("/employee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        this.employeeService.deleteEmployee(id);
        return "redirect:/employee";
    }


}
