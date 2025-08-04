package com.insurance.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "claim_number", unique = true)
    private String claimNumber;

    @Column(name = "claim_date")
    private LocalDate claimDate;

    @Column(name = "amount_claimed")
    private Long amountClaimed;

    @Column(name = "status")
    private String status;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    @JsonIgnore
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "policy_holder_id")
    @JsonIgnore
    private PolicyHolder policyHolder;
}
