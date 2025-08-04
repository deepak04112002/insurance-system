package com.insurance.system.service;

import com.insurance.system.entity.PolicyHolder;
import com.insurance.system.repository.PolicyHolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyHolderService {
    @Autowired
    private PolicyHolderRepository policyHolderRepository;

    public PolicyHolder create(PolicyHolder policyHolder) {
        return policyHolderRepository.save(policyHolder);
    }

    public PolicyHolder getById(Long id) {
        return policyHolderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PolicyHolder not found"));
    }
}
