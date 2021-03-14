package com.csv.integration.kafka.streams.demo.service;

import com.csv.integration.kafka.streams.demo.dao.EmployeeRepository;
import com.csv.integration.kafka.streams.demo.data.Employee;
import com.csv.integration.kafka.streams.demo.data.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeService(EmployeeRepository emp) {
        employeeRepository = emp;
    }

    public EmployeeDTO enrich(EmployeeDTO employeeDTO) {
        logger.debug("enriching the employee");
        Optional<Employee> emp = employeeRepository.findById(employeeDTO.getEmployeeId());

        if (emp.isPresent()) {
            Employee employee = emp.get();
            if (employee.getAddresses() != null && employee.getAddresses().size() > 0) {
                employeeDTO.setAddresses(employee.getAddresses());
            }
            save(employee, employeeDTO);
        } else {
            logger.info(String.format("Employee with id = %d does not exist", employeeDTO.getEmployeeId()));
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
