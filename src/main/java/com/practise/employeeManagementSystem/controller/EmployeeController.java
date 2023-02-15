package com.practise.employeeManagementSystem.controller;

import com.practise.employeeManagementSystem.entity.Employee;
import com.practise.employeeManagementSystem.entity.Role;
import com.practise.employeeManagementSystem.repository.RoleRepository;
import com.practise.employeeManagementSystem.securityConfig.SecurityUtil;
import com.practise.employeeManagementSystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class EmployeeController {

    private EmployeeService employeeService;
    private RoleRepository roleRepository;
    private static PasswordEncoder passwordEncoder;
    @Autowired
    public EmployeeController(EmployeeService employeeService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.employeeService = employeeService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String homePage(){
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(){
        return "login";
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
        Employee user = new Employee();
        user.setFirstName(employee.getFirstName());
        user.setLastName(employee.getLastName());
        user.setCity(employee.getCity());
        user.setEmail(employee.getEmail());
        user.setPassword(passwordEncoder.encode(employee.getPassword()));
        Role role = roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(role));
//        userRepository.save(user);
        employeeService.saveEmployee(user);
        return "redirect:/login?success";
    }

    @GetMapping("/employee")
    public String employeeList(Model model){
        Employee currentUser = new Employee();
        model.addAttribute("employees", employeeService.getAllEmployees());
        String email = SecurityUtil.getSessionUser();
        if(email != null) {
            currentUser = employeeService.findByEmail(email);
            model.addAttribute("currentUser", currentUser);
        }
        model.addAttribute("employee", currentUser);

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
        existingEmployee.setPassword(employee.getPassword());

        // save updated employee object
        employeeService.updateEmployee(existingEmployee);
        return "redirect:/employee?update";

    }

    @GetMapping("/employee/{id}")
    public String deleteEmployee(@PathVariable("id") Long id){
        this.employeeService.deleteEmployee(id);
        return "redirect:/employee";
    }

//    @GetMapping("/user")
//    public String currentUser(Authentication authentication, Model model){
//        return authentication.getName();
//    }


}
