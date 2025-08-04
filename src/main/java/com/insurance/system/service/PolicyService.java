package com.insurance.system.service;

import com.insurance.system.entity.Policy;
import com.insurance.system.repository.PolicyRepository;
import com.insurance.system.repository.PolicyHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {
    @Autowired
    private PolicyRepository policyRepository;
    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    public Policy create(Policy policy, Long policyHolderId) {
        policy.setPolicyHolder(policyHolderRepository.findById(policyHolderId)
                .orElseThrow(() -> new RuntimeException("PolicyHolder not found")));
        return policyRepository.save(policy);
    }

    public Policy getById(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Policy not found"));
    }
}
