package com.projects147.testing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects147.testing.model.Employee;
import com.projects147.testing.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder().firstName("Mark").lastName("Selby").email("mselby@test.com").build();
        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        //when
        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @Test
    void givenEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {
        //given
        Employee employee1 = Employee.builder().firstName("Mark").lastName("Selby").email("mselby@test.com").build();
        Employee employee2 = Employee.builder().firstName("Judd").lastName("Trump").email("jtrump@test.com").build();
        when(employeeService.getAllEmployees()).thenReturn(List.of(employee1, employee2));

        //when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        //given
        Employee employee = Employee.builder().id(1L).firstName("Mark").lastName("Selby").email("mselby@test.com").build();
        when(employeeService.getEmployeeById(employee.getId())).thenReturn(Optional.of(employee));

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Mark")));
    }

}
