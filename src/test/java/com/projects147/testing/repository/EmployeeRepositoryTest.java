package com.projects147.testing.repository;

import com.projects147.testing.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void givenEmployeeObject_whenSaved_thenReturnSavedEmployee() {
        Employee employee = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();

        Employee savedEmployee = employeeRepository.save(employee);

        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
        assertThat(savedEmployee.getEmail()).isEqualTo("rroy@test.com");
    }

    @Test
    void givenEmployeeObjects_whenSaved_thenReturnSavedEmployees() {
        Employee employee1 = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();
        Employee employee2 = Employee.builder().firstName("Joseph").lastName("Kirk").email("jkirk@test.com").build();

        List<Employee> savedEmployees = employeeRepository.saveAll(List.of(employee1, employee2));

        assertThat(savedEmployees).isNotNull();
        assertThat(savedEmployees.size()).isEqualTo(2);
        assertThat(savedEmployees).contains(employee2);
    }

    @Test
    void givenEmployeeObj_whenFindById_thenReturnThatEmployee() {
        //given
        Employee employee1 = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();
        Employee employee2 = Employee.builder().firstName("Joseph").lastName("Kirk").email("jkirk@test.com").build();
        employeeRepository.saveAll(List.of(employee1, employee2));

        //when
        Employee employee = employeeRepository.findById(employee2.getId()).get();

        //then
        assertThat(employee).isNotNull();
        assertThat(employee.getLastName()).isEqualTo("Kirk");
    }

    @Test
    void givenEmployeeObj_whenFindByEmail_thenReturnEmployee() {
        //given
        Employee employee1 = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();
        Employee employee2 = Employee.builder().firstName("Joseph").lastName("Kirk").email("jkirk@test.com").build();
        employeeRepository.saveAll(List.of(employee1, employee2));

        //when
        Employee employee = employeeRepository.findByEmail(employee1.getEmail()).get();

        //then
        assertThat(employee).isNotNull();
        assertThat(employee.getLastName()).isEqualTo("Roy");
    }

    @Test
    void givenEmployeeObj_whenSaveUpdatedEmployee_thenReturnUpdatedEmployee() {
        //given
        Employee employee = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();
        employeeRepository.save(employee);

        //when
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("rroy1@test.com");
        savedEmployee.setFirstName("Rohan");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        //then
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("rroy1@test.com");
        assertThat(updatedEmployee.getFirstName()).isNotEqualTo("Ronny");
    }

    @Test
    void givenEmployeeObj_whenDeleted_thenReturnEmpty() {
        //given
        Employee employee = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();
        employeeRepository.save(employee);

        //when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        //then
        assertThat(optionalEmployee).isEmpty();
    }

    @Test
    void givenEmployeeObj_whenFindByEmployeeFirstNameAndLastName_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder().firstName("Ronny").lastName("Roy").email("rroy@test.com").build();
        employeeRepository.save(employee);

        //when
        Employee result = employeeRepository.findByEmployeeFirstNameAndLastName(employee.getFirstName(), employee.getLastName());

        //then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("rroy@test.com");
    }

}