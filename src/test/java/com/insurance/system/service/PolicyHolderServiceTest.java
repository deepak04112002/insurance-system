package com.insurance.system.service;

import com.insurance.system.entity.PolicyHolder;
import com.insurance.system.repository.PolicyHolderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyHolderServiceTest {

    @Mock
    private PolicyHolderRepository policyHolderRepository;

    @InjectMocks
    private PolicyHolderService policyHolderService;

    private PolicyHolder testPolicyHolder;

    @BeforeEach
    void setUp() {
        testPolicyHolder = PolicyHolder.builder()
                .id(1L)
                .name("John Smith")
                .email("john@example.com")
                .dob(LocalDate.of(1985, 7, 21))
                .phone("9876543210")
                .build();
    }

    @Test
    void testCreatePolicyHolder() {
        when(policyHolderRepository.save(any(PolicyHolder.class))).thenReturn(testPolicyHolder);

        PolicyHolder result = policyHolderService.create(testPolicyHolder);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        assertEquals("john@example.com", result.getEmail());
        verify(policyHolderRepository).save(testPolicyHolder);
    }

    @Test
    void testGetPolicyHolderById() {
        when(policyHolderRepository.findById(1L)).thenReturn(Optional.of(testPolicyHolder));

        PolicyHolder result = policyHolderService.getById(1L);

        assertNotNull(result);
        assertEquals("John Smith", result.getName());
        verify(policyHolderRepository).findById(1L);
    }

    @Test
    void testGetPolicyHolderByIdNotFound() {
        when(policyHolderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                policyHolderService.getById(1L));
    }
}
