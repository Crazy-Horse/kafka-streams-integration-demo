package com.csv.integration.kafka.streams.demo.service;

import com.csv.integration.kafka.streams.demo.dao.EmployeeRepository;
import com.csv.integration.kafka.streams.demo.data.Employee;
import com.csv.integration.kafka.streams.demo.data.EmployeeDTO;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;


    public EmployeeService(EmployeeRepository emp) {
        employeeRepository = emp;
    }

    public EmployeeDTO enrich(EmployeeDTO employeeDTO) {
        Optional<Employee> emp = employeeRepository.findById(employeeDTO.getEmployeeId());

        if (emp.isPresent()) {
            Employee employee = emp.get();
            if (employee.getAddresses() != null && employee.getAddresses().size() > 0) {
                employeeDTO.setAddresses(employee.getAddresses());
            }
            save(employee, employeeDTO);
        }
        return employeeDTO;
    }

    public void save(Employee employee, EmployeeDTO emp) {
        employee.setDepartmentId(emp.getDepartmentId());
        employee.setYearsOfService(emp.getYearsOfService());
        employee.setManagerId(emp.getManagerId());

        employeeRepository.save(employee);
    }
}
