// package com.example.springbeginner.service;

// import com.example.springbeginner.model.Employee;
// import com.example.springbeginner.repository.EmployeeRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import java.util.List;

// @Service
// public class EmployeeService {

//     @Autowired
//     private EmployeeRepository employeeRepository;

//     public List<Employee> getAllEmployees() {
//         return employeeRepository.findAll();
//     }

//     public Employee getEmployeeById(Long id) {
//         return employeeRepository.findById(id).orElse(null);
//     }

//     public Employee createEmployee(Employee employee) {
//         return employeeRepository.save(employee);
//     }

//     public Employee updateEmployee(Long id, Employee employee) {
//         Employee existingEmployee = employeeRepository.findById(id).orElse(null);
//         if (existingEmployee != null) {
//             existingEmployee.setName(employee.getName());
//             existingEmployee.setPosition(employee.getPosition());
//             existingEmployee.setEmail(employee.getEmail());
//             return employeeRepository.save(existingEmployee);
//         }
//         return null;
//     }

//     public void deleteEmployee(Long id) {
//         employeeRepository.deleteById(id);
//     }
// }
