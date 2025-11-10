package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_record")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Name/username used by tests' populator
    @Column(nullable = false, unique = true)
    private String name;

    // Balance used by tests' populator
    @Column(nullable = false)
    private float balance;

    // --- JPA needs this ---
    public UserRecord() { }

    // --- Tests expect this ---
    public UserRecord(String name, float balance) {
        this.name = name;
        this.balance = balance;
    }

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public float getBalance() { return balance; }
    public void setBalance(float balance) { this.balance = balance; }
}