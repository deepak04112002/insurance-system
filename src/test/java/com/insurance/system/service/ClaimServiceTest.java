package com.insurance.system.service;

import com.insurance.system.entity.Claim;
import com.insurance.system.entity.Policy;
import com.insurance.system.entity.PolicyHolder;
import com.insurance.system.repository.ClaimRepository;
import com.insurance.system.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClaimServiceTest {

    @Mock
    private ClaimRepository claimRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private ClaimService claimService;

    private Claim testClaim;
    private Policy testPolicy;
    private PolicyHolder testPolicyHolder;

    @BeforeEach
    void setUp() {
        testPolicyHolder = PolicyHolder.builder()
                .id(1L)
                .name("John Smith")
                .email("john@example.com")
                .build();

        testPolicy = Policy.builder()
                .id(1L)
                .policyNumber("POL123456")
                .type("Health")
                .policyHolder(testPolicyHolder)
                .build();

        testClaim = Claim.builder()
                .id(1L)
                .claimNumber("CLM001")
                .claimDate(LocalDate.now())
                .amountClaimed(50000L)
                .status("PENDING")
                .policy(testPolicy)
                .policyHolder(testPolicyHolder)
                .build();
    }

    @Test
    void testCreateClaim() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(claimRepository.save(any(Claim.class))).thenReturn(testClaim);

        Claim result = claimService.create(testClaim, 1L);

        assertNotNull(result);
        assertEquals("CLM001", result.getClaimNumber());
        assertEquals(testPolicy, result.getPolicy());
        assertEquals(testPolicyHolder, result.getPolicyHolder());
        verify(claimRepository).save(testClaim);
    }

    @Test
    void testGetAllClaims() {
        List<Claim> claims = Arrays.asList(testClaim);
        when(claimRepository.findAll()).thenReturn(claims);

        List<Claim> result = claimService.getAll();

        assertEquals(1, result.size());
        assertEquals("CLM001", result.get(0).getClaimNumber());
        verify(claimRepository).findAll();
    }

    @Test
    void testCreateClaimPolicyNotFound() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                claimService.create(testClaim, 1L));
    }

    @Test
    void testGetByIdSuccess() {
        when(claimRepository.findById(1L)).thenReturn(Optional.of(testClaim));

        Claim result = claimService.getById(1L);

        assertEquals("CLM001", result.getClaimNumber());
        verify(claimRepository).findById(1L);
    }

    @Test
    void testGetByIdNotFound() {
        when(claimRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                claimService.getById(1L));
    }

}
