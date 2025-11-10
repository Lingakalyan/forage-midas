// src/main/java/com/jpmc/midascore/incentive/Incentive.java
package com.jpmc.midascore.incentive;

import java.math.BigDecimal;

public class Incentive {
    private BigDecimal amount;

    public Incentive() {}
    public Incentive(BigDecimal amount) { this.amount = amount; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}