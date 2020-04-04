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
        // TODO: 3/24/20 combine data
        if (emp.isPresent()) {
            Employee employee = emp.get();
            if (employee.getAddresses() != null && employee.getAddresses().size() > 0) {
                employeeDTO.setAddresses(employee.getAddresses());
            }
        }
        return employeeDTO;
    }
}
