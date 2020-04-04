package com.csv.integration.kafka.streams.demo.dao;

import com.csv.integration.kafka.streams.demo.data.Employee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByLastName(String name);
    List<Employee> findByFirstName(String name);

}
