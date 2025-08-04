package com.insurance.system.controller;

import com.insurance.system.entity.Policy;
import com.insurance.system.service.PolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/policies")
public class PolicyController {
    @Autowired
    private PolicyService policyService;

    @PostMapping
    public ResponseEntity<Policy> create(@RequestBody Map<String, Object> request) {
        Policy policy = Policy.builder()
                .policyNumber((String) request.get("policyNumber"))
                .type((String) request.get("type"))
                .coverageAmount(Long.valueOf(request.get("coverageAmount").toString()))
                .startDate(java.time.LocalDate.parse((String) request.get("startDate")))
                .endDate(java.time.LocalDate.parse((String) request.get("endDate")))
                .build();

        Long policyHolderId = Long.valueOf(request.get("policyHolderId").toString());
        return ResponseEntity.ok(policyService.create(policy, policyHolderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getById(@PathVariable Long id) {
        return ResponseEntity.ok(policyService.getById(id));
    }
}
