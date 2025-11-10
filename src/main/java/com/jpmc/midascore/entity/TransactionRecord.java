// src/main/java/com/jpmc/midascore/entity/TransactionRecord.java
package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
public class TransactionRecord {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false) @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne(optional = false) @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal incentiveAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public void setSender(UserRecord sender) {
    }

    public void setRecipient(UserRecord recipient) {
    }

    public void setAmount(BigDecimal amount) {
    }

    public void setIncentiveAmount(BigDecimal incentive) {
    }

    // getters/setters/constructors...
}