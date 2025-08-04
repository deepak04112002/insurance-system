package com.insurance.system.controller;

import com.insurance.system.entity.Claim;
import com.insurance.system.service.ClaimService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClaimController.class,
        excludeAutoConfiguration = {
                org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
                org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration.class
        },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = com.insurance.system.security.JwtAuthenticationFilter.class))
class ClaimControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClaimService claimService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        Claim claim = Claim.builder()
                .id(1L)
                .claimNumber("CLM001")
                .claimDate(LocalDate.of(2024, 12, 23))
                .amountClaimed(50000L)
                .status("PENDING")
                .build();

        when(claimService.create(any(Claim.class), eq(1L))).thenReturn(claim);

        Map<String, Object> request = Map.of(
                "claimNumber", "CLM001",
                "claimDate", "2024-12-23",
                "amountClaimed", 50000,
                "status", "PENDING",
                "policyId", 1
        );

        mockMvc.perform(post("/api/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.claimNumber").value("CLM001"))
                .andExpect(jsonPath("$.amountClaimed").value(50000));
    }

    @Test
    void uploadFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "test content".getBytes());

        when(claimService.uploadFile(1L, file)).thenReturn("/path/to/file");

        mockMvc.perform(multipart("/api/claims/upload")
                        .file(file)
                        .param("claimId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.filePath").value("/path/to/file"));
    }

    @Test
    void getById() throws Exception {
        Claim claim = Claim.builder()
                .id(1L)
                .claimNumber("CLM001")
                .amountClaimed(50000L)
                .build();

        when(claimService.getById(1L)).thenReturn(claim);

        mockMvc.perform(get("/api/claims/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.claimNumber").value("CLM001"));
    }

    @Test
    void getAll() throws Exception {
        List<Claim> claims = List.of(
                Claim.builder().id(1L).claimNumber("CLM001").build(),
                Claim.builder().id(2L).claimNumber("CLM002").build()
        );

        when(claimService.getAll()).thenReturn(claims);

        mockMvc.perform(get("/api/claims/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].claimNumber").value("CLM001"));
    }
}
