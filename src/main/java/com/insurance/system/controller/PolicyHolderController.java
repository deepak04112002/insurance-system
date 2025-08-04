package com.insurance.system.controller;

import com.insurance.system.entity.PolicyHolder;
import com.insurance.system.service.PolicyHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/policyholders")
public class PolicyHolderController {
    @Autowired
    private PolicyHolderService policyHolderService;

    @PostMapping
    public ResponseEntity<PolicyHolder> create(@RequestBody PolicyHolder policyHolder) {
        return ResponseEntity.ok(policyHolderService.create(policyHolder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PolicyHolder> getById(@PathVariable Long id) {
        return ResponseEntity.ok(policyHolderService.getById(id));
    }
}
