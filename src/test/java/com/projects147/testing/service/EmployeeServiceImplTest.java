package com.projects147.testing.service;

import com.projects147.testing.exception.ResourceAlreadyPresentException;
import com.projects147.testing.exception.ResourceNotFoundException;
import com.projects147.testing.model.Employee;
import com.projects147.testing.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void givenEmployeeObjNew_whenSaveEmployee_thenReturnEmployeeObj() {
        //given
        Employee employee = Employee.builder().firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
//        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
//        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.empty());
        when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then
        assertThat(savedEmployee).isNotNull();
    }

    @Test
    void givenEmployeeObjPresentInDatabase_whenSaveEmployee_thenThrowException() {
        //given
        Employee employee = Employee.builder().firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        //when
        assertThrows(ResourceAlreadyPresentException.class, () -> employeeService.saveEmployee(employee));

        //then
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void givenEmployeeObjs_whenGetAllEmployees_thenReturnEmployeeList() {
        //given
        Employee employee1 = Employee.builder().firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        Employee employee2 = Employee.builder().firstName("Ken").lastName("Doherty").email("mdoherty147@test.com").build();
        when(employeeRepository.findAll()).thenReturn(List.of(employee1, employee2));

        //when
        List<Employee> employees = employeeService.getAllEmployees();

        //then
        assertThat(employees).isNotNull();
        assertThat(employees.size()).isEqualTo(2);
    }

    @Test
    void givenEmptyEmployees_whenGetAllEmployees_thenReturnEmployeeList() {
        //given
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<Employee> employees = employeeService.getAllEmployees();

        //then
        assertThat(employees).isEmpty();
        assertThat(employees.size()).isEqualTo(0);
    }

    @Test
    void givenId_whenGetEmployeeById_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        //when
        Optional<Employee> employeeById = employeeService.getEmployeeById(employee.getId());

        //then
        assertThat(employeeById).isPresent();
        assertThat(employeeById.get()).isNotNull();
    }

    @Test
    void givenId_whenGetEmployeeById_thenThrowException() {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.getEmployeeById(employee.getId()));

        //then
        verify(employeeRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);

        //when
        Employee updatedEmp = employeeService.updateEmployee(employee);

        //then
        assertThat(updatedEmp).isNotNull();
        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void givenEmployee_whenUpdateEmployee_thenThrowException() {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.updateEmployee(employee));

        //then
        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(0)).save(any(Employee.class));
    }

    @Test
    void givenId_whenDeleteEmployee_thenSuccess() {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));
        doNothing().when(employeeRepository).deleteById(employee.getId());

        //when
        employeeService.deleteEmployee(employee.getId());

        //then
        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(1)).deleteById(any());
    }

    @Test
    void givenId_whenDeleteEmployee_thenThrowException() {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby147@test.com").build();
        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.empty());

        //when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.deleteEmployee(employee.getId()));

        //then
        verify(employeeRepository, times(1)).findById(any());
        verify(employeeRepository, times(0)).deleteById(any());
    }

}
