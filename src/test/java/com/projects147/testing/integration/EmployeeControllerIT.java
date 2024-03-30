package com.projects147.testing.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projects147.testing.model.Employee;
import com.projects147.testing.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT extends AbstractionBaseTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
//        Each testcase must run independently. Hence deleting all rows before each test case
        employeeRepository.deleteAll();
    }

    @Test
    void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given
        Employee employee = Employee.builder().firstName("Mark").lastName("Selby").email("mselby@test.com").build();

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
        employeeRepository.saveAll(List.of(employee1, employee2));
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
        Employee employee = Employee.builder().firstName("Mark").lastName("Selby").email("mselby@test.com").build();
        Employee savedEmployee = employeeRepository.save(employee);

        //when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", savedEmployee.getId()));

        //then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Mark")));
    }

}
