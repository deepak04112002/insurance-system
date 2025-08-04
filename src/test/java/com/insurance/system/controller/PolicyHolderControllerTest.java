package com.insurance.system.controller;

import com.insurance.system.entity.PolicyHolder;
import com.insurance.system.service.PolicyHolderService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PolicyHolderController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = com.insurance.system.security.JwtAuthenticationFilter.class))
class PolicyHolderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyHolderService policyHolderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        PolicyHolder policyHolder = PolicyHolder.builder()
                .id(1L)
                .name("John Smith")
                .email("john@example.com")
                .dob(LocalDate.of(1985, 7, 21))
                .phone("9876543210")
                .build();

        when(policyHolderService.create(any(PolicyHolder.class))).thenReturn(policyHolder);

        mockMvc.perform(post("/api/policyholders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(policyHolder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.phone").value("9876543210"));
    }

    @Test
    void getById() throws Exception {
        PolicyHolder policyHolder = PolicyHolder.builder()
                .id(1L)
                .name("John Smith")
                .email("john@example.com")
                .phone("9876543210")
                .build();

        when(policyHolderService.getById(1L)).thenReturn(policyHolder);

        mockMvc.perform(get("/api/policyholders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }
}
