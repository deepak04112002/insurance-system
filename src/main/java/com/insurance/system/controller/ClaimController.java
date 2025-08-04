package com.insurance.system.controller;

import com.insurance.system.entity.Claim;
import com.insurance.system.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    @PostMapping
    public ResponseEntity<Claim> create(@RequestBody Map<String, Object> request) {
        Claim claim = Claim.builder()
                .claimNumber((String) request.get("claimNumber"))
                .claimDate(java.time.LocalDate.parse((String) request.get("claimDate")))
                .amountClaimed(Long.valueOf(request.get("amountClaimed").toString()))
                .status((String) request.get("status"))
                .build();

        Long policyId = Long.valueOf(request.get("policyId").toString());
        return ResponseEntity.ok(claimService.create(claim, policyId));
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam Long claimId,
            @RequestParam MultipartFile file) throws IOException {
        String filePath = claimService.uploadFile(claimId, file);
        return ResponseEntity.ok(Map.of("filePath", filePath));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Claim>> getAll() {
        return ResponseEntity.ok(claimService.getAll());
    }
}
