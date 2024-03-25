package com.projects147.testing.service;

import com.projects147.testing.exception.ResourceAlreadyPresentException;
import com.projects147.testing.exception.ResourceNotFoundException;
import com.projects147.testing.model.Employee;
import com.projects147.testing.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findByEmail(employee.getEmail());

        if (savedEmployee.isPresent()) {
            throw new ResourceAlreadyPresentException("Employee already exists with email: " + employee.getEmail());
        }

        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty())
            throw new ResourceNotFoundException("Employee not found with Id:" + id);
        return employee;
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        Optional<Employee> savedEmployee = employeeRepository.findById(employee.getId());
        if (savedEmployee.isEmpty())
            throw new ResourceNotFoundException("Employee not found with Id:" + employee.getId());
        return employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(long id) {
        Optional<Employee> savedEmployee = employeeRepository.findById(id);
        if (savedEmployee.isEmpty())
            throw new ResourceNotFoundException("Employee not found with Id:" + id);
        employeeRepository.deleteById(id);
    }

}
