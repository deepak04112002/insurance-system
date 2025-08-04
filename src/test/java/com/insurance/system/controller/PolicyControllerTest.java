package com.insurance.system.controller;

import com.insurance.system.entity.Policy;
import com.insurance.system.service.PolicyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PolicyController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = com.insurance.system.security.JwtAuthenticationFilter.class))
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        Policy policy = Policy.builder()
                .id(1L)
                .policyNumber("POL123456")
                .type("Health")
                .coverageAmount(500000L)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2029, 1, 1))
                .build();

        when(policyService.create(any(Policy.class), eq(1L))).thenReturn(policy);

        Map<String, Object> request = Map.of(
                "policyNumber", "POL123456",
                "type", "Health",
                "coverageAmount", 500000,
                "startDate", "2024-01-01",
                "endDate", "2029-01-01",
                "policyHolderId", 1
        );

        mockMvc.perform(post("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNumber").value("POL123456"))
                .andExpect(jsonPath("$.type").value("Health"))
                .andExpect(jsonPath("$.coverageAmount").value(500000));
    }

    @Test
    void getById() throws Exception {
        Policy policy = Policy.builder()
                .id(1L)
                .policyNumber("POL123456")
                .type("Health")
                .coverageAmount(500000L)
                .build();

        when(policyService.getById(1L)).thenReturn(policy);

        mockMvc.perform(get("/api/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.policyNumber").value("POL123456"))
                .andExpect(jsonPath("$.type").value("Health"));
    }
}
